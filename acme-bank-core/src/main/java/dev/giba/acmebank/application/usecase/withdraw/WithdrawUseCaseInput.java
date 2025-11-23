package dev.giba.acmebank.application.usecase.withdraw;

public interface WithdrawUseCaseInput {
    void execute(final WithdrawRequest withdrawRequest);
}
