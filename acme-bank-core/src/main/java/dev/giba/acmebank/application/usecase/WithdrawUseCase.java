package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.WithdrawRequest;
import dev.giba.acmebank.application.boundary.input.WithdrawUseCaseInput;
import dev.giba.acmebank.application.boundary.output.*;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WithdrawUseCase implements WithdrawUseCaseInput {
    private final WithdrawUseCaseOutput withdrawUseCaseOutput;
    private final AccountEntityGateway accountEntityGateway;
    private final Transaction transaction;


    public WithdrawUseCase(final WithdrawUseCaseOutput withdrawUseCaseOutput,
                           final AccountEntityGateway accountEntityGateway,
                           final Transaction transaction) {
        this.withdrawUseCaseOutput = Objects.requireNonNull(withdrawUseCaseOutput,
                "withdrawUseCaseOutput is required");
        this.accountEntityGateway = Objects.requireNonNull(accountEntityGateway,
                "accountEntityGateway is required");
        this.transaction = Objects.requireNonNull(transaction, "transaction is required");
    }

    @Override
    public void execute(final WithdrawRequest withdrawRequest) {
        Objects.requireNonNull(withdrawRequest, "withdrawRequest is required");

        var requestModelErrors = this.validateRequestModel(withdrawRequest);

        if (!requestModelErrors.isEmpty()) {
            this.withdrawUseCaseOutput.execute(Result.failure(requestModelErrors));
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumberForUpdate(withdrawRequest.accountNumber());

            optAccount.ifPresentOrElse( account -> {
                try {
                    var updatedAccount = account.withdraw(withdrawRequest.amount());
                    this.accountEntityGateway.save(updatedAccount);
                    this.withdrawUseCaseOutput.execute(Result.success(new WithdrawResponse(updatedAccount.number(),
                            updatedAccount.balance())));
                } catch (IllegalArgumentException e) {
                    this.withdrawUseCaseOutput.execute(Result.failure(List.of(e.getMessage())));
                }

            }, () -> this.withdrawUseCaseOutput.execute(Result.failure(List.of("Account not found"))));
         
        });

    }

    private List<String> validateRequestModel(final WithdrawRequest withdrawRequest) {
        var errors = new ArrayList<String>(2);

        if (Objects.isNull(withdrawRequest.accountNumber()) || withdrawRequest.accountNumber().isEmpty()) {
            errors.add("Account number is mandatory");
        }

        if (Objects.isNull(withdrawRequest.amount()) || withdrawRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("A valid amount is mandatory");
        }

        return errors;
    }
}
