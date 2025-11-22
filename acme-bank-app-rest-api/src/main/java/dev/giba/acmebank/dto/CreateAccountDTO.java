package dev.giba.acmebank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAccountDTO(
        @NotNull(message = "accountNumber cannot be null")
        @NotEmpty(message = "accountNumber cannot be empty")
        @NotBlank(message = "accountNumber cannot be blank")
        String accountNumber
) {}
