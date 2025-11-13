package dev.giba.acmebank.application.boundary.input;

public interface GetAccountStatementUseCaseInput {
    void execute(final GetAccountStatementRequest getAccountStatementRequest);
}