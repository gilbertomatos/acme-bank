package dev.giba.acmebank.restapi.controller;

import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementInputBoundary;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class GetAccountStatementController {
    private final GetAccountStatementInputBoundary getAccountStatementInputBoundary;

    @Autowired
    public GetAccountStatementController(final GetAccountStatementInputBoundary getAccountStatementInputBoundary){
        this.getAccountStatementInputBoundary = Objects.requireNonNull(getAccountStatementInputBoundary,
                "getAccountStatementUseCaseInput is required");
    }

    @GetMapping("/{accountNumber}/statement")
    public void getAccountStatement(@PathVariable("accountNumber") String accountNumber) {
        this.getAccountStatementInputBoundary.execute(new GetAccountStatementRequest(accountNumber));
    }
}
