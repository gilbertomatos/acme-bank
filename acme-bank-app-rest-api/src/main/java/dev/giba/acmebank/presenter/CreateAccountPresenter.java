package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.CreateAccountResponse;
import dev.giba.acmebank.application.boundary.output.CreateAccountUseCaseOutput;
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
public class CreateAccountPresenter extends BasePresenter implements CreateAccountUseCaseOutput {
    protected CreateAccountPresenter(final HttpServletResponse httpServletResponse,
                                     final MappingJackson2HttpMessageConverter jacksonConverter) {
        super(httpServletResponse, jacksonConverter);
    }

    @Override
    public void present(final CreateAccountResponse createAccountResponse) {
        Objects.requireNonNull(createAccountResponse, "createAccountResponse is required");
        super.present(ViewModel.of(HttpStatus.OK, createAccountResponse));
    }

    public void present(final List<String> errors) {
        Objects.requireNonNull(errors, "errors is required");
        super.present(ViewModel.of(HttpStatus.BAD_REQUEST, errors));
    }

}
