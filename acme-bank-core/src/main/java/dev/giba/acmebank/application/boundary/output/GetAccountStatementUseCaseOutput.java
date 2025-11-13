package dev.giba.acmebank.application.boundary.output;

public interface GetAccountStatementUseCaseOutput {
    void execute(final Result<GetAccountStatementResponse> getAccountStatementResponse);
}