package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.GetAccountStatementRequest;
import dev.giba.acmebank.application.boundary.input.GetAccountStatementUseCaseInput;
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
class GetAccountStatementControllerTest {
    @Mock
    private GetAccountStatementUseCaseInput mockedGetAccountStatementUseCaseInput;
    @Captor
    private ArgumentCaptor<GetAccountStatementRequest> getAccountStatementRequestArgumentCaptor;

    private GetAccountStatementController getAccountStatementController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedGetAccountStatementUseCaseInput);

        this.getAccountStatementController = new GetAccountStatementController(
                this.mockedGetAccountStatementUseCaseInput);
    }

    @Test
    @DisplayName("Should create account correctly")
    void shouldDepositCorrectly() {
        //Given
        var number = "890";

        doNothing().when(this.mockedGetAccountStatementUseCaseInput).execute(any(GetAccountStatementRequest.class));

        //When
        this.getAccountStatementController.getAccountStatement(number);

        //Then
        verify(this.mockedGetAccountStatementUseCaseInput, times(1))
                .execute(this.getAccountStatementRequestArgumentCaptor.capture());

        assertEquals(number, this.getAccountStatementRequestArgumentCaptor.getValue().accountNumber());
    }
}
