package dev.giba.acmebank.domain.entity;

import java.math.BigDecimal;
import java.util.List;

public record Account(
        Long id,
        String number,
        BigDecimal balance,
        List<Transaction> transactions
) {

    public Account addTransaction(final TransactionType type, final BigDecimal amount, final String description) {
        var updatedTransactions = new java.util.ArrayList<>(transactions);
        updatedTransactions.add(new Transaction(type, amount, description));
        return new Account(id, number, balance, updatedTransactions);
    }

    public Account deposit(final BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("A valid amount is mandatory");
        var updatedTransactions = new java.util.ArrayList<>(transactions);
        updatedTransactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                "Deposit successfully completed"));
        return new Account(id, number, balance.add(amount), updatedTransactions);
    }

    public Account withdraw(final BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("A valid amount is mandatory");
        if (balance.compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient balance");
        var updatedTransactions = new java.util.ArrayList<>(transactions);
        updatedTransactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                "Withdraw successfully completed"));
        return new Account(id, number, balance.subtract(amount), updatedTransactions);
    }
}
