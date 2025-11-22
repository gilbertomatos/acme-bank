package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.DepositRequest;
import dev.giba.acmebank.application.boundary.input.DepositUseCaseInput;
import dev.giba.acmebank.dto.AccountOperationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class DepositController {
    private final DepositUseCaseInput depositUseCaseInput;

    @Autowired
    public DepositController(final DepositUseCaseInput depositUseCaseInput){
        this.depositUseCaseInput = Objects.requireNonNull(depositUseCaseInput,
                "depositUseCaseInput is required");
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable("accountNumber") String accountNumber,
                        @Valid @RequestBody AccountOperationDTO accountOperationDTO) {
        Objects.requireNonNull(accountNumber, "accountNumber is required");
        Objects.requireNonNull(accountOperationDTO, "operationAmountDTO is required");
        this.depositUseCaseInput.execute(new DepositRequest(accountNumber, accountOperationDTO.amount()));
    }
}
