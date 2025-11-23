package dev.giba.acmebank.domain.gateway;

@FunctionalInterface
public interface Transaction {
    void execute(Runnable action);
}