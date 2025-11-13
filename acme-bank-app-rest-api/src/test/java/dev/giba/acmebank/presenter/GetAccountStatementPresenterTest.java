package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.GetAccountStatementResponse;
import dev.giba.acmebank.application.boundary.output.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetAccountStatementPresenterTest {

    private GetAccountStatementPresenter getAccountStatementPresenter;

    @BeforeEach
    void beforeEachTest() {
        this.getAccountStatementPresenter = new GetAccountStatementPresenter();
    }

    @Test
    @DisplayName("Should present on success correctly")
    void shouldPresentOnSuccessCorrectly() {
        //Given
        var number = "043321";
        var balance = BigDecimal.TEN;
        var getAccountStatementResponse = new GetAccountStatementResponse(number, balance, Collections.emptyList());
        var result = Result.success(getAccountStatementResponse);

        //When
        this.getAccountStatementPresenter.execute(result);
        var viewModel = this.getAccountStatementPresenter.present();

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
        final Result<GetAccountStatementResponse> result = Result.failure(List.of("Error 3"));

        //When
        this.getAccountStatementPresenter.execute(result);
        var viewModel = this.getAccountStatementPresenter.present();

        //Then
        assertNotNull(viewModel);
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getStatusCode());
        assertNotNull(viewModel.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, viewModel.getBody().status());
        assertNull(viewModel.getBody().data());
        assertNotNull(viewModel.getBody().errors());
    }
}
