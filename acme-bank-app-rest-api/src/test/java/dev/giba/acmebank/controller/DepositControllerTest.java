package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.DepositRequest;
import dev.giba.acmebank.application.boundary.input.DepositUseCaseInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositControllerTest {
    @Mock
    private DepositUseCaseInput mockedDepositUseCaseInput;
    @Captor
    private ArgumentCaptor<DepositRequest> depositRequestArgumentCaptor;

    private DepositController depositController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedDepositUseCaseInput);

        this.depositController = new DepositController(this.mockedDepositUseCaseInput);
    }

    @Test
    @DisplayName("Should deposit correctly")
    void shouldDepositCorrectly() {
        //Given
        var number = "4567";
        var amount = BigDecimal.TEN;

        doNothing().when(this.mockedDepositUseCaseInput).execute(any(DepositRequest.class));

        //When
        this.depositController.deposit(number, amount);

        //Then
        verify(this.mockedDepositUseCaseInput, times(1))
                .execute(this.depositRequestArgumentCaptor.capture());

        assertEquals(number, this.depositRequestArgumentCaptor.getValue().accountNumber());
        assertEquals(amount, this.depositRequestArgumentCaptor.getValue().amount());
    }
}
