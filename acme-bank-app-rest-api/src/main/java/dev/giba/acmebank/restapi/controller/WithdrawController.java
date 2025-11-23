package dev.giba.acmebank.restapi.controller;

import dev.giba.acmebank.application.usecase.withdraw.WithdrawRequest;
import dev.giba.acmebank.application.usecase.withdraw.WithdrawUseCaseInput;
import dev.giba.acmebank.restapi.dto.AccountOperationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                         @Valid @RequestBody AccountOperationDTO accountOperationDTO) {
        this.withdrawUseCaseInput.execute(new WithdrawRequest(accountNumber, accountOperationDTO.amount()));
    }
}
