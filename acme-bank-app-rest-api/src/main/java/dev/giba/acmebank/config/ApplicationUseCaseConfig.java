package dev.giba.acmebank.config;

import dev.giba.acmebank.application.boundary.input.CreateAccountUseCaseInput;
import dev.giba.acmebank.application.boundary.input.DepositUseCaseInput;
import dev.giba.acmebank.application.boundary.input.GetAccountStatementUseCaseInput;
import dev.giba.acmebank.application.boundary.input.WithdrawUseCaseInput;
import dev.giba.acmebank.application.boundary.output.CreateAccountUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.DepositUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.GetAccountStatementUseCaseOutput;
import dev.giba.acmebank.application.boundary.output.WithdrawUseCaseOutput;
import dev.giba.acmebank.application.usecase.*;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationUseCaseConfig {

    @Bean
    public CreateAccountUseCaseInput createAccountUseCaseInput(
            final CreateAccountUseCaseOutput createAccountUseCaseOutput,
            final AccountEntityGateway accountEntityGateway,
            final Transaction transaction) {
        return new CreateAccountUseCase(createAccountUseCaseOutput, accountEntityGateway, transaction);
    }

    @Bean
    public DepositUseCaseInput depositUseCaseInput(final DepositUseCaseOutput depositUseCaseOutput,
                                                   final AccountEntityGateway accountEntityGateway,
                                                   final Transaction transaction) {
        return new DepositUseCase(depositUseCaseOutput, accountEntityGateway, transaction);
    }

    @Bean
    public GetAccountStatementUseCaseInput getAccountStatementUseCaseInput(
            final GetAccountStatementUseCaseOutput getAccountStatementUseCaseOutput,
            final AccountEntityGateway accountEntityGateway,
            final ReadOnlyTransaction readOnlyTransaction) {
        return new GetAccountStatementUseCase(getAccountStatementUseCaseOutput, accountEntityGateway,
                readOnlyTransaction);
    }

    @Bean
    public WithdrawUseCaseInput withdrawUseCaseInput(final WithdrawUseCaseOutput withdrawUseCaseOutput,
                                                    final AccountEntityGateway accountEntityGateway,
                                                    final Transaction transaction) {
        return new WithdrawUseCase(withdrawUseCaseOutput, accountEntityGateway, transaction);
    }
}
