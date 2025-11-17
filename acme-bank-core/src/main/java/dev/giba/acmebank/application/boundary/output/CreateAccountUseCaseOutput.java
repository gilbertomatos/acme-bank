package dev.giba.acmebank.application.boundary.output;

public interface CreateAccountUseCaseOutput {
    void present(final Result<CreateAccountResponse> createAccountResult);
}