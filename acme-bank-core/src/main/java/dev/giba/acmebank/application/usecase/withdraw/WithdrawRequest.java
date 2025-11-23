package dev.giba.acmebank.application.usecase.withdraw;

import java.math.BigDecimal;

public record WithdrawRequest(String accountNumber, BigDecimal amount) {
}
