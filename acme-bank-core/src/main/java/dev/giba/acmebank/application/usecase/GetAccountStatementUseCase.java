package dev.giba.acmebank.application.usecase;

import dev.giba.acmebank.application.boundary.input.GetAccountStatementRequest;
import dev.giba.acmebank.application.boundary.input.GetAccountStatementUseCaseInput;
import dev.giba.acmebank.application.boundary.output.*;
import dev.giba.acmebank.domain.entity.AccountTransaction;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetAccountStatementUseCase implements GetAccountStatementUseCaseInput {
    private final GetAccountStatementUseCaseOutput getAccountStatementUseCaseOutput;
    private final AccountEntityGateway accountEntityGateway;
    private final ReadOnlyTransaction readOnlyTransaction;

    public GetAccountStatementUseCase(final GetAccountStatementUseCaseOutput getAccountStatementUseCaseOutput,
                                      final AccountEntityGateway accountEntityGateway,
                                      final ReadOnlyTransaction readOnlyTransaction) {
        this.getAccountStatementUseCaseOutput = Objects.requireNonNull(getAccountStatementUseCaseOutput,
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
            this.getAccountStatementUseCaseOutput.execute(Result.failure(requestModelErrors));
            return;
        }

        this.readOnlyTransaction.execute(() -> {
            var optAccount = this.accountEntityGateway.findByNumber(getAccountStatementRequest.accountNumber());
            optAccount.ifPresentOrElse( account -> this.getAccountStatementUseCaseOutput.execute(
                    Result.success(new GetAccountStatementResponse(account.number(), account.balance(),
                            account.transactions().stream()
                                    .map(AccountTransaction::format).collect(Collectors.toList())
                    )
            )), () -> this.getAccountStatementUseCaseOutput.execute(Result.failure(List.of("Account not found"))));

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
