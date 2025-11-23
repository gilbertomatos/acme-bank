package dev.giba.acmebank.application.usecase.getaccountstatement;

import dev.giba.acmebank.domain.entity.AccountTransaction;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.ReadOnlyTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetAccountStatementUseCase implements GetAccountStatementInputBoundary {
    private final GetAccountStatementOutputBoundary getAccountStatementOutputBoundary;
    private final AccountEntityGateway accountEntityGateway;
    private final ReadOnlyTransaction readOnlyTransaction;

    public GetAccountStatementUseCase(final GetAccountStatementOutputBoundary getAccountStatementOutputBoundary,
                                      final AccountEntityGateway accountEntityGateway,
                                      final ReadOnlyTransaction readOnlyTransaction) {
        this.getAccountStatementOutputBoundary = Objects.requireNonNull(getAccountStatementOutputBoundary,
                "getAccountStatementUseCaseOutput is required");
        this.accountEntityGateway = Objects.requireNonNull(accountEntityGateway,
                "accountEntityGateway is required");
        this.readOnlyTransaction = Objects.requireNonNull(readOnlyTransaction,
                "readOnlyTransaction is required");
    }

    @Override
    public void execute(final GetAccountStatementRequest getAccountStatementRequest) {
        Objects.requireNonNull(getAccountStatementRequest, "getAccountStatementRequest is required");

        var requestModelErrors = this.validateRequestModel(getAccountStatementRequest);

        if (!requestModelErrors.isEmpty()) {
            this.getAccountStatementOutputBoundary.present(requestModelErrors);
            return;
        }

        this.readOnlyTransaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumber(getAccountStatementRequest.accountNumber());
            optAccount.ifPresentOrElse( account -> this.getAccountStatementOutputBoundary.present(
                    new GetAccountStatementResponse(account.number(), account.balance(),
                            account.transactions().stream()
                                    .map(AccountTransaction::format).toList()
                    )
            ), () -> this.getAccountStatementOutputBoundary.present(List.of("Account not found")));

        });
    }

    private List<String> validateRequestModel(final GetAccountStatementRequest getAccountStatementRequest) {
        var errors = new ArrayList<String>(1);

        if (Objects.isNull(getAccountStatementRequest.accountNumber())
                || getAccountStatementRequest.accountNumber().isEmpty()) {
            errors.add("Account number is mandatory");
        }

        return errors;
    }
}
