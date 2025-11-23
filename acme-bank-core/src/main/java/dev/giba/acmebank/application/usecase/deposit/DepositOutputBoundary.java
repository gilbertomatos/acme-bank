package dev.giba.acmebank.application.usecase.deposit;

import java.util.List;

public interface DepositOutputBoundary {
    void present(final DepositResponse depositResponse);
    void present(final List<String> errors);
}