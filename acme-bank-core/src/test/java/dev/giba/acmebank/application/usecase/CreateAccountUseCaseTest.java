package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.output.CreateAccountResponse;
import dev.giba.acmebank.application.boundary.output.CreateAccountUseCaseOutput;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private CreateAccountUseCaseOutput mockedCreateAccountUseCaseOutput;
    @Mock
    private AccountEntityGateway mockedAccountEntityGateway;
    @Mock
    private Transaction mockedTransaction;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    private ArgumentCaptor<Result<CreateAccountResponse>> resultArgumentCaptor;

    private CreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedCreateAccountUseCaseOutput);
        reset(this.mockedAccountEntityGateway);
        reset(this.mockedTransaction);

        this.createAccountUseCase = new CreateAccountUseCase(this.mockedCreateAccountUseCaseOutput,
                this.mockedAccountEntityGateway, this.mockedTransaction);
    }

    @Test
    @DisplayName("Should return an error when given a null account number")
    void shouldReturnAnErrorWhenGivenAnNullAccountNumber() {
        //Given
        var createAccountRequest = new CreateAccountRequest(null);

        //When
        this.createAccountUseCase.execute(createAccountRequest);

        //Then
        verify(this.mockedCreateAccountUseCaseOutput, times(1))
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
        var createAccountRequest = new CreateAccountRequest(number);

        //When
        this.createAccountUseCase.execute(createAccountRequest);

        //Then
        verify(this.mockedCreateAccountUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account number is mandatory");
    }

    @Test
    @DisplayName("Should return an error when a existent account")
    void shouldReturnAnErrorWhenGivenAExistentAccount() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000002";
        var balance = BigDecimal.TEN;
        var account = new Account(id, number, balance, Collections.emptyList());

        var createAccountRequest = new CreateAccountRequest(number);

        when(this.mockedAccountEntityGateway.findByNumber(number)).thenReturn(Optional.of(account));

        this.stubMockedAtomicExecutor();

        //When
        this.createAccountUseCase.execute(createAccountRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumber(number);
        verify(this.mockedCreateAccountUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account already exists");

    }

    @Test
    @DisplayName("Should create account correctly")
    void shouldCreateAccountCorrectly() {
        //Given
        var number = "000001";
        var balance = BigDecimal.ZERO;

        var createAccountRequest = new CreateAccountRequest(number);

        when(this.mockedAccountEntityGateway.findByNumber(number)).thenReturn(Optional.empty());
        doNothing().when(this.mockedAccountEntityGateway).save(any(Account.class));

        this.stubMockedAtomicExecutor();

        //When
        this.createAccountUseCase.execute(createAccountRequest);

        //Then
        verify(this.mockedTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumber(number);
        verify(this.mockedAccountEntityGateway, times(1))
                .save(this.accountArgumentCaptor.capture());
        verify(this.mockedCreateAccountUseCaseOutput, times(1))
                .present(this.resultArgumentCaptor.capture());

        assertEquals(number, this.accountArgumentCaptor.getValue().number());
        assertEquals(balance, this.accountArgumentCaptor.getValue().balance());

        assertTrue(this.resultArgumentCaptor.getValue().isSuccess());
        assertEquals(number, this.resultArgumentCaptor.getValue().value().accountNumber());
        assertEquals(balance, this.resultArgumentCaptor.getValue().value().balance());
    }

    private void stubMockedAtomicExecutor() {
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(this.mockedTransaction).execute(any(Runnable.class));
    }
}
