package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.DepositResponse;
import dev.giba.acmebank.application.boundary.output.DepositUseCaseOutput;
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
public class DepositPresenter extends BasePresenter implements DepositUseCaseOutput {
    protected DepositPresenter(final HttpServletResponse servletResponse,
                                     final MappingJackson2HttpMessageConverter jacksonConverter) {
        super(servletResponse, jacksonConverter);
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
