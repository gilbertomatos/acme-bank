package dev.giba.acmebank.controller;

import dev.giba.acmebank.application.boundary.input.CreateAccountRequest;
import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import dev.giba.acmebank.presenter.CreateAccountPresenter;
import dev.giba.acmebank.view.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/accounts")
public class CreateAccountController {
    private final CreateAccountPresenter createAccountPresenter;
    private final CreateAccountUseCaseInput createAccountUseCaseInput;

    @Autowired
    public CreateAccountController(final CreateAccountPresenter createAccountPresenter,
                                   final CreateAccountUseCaseInput createAccountUseCaseInput){
        this.createAccountPresenter = Objects.requireNonNull(createAccountPresenter,
                "createAccountPresenter is required");
        this.createAccountUseCaseInput = Objects.requireNonNull(createAccountUseCaseInput,
                "createAccountUseCaseInput is required");
    }

    @PostMapping
    public ResponseEntity<ViewModel> createAccount(@RequestParam("accountNumber") String accountNumber) {
        this.createAccountUseCaseInput.execute(new CreateAccountRequest(accountNumber));
        return this.createAccountPresenter.getViewModel();
    }
}
