package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class CreateAccountController {
    private final CreateAccountUseCaseInput createAccountUseCaseInput;

    @Autowired
    public CreateAccountController(final CreateAccountUseCaseInput createAccountUseCaseInput){
        this.createAccountUseCaseInput = Objects.requireNonNull(createAccountUseCaseInput,
                "createAccountUseCaseInput is required");
    }

    @PostMapping
    public void createAccount(@RequestParam("accountNumber") String accountNumber) {
        this.createAccountUseCaseInput.execute(new CreateAccountRequest(accountNumber));
    }
}
