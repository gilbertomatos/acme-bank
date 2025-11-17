package dev.giba.acmebank.application.boundary.output;

public interface DepositUseCaseOutput {
    void present(final Result<DepositResponse> depositResult);
}