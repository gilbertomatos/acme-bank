package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class WithdrawPresenter extends BasePresenter<WithdrawResponse> implements WithdrawUseCaseOutput {
    @Override
    public void present(final Result<WithdrawResponse> withdrawResult) {
        Objects.requireNonNull(withdrawResult, "withdrawResult is required");
        super.present(withdrawResult);
    }
}
