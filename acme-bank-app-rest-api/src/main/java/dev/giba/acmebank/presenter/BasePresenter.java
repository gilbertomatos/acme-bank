package dev.giba.acmebank.presenter;

import dev.giba.acmebank.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;

public abstract class BasePresenter {
    private final HttpServletResponse httpServletResponse;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    protected BasePresenter(final HttpServletResponse httpServletResponse,
                            final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.httpServletResponse = httpServletResponse;
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    protected void present(final ViewModel viewModel) {
        var httpOutputMessage = new DelegatingServerHttpResponse(
                new ServletServerHttpResponse(this.httpServletResponse));

        httpOutputMessage.setStatusCode(viewModel.status());

        try {
            this.mappingJackson2HttpMessageConverter.write(viewModel, MediaType.APPLICATION_JSON, httpOutputMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
