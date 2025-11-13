package dev.giba.acmebank.infra.persistence.transaction;

import dev.giba.acmebank.application.usecase.ReadOnlyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class ReadOnlyTransactionImpl implements ReadOnlyTransaction {
    private static final Logger logger = LoggerFactory.getLogger(ReadOnlyTransactionImpl.class);

    @Override
    @Transactional(readOnly = true)
    public void execute(final Runnable action) {
        var uuid = UUID.randomUUID().toString();
        logger.debug("### Executing inside a read only transaction... Exec ID: {}, Class: {}", uuid,
                action.getClass().getSimpleName());
        try {
            action.run();
        } finally {
            logger.debug("### Execution done. Exec ID: {}, Class: {}", uuid, action.getClass().getSimpleName());
        }
    }
}
