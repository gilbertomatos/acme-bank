package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountControllerTest {
    @Mock
    private CreateAccountUseCaseInput mockedCreateAccountUseCaseInput;
    @Captor
    private ArgumentCaptor<CreateAccountRequest> createAccountRequestArgumentCaptor;

    private CreateAccountController createAccountController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedCreateAccountUseCaseInput);

        this.createAccountController = new CreateAccountController(this.mockedCreateAccountUseCaseInput);
    }

    @Test
    @DisplayName("Should create account correctly")
    void shouldDepositCorrectly() {
        //Given
        var number = "890";

        doNothing().when(this.mockedCreateAccountUseCaseInput).execute(any(CreateAccountRequest.class));

        //When
        this.createAccountController.createAccount(number);

        //Then
        verify(this.mockedCreateAccountUseCaseInput, times(1))
                .execute(this.createAccountRequestArgumentCaptor.capture());

        assertEquals(number, this.createAccountRequestArgumentCaptor.getValue().accountNumber());
    }
}
