package dev.giba.acmebank.application.usecase.withdraw;

import java.math.BigDecimal;

public record WithdrawResponse(String accountNumber, BigDecimal balance) {
}
