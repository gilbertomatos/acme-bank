package dev.giba.acmebank.application.usecase;

@FunctionalInterface
public interface Transaction {
    void execute(Runnable action);
}