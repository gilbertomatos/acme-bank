package dev.giba.acmebank.application.boundary.output;

public interface GetAccountStatementUseCaseOutput {
    void present(final Result<GetAccountStatementResponse> getAccountStatementResponse);
}