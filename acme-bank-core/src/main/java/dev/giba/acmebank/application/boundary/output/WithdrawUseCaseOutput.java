package dev.giba.acmebank.application.boundary.output;

import java.util.List;

public interface WithdrawUseCaseOutput {
    void present(final WithdrawResponse withdrawResponse);
    void present(final List<String> errors);
}