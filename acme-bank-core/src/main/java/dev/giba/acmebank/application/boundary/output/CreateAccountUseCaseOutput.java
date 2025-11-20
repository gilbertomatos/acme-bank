package dev.giba.acmebank.application.boundary.output;

import java.util.List;

public interface CreateAccountUseCaseOutput {
    void present(final CreateAccountResponse createAccountResponse);
    void present(final List<String> errors);
}