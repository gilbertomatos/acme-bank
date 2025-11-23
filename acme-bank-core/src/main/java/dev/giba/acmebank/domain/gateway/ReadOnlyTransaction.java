package dev.giba.acmebank.domain.gateway;

@FunctionalInterface
public interface ReadOnlyTransaction {
    void execute(Runnable action);
}
