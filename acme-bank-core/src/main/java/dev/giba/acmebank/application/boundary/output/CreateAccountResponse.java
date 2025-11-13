package dev.giba.acmebank.application.boundary.output;

import java.math.BigDecimal;

public record CreateAccountResponse(String accountNumber, BigDecimal balance) {
}
