package dev.giba.acmebank.application.boundary.output;

import java.math.BigDecimal;
import java.util.List;

public record GetAccountStatementResponse(String accountNumber, BigDecimal balance, List<String> transactions) {
}