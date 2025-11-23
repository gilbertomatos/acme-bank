package dev.giba.acmebank.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationPresenterTest {
    @Mock
    private BindingResult mockedBindingResult;
    @Mock
    private MethodArgumentNotValidException mockedMethodArgumentNotValidException;

    private ValidationPresenter validationPresenter;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedBindingResult);
        reset(this.mockedMethodArgumentNotValidException);

        this.validationPresenter = new ValidationPresenter();
    }

    @Test
    @DisplayName("Should present on error correctly")
    void shouldPresentOnErrorCorrectly() {
        //Given
        var errors = List.of(new ObjectError("error", "Error 15"));

        when(this.mockedBindingResult.getAllErrors()).thenReturn(errors);
        when(this.mockedMethodArgumentNotValidException.getBindingResult()).thenReturn(this.mockedBindingResult);

        //When
        var viewModel = this.validationPresenter.presentValidationExceptions(
                this.mockedMethodArgumentNotValidException);

        assertNotNull(viewModel);
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.status());
        assertNull(viewModel.data());
        assertNotNull(viewModel.errors());
    }
}
