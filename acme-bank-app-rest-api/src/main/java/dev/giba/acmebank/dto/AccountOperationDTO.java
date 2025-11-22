package dev.giba.acmebank.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountOperationDTO(
        @NotNull(message = "amount cannot be null")
        @Positive(message = "amount must be positive")
        @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
        BigDecimal amount
) {}