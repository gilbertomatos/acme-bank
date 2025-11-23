package dev.giba.acmebank.restapi;

import dev.giba.acmebank.restapi.config.ApplicationUseCaseConfig;
import dev.giba.acmebank.infra.persistence.jpa.config.InfraPersistenceSpringDataJPAConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        ApplicationUseCaseConfig.class,
        InfraPersistenceSpringDataJPAConfig.class
})
public class AcmeBankRestApi {
    public static void main(String[] args) {
        SpringApplication.run(AcmeBankRestApi.class, args);
    }
}