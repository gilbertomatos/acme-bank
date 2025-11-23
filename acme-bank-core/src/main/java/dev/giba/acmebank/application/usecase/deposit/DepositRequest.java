package dev.giba.acmebank.application.usecase.deposit;

import java.math.BigDecimal;

public record DepositRequest(String accountNumber, BigDecimal amount) {
}
