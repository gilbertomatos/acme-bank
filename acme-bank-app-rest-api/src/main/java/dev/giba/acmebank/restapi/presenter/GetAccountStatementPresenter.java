package dev.giba.acmebank.restapi.presenter;

import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementOutputBoundary;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementResponse;
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
public class GetAccountStatementPresenter extends BasePresenter implements GetAccountStatementOutputBoundary {
    @Autowired
    protected GetAccountStatementPresenter(final HttpServletResponse servletResponse,
                                           final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter) {
        super(servletResponse, jacksonJsonHttpMessageConverter);
    }

    @Override
    public void present(final GetAccountStatementResponse getAccountStatementResponse) {
        Objects.requireNonNull(getAccountStatementResponse, "getAccountStatementResponse is required");
        super.present(ViewModel.of(HttpStatus.OK, getAccountStatementResponse));
    }

    @Override
    public void present(final List<String> errors) {
        Objects.requireNonNull(errors, "errors is required");
        super.present(ViewModel.of(HttpStatus.NOT_FOUND, errors));
    }
}
