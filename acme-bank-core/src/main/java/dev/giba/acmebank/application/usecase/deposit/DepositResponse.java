package dev.giba.acmebank.application.usecase.deposit;

import java.math.BigDecimal;

public record DepositResponse(String accountNumber, BigDecimal balance) {
}
