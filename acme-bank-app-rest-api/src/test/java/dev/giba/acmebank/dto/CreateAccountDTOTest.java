package dev.giba.acmebank.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAccountDTOTest {
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
        var createAccountDTO = new CreateAccountDTO("123");

        //When
        var validations = this.validator.validate(createAccountDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).isEmpty();
    }

    @Test
    @DisplayName("Should have a violation when the account number is null")
    void shouldHaveAViolationWhenTheAccountNumberIsNull() {
        //Given
        var createAccountDTO = new CreateAccountDTO(null);

        //When
        var validations = this.validator.validate(createAccountDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("accountNumber cannot be null");
    }

    @Test
    @DisplayName("Should have a violation when the account number is empty")
    void shouldHaveAViolationWhenTheAccountNumberIsEmpty() {
        //Given
        var createAccountDTO = new CreateAccountDTO("");

        //When
        var validations = this.validator.validate(createAccountDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("accountNumber cannot be empty");
    }

    @Test
    @DisplayName("Should have a violation when the account number is blank")
    void shouldHaveAViolationWhenTheAccountNumberIsBlank() {
        //Given
        var createAccountDTO = new CreateAccountDTO(" ");

        //When
        var validations = this.validator.validate(createAccountDTO).stream()
                .map(ConstraintViolation::getMessage).toList();

        //Then
        assertThat(validations).contains("accountNumber cannot be blank");
    }
}