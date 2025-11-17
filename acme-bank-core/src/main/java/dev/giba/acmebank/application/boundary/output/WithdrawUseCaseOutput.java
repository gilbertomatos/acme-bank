package dev.giba.acmebank.application.boundary.output;

public interface WithdrawUseCaseOutput {
    void present(final Result<WithdrawResponse> withdrawResult);
}