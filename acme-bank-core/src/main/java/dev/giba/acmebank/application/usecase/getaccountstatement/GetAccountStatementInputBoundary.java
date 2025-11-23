package dev.giba.acmebank.application.usecase.getaccountstatement;

public interface GetAccountStatementInputBoundary {
    void execute(final GetAccountStatementRequest getAccountStatementRequest);
}