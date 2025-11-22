package dev.giba.acmebank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountOperationDTO(
        @NotNull(message = "amount cannot be null")
        @Positive(message = "amount must be positive")
        @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
        BigDecimal amount
) {}