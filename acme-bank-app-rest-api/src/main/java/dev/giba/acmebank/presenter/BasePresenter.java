package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.Result;
import dev.giba.acmebank.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.util.Objects;

public abstract class BasePresenter<T> {
    private final HttpServletResponse httpServletResponse;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    protected BasePresenter(final HttpServletResponse httpServletResponse,
                            final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.httpServletResponse = httpServletResponse;
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    protected void present(final Result<T> result){
        Objects.requireNonNull(result, "result is required");

        try (var httpOutputMessage = new ServletServerHttpResponse(this.httpServletResponse)) {
            if (result.isSuccess()) {
                this.write(httpOutputMessage, HttpStatus.OK,
                        new ViewModel(HttpStatus.OK, result.value(), null));
            } else {
                this.write(httpOutputMessage, HttpStatus.BAD_REQUEST,
                        new ViewModel(HttpStatus.BAD_REQUEST, null, result.errors()));
            }
        }
    }

    private void write(final ServletServerHttpResponse httpOutputMessage, final HttpStatusCode httpStatusCode,
                       final ViewModel viewModel) {
        httpOutputMessage.setStatusCode(httpStatusCode);
        try {
            this.mappingJackson2HttpMessageConverter.write(viewModel, MediaType.APPLICATION_JSON, httpOutputMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
