package dev.giba.acmebank.infra.persistence.jpa;

import dev.giba.acmebank.infra.persistence.jpa.config.InfraPersistenceSpringDataJPAConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(InfraPersistenceSpringDataJPAConfig.class)
public class TestApplication {
}
