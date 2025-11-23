package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.usecase.deposit.DepositInputBoundary;
import dev.giba.acmebank.application.usecase.deposit.DepositRequest;
import dev.giba.acmebank.dto.AccountOperationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class DepositController {
    private final DepositInputBoundary depositInputBoundary;

    @Autowired
    public DepositController(final DepositInputBoundary depositInputBoundary){
        this.depositInputBoundary = Objects.requireNonNull(depositInputBoundary,
                "depositUseCaseInput is required");
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable("accountNumber") String accountNumber,
                        @Valid @RequestBody AccountOperationDTO accountOperationDTO) {
        this.depositInputBoundary.execute(new DepositRequest(accountNumber, accountOperationDTO.amount()));
    }
}
