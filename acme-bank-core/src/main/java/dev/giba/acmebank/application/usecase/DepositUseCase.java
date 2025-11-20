package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.DepositRequest;
import dev.giba.acmebank.application.boundary.input.DepositUseCaseInput;
import dev.giba.acmebank.application.boundary.output.DepositResponse;
import dev.giba.acmebank.application.boundary.output.DepositUseCaseOutput;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DepositUseCase implements DepositUseCaseInput {
    private final DepositUseCaseOutput depositUseCaseOutput;
    private final AccountEntityGateway accountEntityGateway;
    private final Transaction transaction;


    public DepositUseCase(final DepositUseCaseOutput depositUseCaseOutput,
                          final AccountEntityGateway accountEntityGateway,
                          final Transaction transaction) {
        this.depositUseCaseOutput = Objects.requireNonNull(depositUseCaseOutput,
                "depositUseCaseOutput is required");
        this.accountEntityGateway = Objects.requireNonNull(accountEntityGateway,
                "accountEntityGateway is required");
        this.transaction = Objects.requireNonNull(transaction, "transaction is required");
    }

    @Override
    public void execute(final DepositRequest depositRequest) {
        Objects.requireNonNull(depositRequest, "depositRequest is required");

        var requestModelErrors = this.validateRequestModel(depositRequest);

        if (!requestModelErrors.isEmpty()) {
            this.depositUseCaseOutput.present(requestModelErrors);
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumberForUpdate(depositRequest.accountNumber());

            optAccount.ifPresentOrElse( account -> {

                var updatedAccount = account.deposit(depositRequest.amount());
                this.accountEntityGateway.save(updatedAccount);
                this.depositUseCaseOutput.present(new DepositResponse(updatedAccount.number(),
                        updatedAccount.balance()));

            }, () -> this.depositUseCaseOutput.present(List.of("Account not found")));
         
        });

    }

    private List<String> validateRequestModel(final DepositRequest depositRequest) {
        var errors = new ArrayList<String>(2);

        if (Objects.isNull(depositRequest.accountNumber()) || depositRequest.accountNumber().isEmpty()) {
            errors.add("Account number is mandatory");
        }

        if (Objects.isNull(depositRequest.amount()) || depositRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("A valid amount is mandatory");
        }

        return errors;
    }
}
