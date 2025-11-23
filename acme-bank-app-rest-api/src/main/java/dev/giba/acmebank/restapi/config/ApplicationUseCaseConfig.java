package dev.giba.acmebank.restapi.config;

import dev.giba.acmebank.application.usecase.createaccount.CreateAccountInputBoundary;
import dev.giba.acmebank.application.usecase.createaccount.CreateAccountOutputBoundary;
import dev.giba.acmebank.application.usecase.createaccount.CreateAccountUseCase;
import dev.giba.acmebank.application.usecase.deposit.DepositInputBoundary;
import dev.giba.acmebank.application.usecase.deposit.DepositOutputBoundary;
import dev.giba.acmebank.application.usecase.deposit.DepositUseCase;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementInputBoundary;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementOutputBoundary;
import dev.giba.acmebank.application.usecase.getaccountstatement.GetAccountStatementUseCase;
import dev.giba.acmebank.application.usecase.withdraw.WithdrawUseCase;
import dev.giba.acmebank.application.usecase.withdraw.WithdrawUseCaseInput;
import dev.giba.acmebank.application.usecase.withdraw.WithdrawUseCaseOutput;
import dev.giba.acmebank.domain.gateway.AccountEntityGateway;
import dev.giba.acmebank.domain.gateway.ReadOnlyTransaction;
import dev.giba.acmebank.domain.gateway.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationUseCaseConfig {

    @Bean
    public CreateAccountInputBoundary createAccountUseCaseInput(
            final CreateAccountOutputBoundary createAccountOutputBoundary,
            final AccountEntityGateway accountEntityGateway,
            final Transaction transaction) {
        return new CreateAccountUseCase(createAccountOutputBoundary, accountEntityGateway, transaction);
    }

    @Bean
    public DepositInputBoundary depositUseCaseInput(final DepositOutputBoundary depositOutputBoundary,
                                                    final AccountEntityGateway accountEntityGateway,
                                                    final Transaction transaction) {
        return new DepositUseCase(depositOutputBoundary, accountEntityGateway, transaction);
    }

    @Bean
    public GetAccountStatementInputBoundary getAccountStatementUseCaseInput(
            final GetAccountStatementOutputBoundary getAccountStatementOutputBoundary,
            final AccountEntityGateway accountEntityGateway,
            final ReadOnlyTransaction readOnlyTransaction) {
        return new GetAccountStatementUseCase(getAccountStatementOutputBoundary, accountEntityGateway,
                readOnlyTransaction);
    }

    @Bean
    public WithdrawUseCaseInput withdrawUseCaseInput(final WithdrawUseCaseOutput withdrawUseCaseOutput,
                                                    final AccountEntityGateway accountEntityGateway,
                                                    final Transaction transaction) {
        return new WithdrawUseCase(withdrawUseCaseOutput, accountEntityGateway, transaction);
    }
}
