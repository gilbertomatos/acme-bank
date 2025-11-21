package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.GetAccountStatementResponse;
import dev.giba.acmebank.view.ViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.server.DelegatingServerHttpResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccountStatementPresenterTest {
    @Mock
    private HttpServletResponse mockedHttpServletResponse;
    @Mock
    private JacksonJsonHttpMessageConverter mockedJacksonJsonHttpMessageConverter;
    @Captor
    private ArgumentCaptor<ViewModel> viewModelArgumentCaptor;

    private GetAccountStatementPresenter getAccountStatementPresenter;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedHttpServletResponse);
        reset(this.mockedJacksonJsonHttpMessageConverter);

        this.getAccountStatementPresenter = new GetAccountStatementPresenter(this.mockedHttpServletResponse,
                this.mockedJacksonJsonHttpMessageConverter);
    }

    @Test
    @DisplayName("Should present on success correctly")
    void shouldPresentOnSuccessCorrectly() throws IOException {
        //Given
        var number = "043321";
        var balance = BigDecimal.TEN;
        var getAccountStatementResponse = new GetAccountStatementResponse(number, balance, Collections.emptyList());

        doNothing().when(this.mockedJacksonJsonHttpMessageConverter).write(any(ViewModel.class),
                eq(MediaType.APPLICATION_JSON), any(DelegatingServerHttpResponse.class));

        //When
        this.getAccountStatementPresenter.present(getAccountStatementResponse);

        //Then
        verify(this.mockedJacksonJsonHttpMessageConverter, times(1))
                .write(this.viewModelArgumentCaptor.capture(),
                        eq(MediaType.APPLICATION_JSON), any(DelegatingServerHttpResponse.class));

        var viewModel = this.viewModelArgumentCaptor.getValue();

        assertNotNull(viewModel);
        assertEquals(HttpStatus.OK, viewModel.status());
        assertNotNull(viewModel.data());
        assertNull(viewModel.errors());
    }

    @Test
    @DisplayName("Should present on error correctly")
    void shouldPresentOnErrorCorrectly() throws IOException {
        //Given
        var result = List.of("Error 3");

        doNothing().when(this.mockedJacksonJsonHttpMessageConverter).write(any(ViewModel.class),
                eq(MediaType.APPLICATION_JSON), any(DelegatingServerHttpResponse.class));

        //When
        this.getAccountStatementPresenter.present(result);

        //Then
        verify(this.mockedJacksonJsonHttpMessageConverter, times(1))
                .write(this.viewModelArgumentCaptor.capture(),
                        eq(MediaType.APPLICATION_JSON), any(DelegatingServerHttpResponse.class));

        var viewModel = this.viewModelArgumentCaptor.getValue();

        assertNotNull(viewModel);
        assertEquals(HttpStatus.NOT_FOUND, viewModel.status());
        assertNull(viewModel.data());
        assertNotNull(viewModel.errors());
    }
}
