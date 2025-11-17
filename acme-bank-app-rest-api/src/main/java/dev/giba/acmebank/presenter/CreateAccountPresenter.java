package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class CreateAccountPresenter extends BasePresenter<CreateAccountResponse> implements CreateAccountUseCaseOutput {
    @Override
    public void present(final Result<CreateAccountResponse> createAccountResult) {
        Objects.requireNonNull(createAccountResult, "createAccountResult is required");
        super.present(createAccountResult);
    }
}
