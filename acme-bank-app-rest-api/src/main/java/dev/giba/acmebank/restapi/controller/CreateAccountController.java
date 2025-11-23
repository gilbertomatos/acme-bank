package dev.giba.acmebank.restapi.controller;

import dev.giba.acmebank.application.usecase.createaccount.CreateAccountInputBoundary;
import dev.giba.acmebank.application.usecase.createaccount.CreateAccountRequest;
import dev.giba.acmebank.restapi.dto.CreateAccountDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class CreateAccountController {
    private final CreateAccountInputBoundary createAccountInputBoundary;

    @Autowired
    public CreateAccountController(final CreateAccountInputBoundary createAccountInputBoundary){
        this.createAccountInputBoundary = Objects.requireNonNull(createAccountInputBoundary,
                "createAccountUseCaseInput is required");
    }

    @PostMapping
    public void createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        Objects.requireNonNull(createAccountDTO, "createAccountDTO is required");
        this.createAccountInputBoundary.execute(new CreateAccountRequest(createAccountDTO.accountNumber()));
    }
}
