package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.DepositRequest;
import dev.giba.acmebank.application.boundary.output.DepositResponse;
import dev.giba.acmebank.application.boundary.output.DepositUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.Result;
import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositUseCaseTest {
    @Mock
    private DepositUseCaseOutput mockedDepositUseCaseOutput;
    @Mock
    private AccountEntityGateway mockedAccountEntityGateway;
    @Mock
    private Transaction mockedTransaction;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    private ArgumentCaptor<Result<DepositResponse>> resultArgumentCaptor;

    private DepositUseCase depositUseCase;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedDepositUseCaseOutput);
        reset(this.mockedAccountEntityGateway);
        reset(this.mockedTransaction);

        this.depositUseCase = new DepositUseCase(this.mockedDepositUseCaseOutput, this.mockedAccountEntityGateway,
                this.mockedTransaction);
    }

    @Test
    @DisplayName("Should return an error when given a null account number")
    void shouldReturnAnErrorWhenGivenAnNullAccountNumber() {
        //Given
        var amount = BigDecimal.TEN;
        var depositRequest = new DepositRequest(null, amount);

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account number is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given an empty account number")
    void shouldReturnAnErrorWhenGivenAnEmptyAccountNumber() {
        //Given
        var number = "";
        var amount = BigDecimal.TEN;
        var depositRequest = new DepositRequest(number, amount);

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account number is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given a null amount")
    void shouldReturnAnErrorWhenGivenAnNullAmount() {
        //Given
        var number = "000123";
        var depositRequest = new DepositRequest(number, null);

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("A valid amount is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given an empty amount")
    void shouldReturnAnErrorWhenGivenAnEmptyAmount() {
        //Given
        var number = "000124";
        var amount = BigDecimal.ZERO;
        var depositRequest = new DepositRequest(number, amount);

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("A valid amount is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given a nonexistent account")
    void shouldReturnAnErrorWhenGivenANonexistentAccount() {
        //Given
        var number = "000002";
        var amount = BigDecimal.TEN;
        var depositRequest = new DepositRequest(number, amount);

        when(this.mockedAccountEntityGateway.findByNumberForUpdate(number)).thenReturn(Optional.empty());

        this.stubMockedAtomicExecutor();

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumberForUpdate(number);
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account not found");

    }

    @Test
    @DisplayName("Should deposit correctly")
    void shouldDepositCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000001";
        var balance = BigDecimal.ZERO;
        var account = new Account(id, number, balance, Collections.emptyList());

        var amount = BigDecimal.TEN;
        var depositRequest = new DepositRequest(number, amount);

        when(this.mockedAccountEntityGateway.findByNumberForUpdate(number)).thenReturn(Optional.of(account));
        doNothing().when(this.mockedAccountEntityGateway).save(any(Account.class));

        this.stubMockedAtomicExecutor();

        //When
        this.depositUseCase.execute(depositRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumberForUpdate(number);
        verify(this.mockedAccountEntityGateway, times(1))
                .save(this.accountArgumentCaptor.capture());
        verify(this.mockedDepositUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertEquals(number, this.accountArgumentCaptor.getValue().number());
        assertEquals(amount, this.accountArgumentCaptor.getValue().balance());

        assertTrue(this.resultArgumentCaptor.getValue().isSuccess());
        assertEquals(number, this.resultArgumentCaptor.getValue().value().accountNumber());
        assertEquals(amount, this.resultArgumentCaptor.getValue().value().balance());
    }

    private void stubMockedAtomicExecutor() {
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(this.mockedTransaction).execute(any(Runnable.class));
    }
}
