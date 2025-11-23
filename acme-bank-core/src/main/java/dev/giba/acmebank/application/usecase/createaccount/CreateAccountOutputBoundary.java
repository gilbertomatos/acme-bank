package dev.giba.acmebank.application.usecase.createaccount;

import java.util.List;

public interface CreateAccountOutputBoundary {
    void present(final CreateAccountResponse createAccountResponse);
    void present(final List<String> errors);
}