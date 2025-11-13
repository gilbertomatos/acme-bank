package dev.giba.acmebank.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ViewModel(HttpStatusCode status, Object data, List<String> errors) {
}
