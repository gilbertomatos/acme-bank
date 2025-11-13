package dev.giba.acmebank.application.boundary.input;

import java.math.BigDecimal;

public record DepositRequest(String accountNumber, BigDecimal amount) {
}
