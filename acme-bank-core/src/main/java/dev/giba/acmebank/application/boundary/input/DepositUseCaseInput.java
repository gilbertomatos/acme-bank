package dev.giba.acmebank.application.boundary.input;

public interface DepositUseCaseInput {
    void execute(final DepositRequest depositRequest);
}
