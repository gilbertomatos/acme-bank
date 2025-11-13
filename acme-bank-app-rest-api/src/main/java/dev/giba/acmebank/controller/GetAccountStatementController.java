package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.GetAccountStatementRequest;
import dev.giba.acmebank.application.boundary.input.GetAccountStatementUseCaseInput;
import dev.giba.acmebank.presenter.GetAccountStatementPresenter;
import dev.giba.acmebank.view.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class GetAccountStatementController {
    private final GetAccountStatementPresenter getAccountStatementPresenter;
    private final GetAccountStatementUseCaseInput getAccountStatementUseCaseInput;

    @Autowired
    public GetAccountStatementController(final GetAccountStatementPresenter getAccountStatementPresenter,
                                         final GetAccountStatementUseCaseInput getAccountStatementUseCaseInput){
        this.getAccountStatementPresenter = Objects.requireNonNull(getAccountStatementPresenter,
                "getAccountStatementPresenter is required");
        this.getAccountStatementUseCaseInput = Objects.requireNonNull(getAccountStatementUseCaseInput,
                "getAccountStatementUseCaseInput is required");
    }

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<ViewModel> getAccountStatement(@PathVariable("accountNumber") String accountNumber) {
        this.getAccountStatementUseCaseInput.execute(new GetAccountStatementRequest(accountNumber));
        return this.getAccountStatementPresenter.present();
    }
}
