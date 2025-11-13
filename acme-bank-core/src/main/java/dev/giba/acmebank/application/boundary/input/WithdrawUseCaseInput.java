package dev.giba.acmebank.application.boundary.input;

public interface WithdrawUseCaseInput {
    void execute(final WithdrawRequest withdrawRequest);
}
