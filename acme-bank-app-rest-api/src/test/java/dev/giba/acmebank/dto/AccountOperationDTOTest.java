package dev.giba.acmebank.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountOperationDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Should validate no violations")
    void shouldValidateNoViolations() {
        //Given
        var accountOperationDTO = new AccountOperationDTO(BigDecimal.ONE);

        //When
        var validations = this.validator.validate(accountOperationDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).isEmpty();
    }

    @Test
    @DisplayName("Should have a violation when the amount is null")
    void shouldHaveAViolationWhenTheAmountIsNull() {
        //Given
        var accountOperationDTO = new AccountOperationDTO(null);

        //When
        var validations = this.validator.validate(accountOperationDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("amount cannot be null");
    }

    @Test
    @DisplayName("Should have a violation when the amount is not positive")
    void shouldHaveAViolationWhenTheAmountIsNotPositive() {
        //Given
        var accountOperationDTO = new AccountOperationDTO(new BigDecimal("-100.00"));

        //When
        var validations = this.validator.validate(accountOperationDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("amount must be positive");
    }

    @Test
    @DisplayName("Should have a violation when the amount is lower or equals zero")
    void shouldHaveAViolationWhenTheAccountNumberIsLowerOrEqualsZero() {
        //Given
        var accountOperationDTO = new AccountOperationDTO(BigDecimal.ZERO);

        //When
        var validations = this.validator.validate(accountOperationDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("amount must be at least 0.01");
    }
}