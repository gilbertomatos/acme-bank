package dev.giba.acmebank.presenter;

import dev.giba.acmebank.view.ViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ValidationPresenter {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ViewModel presentValidationExceptions(
            final MethodArgumentNotValidException methodArgumentNotValidException) {
        Objects.requireNonNull(methodArgumentNotValidException, "methodArgumentNotValidException is required");

        final List<String> errors = new ArrayList<>();

        for (ObjectError error : methodArgumentNotValidException.getBindingResult().getAllErrors()) {
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        }

        return ViewModel.of(HttpStatus.BAD_REQUEST, errors);
    }
}
