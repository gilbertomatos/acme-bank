package dev.giba.acmebank.restapi.presenter;

import dev.giba.acmebank.application.usecase.deposit.DepositOutputBoundary;
import dev.giba.acmebank.application.usecase.deposit.DepositResponse;
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
public class DepositPresenter extends BasePresenter implements DepositOutputBoundary {
    @Autowired
    protected DepositPresenter(final HttpServletResponse servletResponse,
                               final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter) {
        super(servletResponse, jacksonJsonHttpMessageConverter);
    }

    @Override
    public void present(final DepositResponse depositResponse) {
        Objects.requireNonNull(depositResponse, "depositResponse is required");
        super.present(ViewModel.of(HttpStatus.OK, depositResponse));
    }

    @Override
    public void present(final List<String> errors) {
        Objects.requireNonNull(errors, "errors is required");
        super.present(ViewModel.of(HttpStatus.BAD_REQUEST, errors));
    }

}
