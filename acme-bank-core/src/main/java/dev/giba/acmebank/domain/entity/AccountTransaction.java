package dev.giba.acmebank.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AccountTransaction(TransactionType type,
                                 BigDecimal amount,
                                 LocalDateTime timestamp,
                                 String description) {
    public AccountTransaction(TransactionType type, BigDecimal amount, String description) {
        this(type, amount, LocalDateTime.now(), description);
    }

    public String getLine() {
        var formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return String.format("%s - %s -> $%s", timestamp.format(formatter), description, amount);
    }

}
