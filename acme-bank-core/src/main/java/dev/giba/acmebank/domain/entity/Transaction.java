package dev.giba.acmebank.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(TransactionType type,
                          BigDecimal amount,
                          LocalDateTime timestamp,
                          String description) {
    public Transaction(TransactionType type, BigDecimal amount, String description) {
        this(type, amount, LocalDateTime.now(), description);
    }
}
