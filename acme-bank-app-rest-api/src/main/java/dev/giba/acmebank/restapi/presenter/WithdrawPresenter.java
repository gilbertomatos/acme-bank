package dev.giba.acmebank.restapi.presenter;

import dev.giba.acmebank.application.usecase.withdraw.WithdrawResponse;
import dev.giba.acmebank.application.usecase.withdraw.WithdrawUseCaseOutput;
import dev.giba.acmebank.restapi.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Objects;

@Component
@RequestScope
public class WithdrawPresenter extends BasePresenter implements WithdrawUseCaseOutput {
    @Autowired
    protected WithdrawPresenter(final HttpServletResponse servletResponse,
                                final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter) {
        super(servletResponse, jacksonJsonHttpMessageConverter);
    }

    @Override
    public void present(final WithdrawResponse withdrawResponse) {
        Objects.requireNonNull(withdrawResponse, "withdrawResponse is required");
        super.present(ViewModel.of(HttpStatus.OK, withdrawResponse));
    }

    @Override
    public void present(final List<String> errors) {
        Objects.requireNonNull(errors, "errors is required");
        super.present(ViewModel.of(HttpStatus.BAD_REQUEST, errors));
    }
}
