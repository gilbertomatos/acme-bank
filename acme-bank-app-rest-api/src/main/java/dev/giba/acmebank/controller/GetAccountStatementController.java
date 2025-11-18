package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.GetAccountStatementRequest;
import dev.giba.acmebank.application.boundary.input.GetAccountStatementUseCaseInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class GetAccountStatementController {
    private final GetAccountStatementUseCaseInput getAccountStatementUseCaseInput;

    @Autowired
    public GetAccountStatementController(final GetAccountStatementUseCaseInput getAccountStatementUseCaseInput){
        this.getAccountStatementUseCaseInput = Objects.requireNonNull(getAccountStatementUseCaseInput,
                "getAccountStatementUseCaseInput is required");
    }

    @GetMapping("/{accountNumber}/statement")
    public void getAccountStatement(@PathVariable("accountNumber") String accountNumber) {
        this.getAccountStatementUseCaseInput.execute(new GetAccountStatementRequest(accountNumber));
    }
}
