package dev.giba.acmebank.application.usecase.getaccountstatement;

import java.util.List;

public interface GetAccountStatementOutputBoundary {
    void present(final GetAccountStatementResponse getAccountStatementResponse);
    void present(final List<String> errors);
}