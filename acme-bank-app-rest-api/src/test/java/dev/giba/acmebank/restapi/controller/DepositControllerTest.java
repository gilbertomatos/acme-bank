package dev.giba.acmebank.restapi.controller;

import dev.giba.acmebank.application.usecase.deposit.DepositInputBoundary;
import dev.giba.acmebank.application.usecase.deposit.DepositRequest;
import dev.giba.acmebank.restapi.dto.AccountOperationDTO;
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
    private DepositInputBoundary mockedDepositInputBoundary;
    @Captor
    private ArgumentCaptor<DepositRequest> depositRequestArgumentCaptor;

    private DepositController depositController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedDepositInputBoundary);

        this.depositController = new DepositController(this.mockedDepositInputBoundary);
    }

    @Test
    @DisplayName("Should deposit correctly")
    void shouldDepositCorrectly() {
        //Given
        var number = "4567";
        var amount = BigDecimal.TEN;
        var operationAmountDTO = new AccountOperationDTO(amount);

        doNothing().when(this.mockedDepositInputBoundary).execute(any(DepositRequest.class));

        //When
        this.depositController.deposit(number, operationAmountDTO);

        //Then
        verify(this.mockedDepositInputBoundary, times(1))
                .execute(this.depositRequestArgumentCaptor.capture());

        assertEquals(number, this.depositRequestArgumentCaptor.getValue().accountNumber());
        assertEquals(amount, this.depositRequestArgumentCaptor.getValue().amount());
    }
}
