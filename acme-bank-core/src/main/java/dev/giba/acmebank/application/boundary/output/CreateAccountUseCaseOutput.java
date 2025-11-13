package dev.giba.acmebank.application.boundary.output;

public interface CreateAccountUseCaseOutput {
    void execute(final Result<CreateAccountResponse> createAccountResult);
}