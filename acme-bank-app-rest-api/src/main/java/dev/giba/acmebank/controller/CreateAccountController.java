package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import dev.giba.acmebank.dto.CreateAccountDTO;
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
    private final CreateAccountUseCaseInput createAccountUseCaseInput;

    @Autowired
    public CreateAccountController(final CreateAccountUseCaseInput createAccountUseCaseInput){
        this.createAccountUseCaseInput = Objects.requireNonNull(createAccountUseCaseInput,
                "createAccountUseCaseInput is required");
    }

    @PostMapping
    public void createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        Objects.requireNonNull(createAccountDTO, "createAccountDTO is required");
        this.createAccountUseCaseInput.execute(new CreateAccountRequest(createAccountDTO.accountNumber()));
    }
}
