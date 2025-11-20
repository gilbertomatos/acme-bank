package dev.giba.acmebank.infra.persistence.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages="dev.giba.acmebank.infra.persistence.jpa.repository")
@EntityScan(basePackages="dev.giba.acmebank.infra.persistence.jpa.entity")
@ComponentScan(basePackages={
        "dev.giba.acmebank.infra.persistence.jpa.adapter",
        "dev.giba.acmebank.infra.persistence.jpa.transaction"
})
@EnableTransactionManagement
public class InfraPersistenceSpringDataJPAConfig {
}
