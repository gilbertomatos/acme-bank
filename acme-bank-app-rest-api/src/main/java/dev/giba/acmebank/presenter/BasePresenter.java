package dev.giba.acmebank.presenter;

import dev.giba.acmebank.application.boundary.output.Result;
import dev.giba.acmebank.view.ViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


public abstract class BasePresenter<T> {
    private Result<T> result;

    protected void execute(final Result<T> result) {
        Objects.requireNonNull(result, "result is required");
        this.result = result;
    }

    public ResponseEntity<ViewModel> present() {
        if (this.result.isSuccess()) {
            return ResponseEntity.ok(new ViewModel(HttpStatus.OK, this.result.value(), null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ViewModel(HttpStatus.BAD_REQUEST,
                    this.result.value(), this.result.errors()));
        }
    }
}
