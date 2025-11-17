package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import dev.giba.acmebank.application.boundary.output.CreateAccountResponse;
import dev.giba.acmebank.application.boundary.output.CreateAccountUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.Result;
import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CreateAccountUseCase implements CreateAccountUseCaseInput {
    private final CreateAccountUseCaseOutput createAccountUseCaseOutput;
    private final AccountEntityGateway accountEntityGateway;
    private final Transaction transaction;

    public CreateAccountUseCase(final CreateAccountUseCaseOutput createAccountUseCaseOutput,
                          final AccountEntityGateway accountEntityGateway,
                          final Transaction transaction) {
        this.createAccountUseCaseOutput = Objects.requireNonNull(createAccountUseCaseOutput,
                "createAccountUseCaseOutput is required");
        this.accountEntityGateway = Objects.requireNonNull(accountEntityGateway,
                "accountEntityGateway is required");
        this.transaction = Objects.requireNonNull(transaction,
                "transaction is required");
    }

    @Override
    public void execute(final CreateAccountRequest createAccountRequest) {
        Objects.requireNonNull(createAccountRequest, "createAccountRequest is required");

        var requestModelErrors = this.validateRequestModel(createAccountRequest);

        if (!requestModelErrors.isEmpty()) {
            this.createAccountUseCaseOutput.present(Result.failure(requestModelErrors));
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumber(createAccountRequest.accountNumber());

            optAccount.ifPresentOrElse( account ->
                    this.createAccountUseCaseOutput.present(
                            Result.failure(List.of("Account already exists"))), () -> {

                var newAccount = new Account(null, createAccountRequest.accountNumber(), BigDecimal.ZERO,
                        Collections.emptyList());
                this.accountEntityGateway.save(newAccount);
                this.createAccountUseCaseOutput.present(Result.success(new CreateAccountResponse(newAccount.number(),
                        newAccount.balance())));
            });

        });

    }

    private List<String> validateRequestModel(final CreateAccountRequest createAccountRequest) {
        var errors = new ArrayList<String>(1);

        if (Objects.isNull(createAccountRequest.accountNumber()) || createAccountRequest.accountNumber().isEmpty()) {
            errors.add("Account number is mandatory");
        }

        return errors;
    }
}
