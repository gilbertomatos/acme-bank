package dev.giba.acmebank.infra.persistence.jpa.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionImplTest {
    @Mock
    private Runnable mockedAction;

    private TransactionImpl transactionExecutorImpl;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedAction);
        this.transactionExecutorImpl = new TransactionImpl();
    }

    @Test
    @DisplayName("Should execute correctly")
    void shouldExecuteCorrectly() {
        //Given
        doNothing().when(this.mockedAction).run();

        //When
        this.transactionExecutorImpl.execute(this.mockedAction);

        //Then
        verify(this.mockedAction, times(1)).run();
    }
}