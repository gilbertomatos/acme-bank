package dev.giba.acmebank.application.usecase.getaccountstatement;

import java.math.BigDecimal;
import java.util.List;

public record GetAccountStatementResponse(String accountNumber, BigDecimal balance, List<String> transactions) {
}