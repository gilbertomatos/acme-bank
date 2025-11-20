package dev.giba.acmebank.application.boundary.output;

import java.util.List;

public interface GetAccountStatementUseCaseOutput {
    void present(final GetAccountStatementResponse getAccountStatementResponse);
    void present(final List<String> errors);
}