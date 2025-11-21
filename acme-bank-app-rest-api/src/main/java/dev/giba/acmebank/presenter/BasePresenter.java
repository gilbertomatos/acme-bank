package dev.giba.acmebank.presenter;

import dev.giba.acmebank.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.util.Objects;

public abstract class BasePresenter {
    private final HttpServletResponse httpServletResponse;
    private final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter;

    protected BasePresenter(final HttpServletResponse httpServletResponse,
                            final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter) {
        Objects.requireNonNull(httpServletResponse, "httpServletResponse is required");
        Objects.requireNonNull(jacksonJsonHttpMessageConverter, "jacksonJsonHttpMessageConverter is required");

        this.httpServletResponse = httpServletResponse;
        this.jacksonJsonHttpMessageConverter = jacksonJsonHttpMessageConverter;
    }

    protected void present(final ViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel is required");

        var httpOutputMessage = new DelegatingServerHttpResponse(
                new ServletServerHttpResponse(this.httpServletResponse));

        httpOutputMessage.setStatusCode(viewModel.status());

        try {
            this.jacksonJsonHttpMessageConverter.write(viewModel, MediaType.APPLICATION_JSON, httpOutputMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
