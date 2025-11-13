package dev.giba.acmebank.application.boundary.input;

public interface CreateAccountUseCaseInput {
    void execute(final CreateAccountRequest createAccountRequest);
}