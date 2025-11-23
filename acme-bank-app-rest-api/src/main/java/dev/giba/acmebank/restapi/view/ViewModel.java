package dev.giba.acmebank.restapi.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ViewModel(HttpStatusCode status, Object data, List<String> errors) {
    public static ViewModel of(HttpStatusCode status, Object data) {
        return new ViewModel(status, data, null);
    }
    public static ViewModel of(HttpStatusCode status, List<String> errors) {
        return new ViewModel(status, null, List.copyOf(errors));
    }
}
