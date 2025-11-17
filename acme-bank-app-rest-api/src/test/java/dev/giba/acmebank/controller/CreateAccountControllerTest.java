package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import dev.giba.acmebank.presenter.CreateAccountPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountControllerTest {
    @Mock
    private CreateAccountPresenter mockedCreateAccountPresenter;
    @Mock
    private CreateAccountUseCaseInput mockedCreateAccountUseCaseInput;
    @Captor
    private ArgumentCaptor<CreateAccountRequest> createAccountRequestArgumentCaptor;

    private CreateAccountController createAccountController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedCreateAccountPresenter);
        reset(this.mockedCreateAccountUseCaseInput);

        this.createAccountController = new CreateAccountController(this.mockedCreateAccountPresenter,
                this.mockedCreateAccountUseCaseInput);
    }

    @Test
    @DisplayName("Should create account correctly")
    void shouldDepositCorrectly() {
        //Given
        var number = "890";

        doNothing().when(this.mockedCreateAccountUseCaseInput).execute(any(CreateAccountRequest.class));
        when(this.mockedCreateAccountPresenter.getViewModel()).thenReturn(ResponseEntity.ok().build());

        //When
        var response = this.createAccountController.createAccount(number);

        //Then
        assertNotNull(response);

        verify(this.mockedCreateAccountUseCaseInput, times(1))
                .execute(this.createAccountRequestArgumentCaptor.capture());

        assertEquals(number, this.createAccountRequestArgumentCaptor.getValue().accountNumber());
    }
}
