package dev.giba.acmebank.application.usecase.withdraw;

import java.util.List;

public interface WithdrawUseCaseOutput {
    void present(final WithdrawResponse withdrawResponse);
    void present(final List<String> errors);
}