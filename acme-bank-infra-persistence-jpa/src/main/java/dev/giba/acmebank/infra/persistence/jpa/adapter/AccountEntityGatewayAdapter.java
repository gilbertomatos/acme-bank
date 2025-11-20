package dev.giba.acmebank.infra.persistence.jpa.adapter;

import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.entity.AccountTransaction;
import dev.giba.acmebank.domain.entity.TransactionType;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.infra.persistence.jpa.entity.AccountEntity;
import dev.giba.acmebank.infra.persistence.jpa.entity.TransactionEntity;
import dev.giba.acmebank.infra.persistence.jpa.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class AccountEntityGatewayAdapter implements AccountEntityGateway {
    public static final String NUMBER_IS_REQUIRED = "number is required";
    public static final String ACCOUNT_IS_REQUIRED = "account is required";
    private final AccountRepository accountRepository;

    @Autowired
    public AccountEntityGatewayAdapter(final AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository is required");
    }

    @Override
    public Optional<Account> findByNumber(final String number) {
        Objects.requireNonNull(number, NUMBER_IS_REQUIRED);
        return this.accountRepository.findByNumber(number).map(this::toDomain);
    }

    @Override
    public Optional<Account> findByNumberForUpdate(final String number) {
        Objects.requireNonNull(number, NUMBER_IS_REQUIRED);
        return this.accountRepository.findByNumberForUpdate(number).map(this::toDomain);
    }

    @Override
    public void save(final Account account) {
        Objects.requireNonNull(account, ACCOUNT_IS_REQUIRED);
        this.accountRepository.save(this.toEntity(account));
    }

    private Account toDomain(final AccountEntity accountEntity) {
        Objects.requireNonNull(accountEntity, "accountEntity is required");
        var txs = accountEntity.getTransactions().stream()
                .map(t -> new AccountTransaction(TransactionType.valueOf(t.getType()), t.getAmount(),
                        t.getTimestamp(), t.getDescription()))
                .toList();
        return new Account(accountEntity.getId(), accountEntity.getNumber(), accountEntity.getBalance(), txs);
    }

    private AccountEntity toEntity(final Account account) {
        Objects.requireNonNull(account, ACCOUNT_IS_REQUIRED);
        var entity = new AccountEntity();
        entity.setId(account.id());
        entity.setNumber(account.number());
        entity.setBalance(account.balance());
        entity.setTransactions(account.transactions().stream()
                .map(t -> {
                    var te = new TransactionEntity();
                    te.setType(t.type().name());
                    te.setAmount(t.amount());
                    te.setTimestamp(t.timestamp());
                    te.setDescription(t.description());
                    return te;
                })
                .toList());
        return entity;
    }
}
