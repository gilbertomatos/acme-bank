package dev.giba.acmebank.infra.persistence.transaction;

import dev.giba.acmebank.application.usecase.Transaction;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
public class TransactionImpl implements Transaction {
    private static final Logger logger = LoggerFactory.getLogger(TransactionImpl.class);

    @Override
    @Transactional
    public void execute(final Runnable action) {
        var uuid = UUID.randomUUID().toString();
        logger.debug("### Executing inside a transaction... Exec ID: {}, Class: {}", uuid,
                action.getClass().getSimpleName());
        try {
            action.run();
        } finally {
            logger.debug("### Execution done. Exec ID: {}, Class: {}", uuid, action.getClass().getSimpleName());
        }

    }
}