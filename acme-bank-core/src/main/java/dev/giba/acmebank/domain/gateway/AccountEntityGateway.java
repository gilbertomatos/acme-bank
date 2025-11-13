package dev.giba.acmebank.domain.gateway;

import dev.giba.acmebank.domain.entity.Account;

import java.util.Optional;

public interface AccountEntityGateway {
    Optional<Account> findByNumber(final String number);
    Optional<Account> findByNumberForUpdate(final String number);
    void save(final Account account);
}
