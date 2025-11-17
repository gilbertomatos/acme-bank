package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.WithdrawRequest;
import dev.giba.acmebank.application.boundary.output.*;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawUseCaseTest {

    @Mock
    private WithdrawUseCaseOutput mockedWithdrawUseCaseOutput;
    @Mock
    private AccountEntityGateway mockedAccountEntityGateway;
    @Mock
    private Transaction mockedTransaction;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    private ArgumentCaptor<Result<WithdrawResponse>> resultArgumentCaptor;

    private WithdrawUseCase withdrawUseCase;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedWithdrawUseCaseOutput);
        reset(this.mockedAccountEntityGateway);
        reset(this.mockedTransaction);

        this.withdrawUseCase = new WithdrawUseCase(this.mockedWithdrawUseCaseOutput, this.mockedAccountEntityGateway,
                this.mockedTransaction);
    }

    @Test
    @DisplayName("Should return an error when given a null account number")
    void shouldReturnAnErrorWhenGivenAnNullAccountNumber() {
        //Given
        var amount = BigDecimal.TEN;
        var withdrawRequest = new WithdrawRequest(null, amount);

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

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
        var withdrawRequest = new WithdrawRequest(number, amount);

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account number is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given a null amount")
    void shouldReturnAnErrorWhenGivenAnNullAmount() {
        //Given
        var number = "000123";
        var withdrawRequest = new WithdrawRequest(number, null);

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

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
        var withdrawRequest = new WithdrawRequest(number, amount);

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

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
        var withdrawRequest = new WithdrawRequest(number, amount);

        when(this.mockedAccountEntityGateway.findByNumberForUpdate(number)).thenReturn(Optional.empty());

        this.stubMockedAtomicExecutor();

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumberForUpdate(number);
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account not found");

    }

    @Test
    @DisplayName("Should return an error when given an amount greater than the balance")
    void shouldReturnAnErrorWhenGivenAnAmountGreaterThanTheBalance() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000022";
        var amount = BigDecimal.TEN;
        var balance = BigDecimal.TWO;
        var account = new Account(id, number, balance, Collections.emptyList());

        var withdrawRequest = new WithdrawRequest(number, amount);

        when(this.mockedAccountEntityGateway.findByNumberForUpdate(number)).thenReturn(Optional.of(account));

        this.stubMockedAtomicExecutor();

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumberForUpdate(number);
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Insufficient balance");

    }

    @Test
    @DisplayName("Should withdraw correctly")
    void shouldWithdrawCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000001";
        var balance = BigDecimal.TEN;
        var account = new Account(id, number, balance, Collections.emptyList());
        var expectedBalance = BigDecimal.valueOf(8);
        var amount = BigDecimal.TWO;
        var withdrawRequest = new WithdrawRequest(number, amount);

        when(this.mockedAccountEntityGateway.findByNumberForUpdate(number)).thenReturn(Optional.of(account));
        doNothing().when(this.mockedAccountEntityGateway).save(any(Account.class));

        this.stubMockedAtomicExecutor();

        //When
        this.withdrawUseCase.execute(withdrawRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumberForUpdate(number);
        verify(this.mockedAccountEntityGateway, times(1))
                .save(this.accountArgumentCaptor.capture());
        verify(this.mockedWithdrawUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertEquals(number, this.accountArgumentCaptor.getValue().number());
        assertEquals(expectedBalance, this.accountArgumentCaptor.getValue().balance());

        assertTrue(this.resultArgumentCaptor.getValue().isSuccess());
        assertEquals(number, this.resultArgumentCaptor.getValue().value().accountNumber());
        assertEquals(expectedBalance, this.resultArgumentCaptor.getValue().value().balance());
    }

    private void stubMockedAtomicExecutor() {
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(this.mockedTransaction).execute(any(Runnable.class));
    }
}
