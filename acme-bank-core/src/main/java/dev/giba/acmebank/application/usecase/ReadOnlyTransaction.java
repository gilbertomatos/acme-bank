package dev.giba.acmebank.application.usecase;

@FunctionalInterface
public interface ReadOnlyTransaction {
    void execute(Runnable action);
}
