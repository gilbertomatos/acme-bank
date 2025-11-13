package dev.giba.acmebank.application.boundary.output;

import java.util.List;
import java.util.Objects;

public record Result<T>(T value, List<String> errors) {

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(List<String> errors) {
        return new Result<>(null, errors);
    }

    public boolean isSuccess() {
        return Objects.isNull(errors) || errors.isEmpty();
    }

    public boolean isFailure() {
        return Objects.nonNull(errors) && !errors.isEmpty();
    }
}
