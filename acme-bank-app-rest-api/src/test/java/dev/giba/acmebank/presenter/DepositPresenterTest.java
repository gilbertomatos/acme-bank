package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.DepositResponse;
import dev.giba.acmebank.application.boundary.output.Result;
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
class DepositPresenterTest {

    private DepositPresenter depositPresenter;

    @BeforeEach
    void beforeEachTest() {
        this.depositPresenter = new DepositPresenter();
    }

    @Test
    @DisplayName("Should present on success correctly")
    void shouldPresentOnSuccessCorrectly() {
        //Given
        var number = "04321";
        var balance = BigDecimal.TEN;
        var depositResponse = new DepositResponse(number, balance);
        var result = Result.success(depositResponse);

        //When
        this.depositPresenter.present(result);
        var viewModel = this.depositPresenter.getViewModel();

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
        final Result<DepositResponse> result = Result.failure(List.of("Error 1"));

        //When
        this.depositPresenter.present(result);
        var viewModel = this.depositPresenter.getViewModel();

        //Then
        assertNotNull(viewModel);
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getStatusCode());
        assertNotNull(viewModel.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getBody().status());
        assertNull(viewModel.getBody().data());
        assertNotNull(viewModel.getBody().errors());
    }
}
