package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.DepositResponse;
import dev.giba.acmebank.application.boundary.output.DepositUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class DepositPresenter extends BasePresenter<DepositResponse> implements DepositUseCaseOutput {
    @Override
    public void execute(final Result<DepositResponse> depositResult) {
        Objects.requireNonNull(depositResult, "depositResult is required");
        super.execute(depositResult);
    }
}
