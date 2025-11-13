package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class GetAccountStatementPresenter extends BasePresenter<GetAccountStatementResponse>
        implements GetAccountStatementUseCaseOutput {
    @Override
    public void execute(final Result<GetAccountStatementResponse> getAccountStatementResult) {
        Objects.requireNonNull(getAccountStatementResult, "getAccountStatementResult is required");
        super.execute(getAccountStatementResult);
    }
}
