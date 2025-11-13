package dev.giba.acmebank.infra.persistence;

import dev.giba.acmebank.infra.persistence.config.InfraPersistenceSpringDataJPAConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(InfraPersistenceSpringDataJPAConfig.class)
public class TestApplication {
}
