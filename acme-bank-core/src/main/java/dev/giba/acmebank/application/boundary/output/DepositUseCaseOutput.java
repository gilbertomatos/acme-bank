package dev.giba.acmebank.application.boundary.output;

import java.util.List;

public interface DepositUseCaseOutput {
    void present(final DepositResponse depositResponse);
    void present(final List<String> errors);
}