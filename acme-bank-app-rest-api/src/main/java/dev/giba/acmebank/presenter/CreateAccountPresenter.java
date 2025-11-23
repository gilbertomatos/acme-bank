package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.usecase.createaccount.CreateAccountOutputBoundary;
import dev.giba.acmebank.application.usecase.createaccount.CreateAccountResponse;
import dev.giba.acmebank.view.ViewModel;
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
public class CreateAccountPresenter extends BasePresenter implements CreateAccountOutputBoundary {
    @Autowired
    protected CreateAccountPresenter(final HttpServletResponse httpServletResponse,
                                     final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter) {
        super(httpServletResponse, jacksonJsonHttpMessageConverter);
    }

    @Override
    public void present(final CreateAccountResponse createAccountResponse) {
        Objects.requireNonNull(createAccountResponse, "createAccountResponse is required");
        super.present(ViewModel.of(HttpStatus.OK, createAccountResponse));
    }

    @Override
    public void present(final List<String> errors) {
        Objects.requireNonNull(errors, "errors is required");
        super.present(ViewModel.of(HttpStatus.BAD_REQUEST, errors));
    }

}
