package dev.giba.acmebank.application.boundary.output;

import java.math.BigDecimal;

public record DepositResponse(String accountNumber, BigDecimal balance) {
}
