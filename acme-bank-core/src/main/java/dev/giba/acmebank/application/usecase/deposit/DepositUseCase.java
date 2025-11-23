package dev.giba.acmebank.application.usecase.deposit;

import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DepositUseCase implements DepositInputBoundary {
    private final DepositOutputBoundary depositOutputBoundary;
    private final AccountEntityGateway accountEntityGateway;
    private final Transaction transaction;


    public DepositUseCase(final DepositOutputBoundary depositOutputBoundary,
                          final AccountEntityGateway accountEntityGateway,
                          final Transaction transaction) {
        this.depositOutputBoundary = Objects.requireNonNull(depositOutputBoundary,
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
            this.depositOutputBoundary.present(requestModelErrors);
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumberForUpdate(depositRequest.accountNumber());

            optAccount.ifPresentOrElse( account -> {

                var updatedAccount = account.deposit(depositRequest.amount());
                this.accountEntityGateway.save(updatedAccount);
                this.depositOutputBoundary.present(new DepositResponse(updatedAccount.number(),
                        updatedAccount.balance()));

            }, () -> this.depositOutputBoundary.present(List.of("Account not found")));
         
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
