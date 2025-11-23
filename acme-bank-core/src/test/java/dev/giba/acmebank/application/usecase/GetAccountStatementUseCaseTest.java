package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementOutputBoundary;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementRequest;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementResponse;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementUseCase;
import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.entity.AccountTransaction;
import dev.giba.acmebank.domain.entity.TransactionType;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.ReadOnlyTransaction;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccountStatementUseCaseTest {
    @Mock
    private GetAccountStatementOutputBoundary mockedGetAccountStatementOutputBoundary;
    @Mock
    private AccountEntityGateway mockedAccountEntityGateway;
    @Mock
    private ReadOnlyTransaction mockedReadOnlyTransaction;
    @Captor
    private ArgumentCaptor<GetAccountStatementResponse> getAccountStatementResponseArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<String>> listArgumentCaptor;

    private GetAccountStatementUseCase getAccountStatementUseCase;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedGetAccountStatementOutputBoundary);
        reset(this.mockedAccountEntityGateway);
        reset(this.mockedReadOnlyTransaction);

        this.getAccountStatementUseCase = new GetAccountStatementUseCase(this.mockedGetAccountStatementOutputBoundary,
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
        verify(this.mockedGetAccountStatementOutputBoundary, times(1))
                .present(this.listArgumentCaptor.capture());

        assertThat(this.listArgumentCaptor.getValue())
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
        verify(this.mockedGetAccountStatementOutputBoundary, times(1))
                .present(this.listArgumentCaptor.capture());

        assertThat(this.listArgumentCaptor.getValue())
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
        verify(this.mockedGetAccountStatementOutputBoundary, times(1))
                .present(this.listArgumentCaptor.capture());

        assertThat(this.listArgumentCaptor.getValue())
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

        verify(this.mockedGetAccountStatementOutputBoundary, times(1))
                .present(this.getAccountStatementResponseArgumentCaptor.capture());

        assertEquals(number, this.getAccountStatementResponseArgumentCaptor.getValue().accountNumber());
        assertEquals(balance, this.getAccountStatementResponseArgumentCaptor.getValue().balance());
        assertEquals(1, this.getAccountStatementResponseArgumentCaptor.getValue().transactions().size());
        assertEquals(accountTransaction.format(), this.getAccountStatementResponseArgumentCaptor.getValue()
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
