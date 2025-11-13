package dev.giba.acmebank.infra.persistence.repository;

import dev.giba.acmebank.domain.entity.TransactionType;
import dev.giba.acmebank.infra.persistence.entity.AccountEntity;
import dev.giba.acmebank.infra.persistence.entity.TransactionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AccountRepositoryIntegrationTest {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountRepositoryIntegrationTest(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    @DisplayName("Should find by number correctly")
    @Transactional
    void shouldFindByNumberCorrectly() {
        //Given
        var transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(BigDecimal.TEN);
        transactionEntity.setType(TransactionType.DEPOSIT.name());
        transactionEntity.setDescription("Return on investment");
        transactionEntity.setTimestamp(LocalDateTime.now());

        var accountEntity = new AccountEntity();
        accountEntity.setNumber("0001");
        accountEntity.setBalance(BigDecimal.TEN);
        accountEntity.setTransactions(List.of(transactionEntity));
        this.accountRepository.save(accountEntity);

        //When
        var optionalAccountEntity = this.accountRepository.findByNumber(accountEntity.getNumber());

        //Then
        assertThat(optionalAccountEntity).isPresent();
    }

    @Test
    @DisplayName("Should find by number for update correctly")
    @Transactional
    void shouldFindByNumberForUpdateCorrectly() {
        //Given
        var transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(BigDecimal.TEN);
        transactionEntity.setType(TransactionType.DEPOSIT.name());
        transactionEntity.setDescription("Salary payment");
        transactionEntity.setTimestamp(LocalDateTime.now());

        var accountEntity = new AccountEntity();
        accountEntity.setNumber("0002");
        accountEntity.setBalance(BigDecimal.TEN);
        accountEntity.setTransactions(List.of(transactionEntity));
        this.accountRepository.save(accountEntity);

        //When
        var optionalAccountEntity =
                this.accountRepository.findByNumberForUpdate(accountEntity.getNumber());

        //Then
        assertThat(optionalAccountEntity).isPresent();
    }
}
