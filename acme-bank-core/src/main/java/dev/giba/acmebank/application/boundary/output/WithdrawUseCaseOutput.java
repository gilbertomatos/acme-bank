package dev.giba.acmebank.application.boundary.output;

public interface WithdrawUseCaseOutput {
    void execute(final Result<WithdrawResponse> withdrawResult);
}