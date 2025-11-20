package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.GetAccountStatementResponse;
import dev.giba.acmebank.application.boundary.output.GetAccountStatementUseCaseOutput;
import dev.giba.acmebank.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Objects;

@Component
@RequestScope
public class GetAccountStatementPresenter extends BasePresenter implements GetAccountStatementUseCaseOutput {
    protected GetAccountStatementPresenter(final HttpServletResponse servletResponse,
                               final MappingJackson2HttpMessageConverter jacksonConverter) {
        super(servletResponse, jacksonConverter);
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
