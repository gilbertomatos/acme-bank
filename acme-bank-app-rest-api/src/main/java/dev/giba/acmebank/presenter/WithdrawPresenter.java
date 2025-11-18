package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Component
@RequestScope
public class WithdrawPresenter extends BasePresenter<WithdrawResponse> implements WithdrawUseCaseOutput {
    protected WithdrawPresenter(final HttpServletResponse servletResponse,
                                           final MappingJackson2HttpMessageConverter jacksonConverter) {
        super(servletResponse, jacksonConverter);
    }

    @Override
    public void present(final Result<WithdrawResponse> withdrawResult) {
        Objects.requireNonNull(withdrawResult, "withdrawResult is required");
        super.present(withdrawResult);
    }
}
