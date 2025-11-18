package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.WithdrawRequest;
import dev.giba.acmebank.application.boundary.input.WithdrawUseCaseInput;
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
class WithdrawControllerTest {
    @Mock
    private WithdrawUseCaseInput mockedWithdrawUseCaseInput;
    @Captor
    private ArgumentCaptor<WithdrawRequest> withdrawRequestArgumentCaptor;

    private WithdrawController withdrawController;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedWithdrawUseCaseInput);

        this.withdrawController = new WithdrawController(this.mockedWithdrawUseCaseInput);
    }

    @Test
    @DisplayName("Should withdraw correctly")
    void shouldWithdrawCorrectly() {
        //Given
        var number = "4567";
        var amount = BigDecimal.TEN;

        doNothing().when(this.mockedWithdrawUseCaseInput).execute(any(WithdrawRequest.class));

        //When
        this.withdrawController.withdraw(number, amount);

        //Then
        verify(this.mockedWithdrawUseCaseInput, times(1))
                .execute(this.withdrawRequestArgumentCaptor.capture());

        assertEquals(number, this.withdrawRequestArgumentCaptor.getValue().accountNumber());
        assertEquals(amount, this.withdrawRequestArgumentCaptor.getValue().amount());
    }
}
