package dev.giba.acmebank.application.boundary.output;

public interface DepositUseCaseOutput {
    void execute(final Result<DepositResponse> depositResult);
}