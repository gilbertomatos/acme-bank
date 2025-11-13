package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.DepositRequest;
import dev.giba.acmebank.application.boundary.input.DepositUseCaseInput;
import dev.giba.acmebank.presenter.DepositPresenter;
import dev.giba.acmebank.view.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class DepositController {
    private final DepositPresenter depositPresenter;
    private final DepositUseCaseInput depositUseCaseInput;

    @Autowired
    public DepositController(final DepositPresenter depositPresenter, final DepositUseCaseInput depositUseCaseInput){
        this.depositPresenter = Objects.requireNonNull(depositPresenter, "depositPresenter is required");
        this.depositUseCaseInput = Objects.requireNonNull(depositUseCaseInput,
                "depositUseCaseInput is required");
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<ViewModel> deposit(@PathVariable("accountNumber") String accountNumber,
                                             @RequestParam("amount") BigDecimal amount) {
        this.depositUseCaseInput.execute(new DepositRequest(accountNumber, amount));
        return this.depositPresenter.present();
    }
}
