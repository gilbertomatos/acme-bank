package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.CreateAccountResponse;
import dev.giba.acmebank.application.boundary.output.Result;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateAccountPresenterTest {
    @Mock
    private HttpServletResponse mockedHttpServletResponse;
    @Mock
    private MappingJackson2HttpMessageConverter mockedMappingJackson2HttpMessageConverter;
    @Captor
    private ArgumentCaptor<ViewModel> viewModelArgumentCaptor;

    private CreateAccountPresenter createAccountPresenter;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedHttpServletResponse);
        reset(this.mockedMappingJackson2HttpMessageConverter);

        this.createAccountPresenter = new CreateAccountPresenter(this.mockedHttpServletResponse,
                this.mockedMappingJackson2HttpMessageConverter);
    }

    @Test
    @DisplayName("Should present on success correctly")
    void shouldPresentOnSuccessCorrectly() throws IOException {
        //Given
        var number = "04321";
        var balance = BigDecimal.TEN;
        var createAccountResponse = new CreateAccountResponse(number, balance);
        var result = Result.success(createAccountResponse);

        doNothing().when(this.mockedMappingJackson2HttpMessageConverter).write(any(ViewModel.class),
                eq(MediaType.APPLICATION_JSON), any(ServletServerHttpResponse.class));
        //When
        this.createAccountPresenter.present(result);

        //Then
        verify(this.mockedMappingJackson2HttpMessageConverter, times(1))
                .write(this.viewModelArgumentCaptor.capture(),
                        eq(MediaType.APPLICATION_JSON), any(ServletServerHttpResponse.class));

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
        final Result<CreateAccountResponse> result = Result.failure(List.of("Error 2"));

        doNothing().when(this.mockedMappingJackson2HttpMessageConverter).write(any(ViewModel.class),
                eq(MediaType.APPLICATION_JSON), any(ServletServerHttpResponse.class));

        //When
        this.createAccountPresenter.present(result);


        //Then
        verify(this.mockedMappingJackson2HttpMessageConverter, times(1))
                .write(this.viewModelArgumentCaptor.capture(),
                        eq(MediaType.APPLICATION_JSON), any(ServletServerHttpResponse.class));

        var viewModel = this.viewModelArgumentCaptor.getValue();

        assertNotNull(viewModel);
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.status());
        assertNull(viewModel.data());
        assertNotNull(viewModel.errors());
    }
}
