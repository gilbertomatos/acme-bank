package dev.giba.acmebank.application.usecase.createaccount;

import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CreateAccountUseCase implements CreateAccountInputBoundary {
    private final CreateAccountOutputBoundary createAccountOutputBoundary;
    private final AccountEntityGateway accountEntityGateway;
    private final Transaction transaction;

    public CreateAccountUseCase(final CreateAccountOutputBoundary createAccountOutputBoundary,
                          final AccountEntityGateway accountEntityGateway,
                          final Transaction transaction) {
        this.createAccountOutputBoundary = Objects.requireNonNull(createAccountOutputBoundary,
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
            this.createAccountOutputBoundary.present(requestModelErrors);
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumber(createAccountRequest.accountNumber());

            optAccount.ifPresentOrElse( account ->
                    this.createAccountOutputBoundary.present(
                            List.of("Account already exists")), () -> {

                var newAccount = new Account(null, createAccountRequest.accountNumber(), BigDecimal.ZERO,
                        Collections.emptyList());
                this.accountEntityGateway.save(newAccount);
                this.createAccountOutputBoundary.present(new CreateAccountResponse(newAccount.number(),
                        newAccount.balance()));
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
