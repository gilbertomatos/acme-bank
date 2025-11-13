package dev.giba.acmebank.infra.persistence.adapter;

import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.entity.Transaction;
import dev.giba.acmebank.domain.entity.TransactionType;
import dev.giba.acmebank.infra.persistence.entity.AccountEntity;
import dev.giba.acmebank.infra.persistence.entity.TransactionEntity;
import dev.giba.acmebank.infra.persistence.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountEntityGatewayAdapterTest {
    @Mock
    private AccountRepository mockedAccountRepository;
    @Captor
    private ArgumentCaptor<AccountEntity> accountEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private AccountEntityGatewayAdapter accountEntityGatewayAdapter;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedAccountRepository);
        this.accountEntityGatewayAdapter = new AccountEntityGatewayAdapter(this.mockedAccountRepository);
    }

    @Test
    @DisplayName("Should save correctly")
    void shouldSaveCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000001";
        var balance = BigDecimal.TEN;

        var transactionType = TransactionType.DEPOSIT;
        var amount = BigDecimal.TEN;
        var description = "Return of investments";

        var transaction = new Transaction(transactionType, amount, description);
        var account = new Account(id, number, balance, List.of(transaction));

        //When
        when(this.mockedAccountRepository.save(any(AccountEntity.class))).thenReturn(new AccountEntity());
        this.accountEntityGatewayAdapter.save(account);

        //Then
        verify(this.mockedAccountRepository, times(1))
                .save(this.accountEntityArgumentCaptor.capture());

        assertEquals(id, this.accountEntityArgumentCaptor.getValue().getId());
        assertEquals(number, this.accountEntityArgumentCaptor.getValue().getNumber());
        assertEquals(balance, this.accountEntityArgumentCaptor.getValue().getBalance());
        assertEquals(transactionType.name(),
                this.accountEntityArgumentCaptor.getValue().getTransactions().getFirst().getType());
        assertEquals(amount, this.accountEntityArgumentCaptor.getValue().getTransactions().getFirst().getAmount());
        assertEquals(description,
                this.accountEntityArgumentCaptor.getValue().getTransactions().getFirst().getDescription());

    }

    @Test
    @DisplayName("Should find by number correctly")
    void shouldFindByNumberCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000003";
        var balance = BigDecimal.TEN;

        var transactionType = TransactionType.DEPOSIT;
        var amount = BigDecimal.TEN;
        var description = "Loan repayment";
        var timestamp = LocalDateTime.now();

        var transactionEntity = new TransactionEntity();
        transactionEntity.setTimestamp(timestamp);
        transactionEntity.setType(transactionType.name());
        transactionEntity.setAmount(amount);
        transactionEntity.setDescription(description);

        var accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setNumber(number);
        accountEntity.setBalance(balance);
        accountEntity.setTransactions(List.of(transactionEntity));

        //When
        when(this.mockedAccountRepository.findByNumber(Mockito.any())).thenReturn(Optional.of(accountEntity));
        var resultAccount = this.accountEntityGatewayAdapter.findByNumber(number);

        //Then
        verify(this.mockedAccountRepository, times(1))
                .findByNumber(this.stringArgumentCaptor.capture());

        assertEquals(number, this.stringArgumentCaptor.getValue());
        assertThat(resultAccount).isPresent();
        assertEquals(id, resultAccount.get().id());
        assertEquals(number, resultAccount.get().number());
        assertEquals(balance, resultAccount.get().balance());
        assertEquals(timestamp, resultAccount.get().transactions().getFirst().timestamp());
        assertEquals(transactionType, resultAccount.get().transactions().getFirst().type());
        assertEquals(amount, resultAccount.get().transactions().getFirst().amount());
        assertEquals(description, resultAccount.get().transactions().getFirst().description());

    }

    @Test
    @DisplayName("Should find by number for update correctly")
    void shouldFindByNumberForUpdateCorrectly() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "000003";
        var balance = BigDecimal.TEN;

        var transactionType = TransactionType.DEPOSIT;
        var amount = BigDecimal.TEN;
        var description = "Won the lottery";
        var timestamp = LocalDateTime.now();

        var transactionEntity = new TransactionEntity();
        transactionEntity.setTimestamp(timestamp);
        transactionEntity.setType(transactionType.name());
        transactionEntity.setAmount(amount);
        transactionEntity.setDescription(description);

        var accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setNumber(number);
        accountEntity.setBalance(balance);
        accountEntity.setTransactions(List.of(transactionEntity));

        //When
        when(this.mockedAccountRepository.findByNumberForUpdate(Mockito.any()))
                .thenReturn(Optional.of(accountEntity));
        var resultAccount = this.accountEntityGatewayAdapter.findByNumberForUpdate(number);

        //Then
        verify(this.mockedAccountRepository, times(1))
                .findByNumberForUpdate(this.stringArgumentCaptor.capture());

        assertEquals(number, this.stringArgumentCaptor.getValue());
        assertThat(resultAccount).isPresent();
        assertEquals(id, resultAccount.get().id());
        assertEquals(number, resultAccount.get().number());
        assertEquals(balance, resultAccount.get().balance());
        assertEquals(timestamp, resultAccount.get().transactions().getFirst().timestamp());
        assertEquals(transactionType, resultAccount.get().transactions().getFirst().type());
        assertEquals(amount, resultAccount.get().transactions().getFirst().amount());
        assertEquals(description, resultAccount.get().transactions().getFirst().description());

    }
}
