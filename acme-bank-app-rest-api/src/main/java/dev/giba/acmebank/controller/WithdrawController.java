package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.WithdrawRequest;
import dev.giba.acmebank.application.boundary.input.WithdrawUseCaseInput;
import dev.giba.acmebank.presenter.WithdrawPresenter;
import dev.giba.acmebank.view.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class WithdrawController {
    private final WithdrawPresenter withdrawPresenter;
    private final WithdrawUseCaseInput withdrawUseCaseInput;

    @Autowired
    public WithdrawController(final WithdrawPresenter withdrawPresenter,
                              final WithdrawUseCaseInput withdrawUseCaseInput){
        this.withdrawPresenter = Objects.requireNonNull(withdrawPresenter, "withdrawPresenter is required");
        this.withdrawUseCaseInput = Objects.requireNonNull(withdrawUseCaseInput,
                "withdrawUseCaseInput is required");
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ViewModel> withdraw(@PathVariable("accountNumber") String accountNumber,
                                             @RequestParam("amount") BigDecimal amount) {
        this.withdrawUseCaseInput.execute(new WithdrawRequest(accountNumber, amount));
        return this.withdrawPresenter.getViewModel();
    }
}
