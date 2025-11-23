package dev.giba.acmebank.application.usecase.deposit;

public interface DepositInputBoundary {
    void execute(final DepositRequest depositRequest);
}
