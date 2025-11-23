package dev.giba.acmebank.application.usecase.withdraw;

import dev.giba.acmebank.domain.entity.Account;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.Transaction;

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
            this.withdrawUseCaseOutput.present(requestModelErrors);
            return;
        }

        this.transaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumberForUpdate(withdrawRequest.accountNumber());

            optAccount.ifPresentOrElse( account -> {

                Account updatedAccount;
                try {
                    updatedAccount = account.withdraw(withdrawRequest.amount());
                } catch (IllegalArgumentException e) {
                    this.withdrawUseCaseOutput.present(List.of(e.getMessage()));
                    return;
                }

                this.accountEntityGateway.save(updatedAccount);
                this.withdrawUseCaseOutput.present((new WithdrawResponse(updatedAccount.number(),
                        updatedAccount.balance())));

            }, () -> this.withdrawUseCaseOutput.present(List.of("Account not found")));
         
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
