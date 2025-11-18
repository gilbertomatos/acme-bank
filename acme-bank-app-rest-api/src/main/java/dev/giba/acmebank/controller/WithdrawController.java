package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.WithdrawRequest;
import dev.giba.acmebank.application.boundary.input.WithdrawUseCaseInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class WithdrawController {
    private final WithdrawUseCaseInput withdrawUseCaseInput;

    @Autowired
    public WithdrawController(final WithdrawUseCaseInput withdrawUseCaseInput){
        this.withdrawUseCaseInput = Objects.requireNonNull(withdrawUseCaseInput,
                "withdrawUseCaseInput is required");
    }

    @PostMapping("/{accountNumber}/withdraw")
    public void withdraw(@PathVariable("accountNumber") String accountNumber,
                                             @RequestParam("amount") BigDecimal amount) {
        this.withdrawUseCaseInput.execute(new WithdrawRequest(accountNumber, amount));
    }
}
