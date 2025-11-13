package dev.giba.acmebank.infra.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages="dev.giba.acmebank.infra.persistence.repository")
@EntityScan(basePackages="dev.giba.acmebank.infra.persistence.entity")
@ComponentScan(basePackages={
        "dev.giba.acmebank.infra.persistence.adapter",
        "dev.giba.acmebank.infra.persistence.transaction"
})
@EnableTransactionManagement
public class InfraPersistenceSpringDataJPAConfig {
}
