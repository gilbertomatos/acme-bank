package dev.giba.acmebank.application.usecase.createaccount;

public interface CreateAccountInputBoundary {
    void execute(final CreateAccountRequest createAccountRequest);
}