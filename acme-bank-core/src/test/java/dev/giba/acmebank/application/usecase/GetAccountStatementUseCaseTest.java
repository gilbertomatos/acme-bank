package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.GetAccountStatementRequest;
import dev.giba.acmebank.application.boundary.output.*;
import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.entity.AccountTransaction;
import dev.giba.acmebank.domain.entity.TransactionType;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccountStatementUseCaseTest {
    @Mock
    private GetAccountStatementUseCaseOutput mockedGetAccountStatementUseCaseOutput;
    @Mock
    private AccountEntityGateway mockedAccountEntityGateway;
    @Mock
    private ReadOnlyTransaction mockedReadOnlyTransaction;
    @Captor
    private ArgumentCaptor<Result<GetAccountStatementResponse>> resultArgumentCaptor;

    private GetAccountStatementUseCase getAccountStatementUseCase;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedGetAccountStatementUseCaseOutput);
        reset(this.mockedAccountEntityGateway);
        reset(this.mockedReadOnlyTransaction);

        this.getAccountStatementUseCase = new GetAccountStatementUseCase(this.mockedGetAccountStatementUseCaseOutput,
                this.mockedAccountEntityGateway, this.mockedReadOnlyTransaction);
    }

    @Test
    @DisplayName("Should return an error when given a null account number")
    void shouldReturnAnErrorWhenGivenAnNullAccountNumber() {
        //Given
        var getAccountStatementRequest = new GetAccountStatementRequest(null);

        //When
        this.getAccountStatementUseCase.execute(getAccountStatementRequest);

        //Then
        verify(this.mockedGetAccountStatementUseCaseOutput, times(1))
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
        var getAccountStatementRequest = new GetAccountStatementRequest(number);

        //When
        this.getAccountStatementUseCase.execute(getAccountStatementRequest);

        //Then
        verify(this.mockedGetAccountStatementUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account number is mandatory");
    }

    @Test
    @DisplayName("Should return an error when given a nonexistent account")
    void shouldReturnAnErrorWhenGivenANonexistentAccount() {
        //Given
        var number = "000002";
        var getAccountStatementRequest = new GetAccountStatementRequest(number);

        when(this.mockedAccountEntityGateway.findByNumber(number)).thenReturn(Optional.empty());

        this.stubMockedAtomicExecutor();

        //When
        this.getAccountStatementUseCase.execute(getAccountStatementRequest);

        //Then
        verify(this.mockedReadOnlyTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumber(number);
        verify(this.mockedGetAccountStatementUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isFailure());
        assertThat(this.resultArgumentCaptor.getValue().errors())
                .containsOnly("Account not found");

    }

    @Test
    @DisplayName("Should get account statement correctly")
    void shouldGetAccountStatementCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000001";
        var balance = BigDecimal.TEN;

        var accountTransaction = new AccountTransaction(TransactionType.DEPOSIT, BigDecimal.TEN, "Deposit 1");

        var account = new Account(id, number, balance, List.of(accountTransaction));

        var getAccountStatementRequest = new GetAccountStatementRequest(number);

        when(this.mockedAccountEntityGateway.findByNumber(number)).thenReturn(Optional.of(account));

        this.stubMockedAtomicExecutor();

        //When
        this.getAccountStatementUseCase.execute(getAccountStatementRequest);

        //Then
        verify(this.mockedReadOnlyTransaction, times(1)).execute(any(Runnable.class));
        verify(this.mockedAccountEntityGateway, times(1)).findByNumber(number);

        verify(this.mockedGetAccountStatementUseCaseOutput, times(1))
                .execute(this.resultArgumentCaptor.capture());

        assertTrue(this.resultArgumentCaptor.getValue().isSuccess());
        assertEquals(number, this.resultArgumentCaptor.getValue().value().accountNumber());
        assertEquals(balance, this.resultArgumentCaptor.getValue().value().balance());
        assertEquals(1, this.resultArgumentCaptor.getValue().value().transactions().size());
        assertEquals(accountTransaction.format(), this.resultArgumentCaptor.getValue().value()
                .transactions().getFirst());
    }

    private void stubMockedAtomicExecutor() {
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(this.mockedReadOnlyTransaction).execute(any(Runnable.class));
    }
}
