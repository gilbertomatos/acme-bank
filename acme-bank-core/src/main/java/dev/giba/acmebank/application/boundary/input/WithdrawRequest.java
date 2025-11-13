package dev.giba.acmebank.application.boundary.input;

import java.math.BigDecimal;

public record WithdrawRequest(String accountNumber, BigDecimal amount) {
}
