package dev.giba.acmebank.application.boundary.output;

import java.math.BigDecimal;

public record WithdrawResponse(String accountNumber, BigDecimal balance) {
}
