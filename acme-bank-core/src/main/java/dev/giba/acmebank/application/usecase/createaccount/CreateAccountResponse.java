package dev.giba.acmebank.application.usecase.createaccount;

import java.math.BigDecimal;

public record CreateAccountResponse(String accountNumber, BigDecimal balance) {
}
