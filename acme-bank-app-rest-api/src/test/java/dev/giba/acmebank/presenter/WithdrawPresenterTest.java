package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.Result;
import dev.giba.acmebank.application.boundary.output.WithdrawResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WithdrawPresenterTest {

    private WithdrawPresenter withdrawPresenter;

    @BeforeEach
    void beforeEachTest() {
        this.withdrawPresenter = new WithdrawPresenter();
    }

    @Test
    @DisplayName("Should present on success correctly")
    void shouldPresentOnSuccessCorrectly() {
        //Given
        var number = "043215";
        var balance = BigDecimal.TEN;
        var withdrawResponse = new WithdrawResponse(number, balance);
        var result = Result.success(withdrawResponse);

        //When
        this.withdrawPresenter.execute(result);
        var viewModel = this.withdrawPresenter.present();

        //Then
        assertNotNull(viewModel);
        assertEquals(HttpStatus.OK, viewModel.getStatusCode());
        assertNotNull(viewModel.getBody());
        assertEquals(HttpStatus.OK, viewModel.getBody().status());
        assertNotNull(viewModel.getBody().data());
        assertNull(viewModel.getBody().errors());
    }

    @Test
    @DisplayName("Should present on error correctly")
    void shouldPresentOnErrorCorrectly() {
        //Given
        final Result<WithdrawResponse> result = Result.failure(List.of("Error 14"));

        //When
        this.withdrawPresenter.execute(result);
        var viewModel = this.withdrawPresenter.present();

        //Then
        assertNotNull(viewModel);
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getStatusCode());
        assertNotNull(viewModel.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getBody().status());
        assertNull(viewModel.getBody().data());
        assertNotNull(viewModel.getBody().errors());
    }
}
