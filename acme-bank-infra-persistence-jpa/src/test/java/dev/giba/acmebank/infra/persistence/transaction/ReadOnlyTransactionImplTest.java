package dev.giba.acmebank.infra.persistence.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadOnlyTransactionImplTest {

    @Mock
    private Runnable mockedAction;

    private ReadOnlyTransactionImpl readOnlyTransactionTemplateImpl;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedAction);
        this.readOnlyTransactionTemplateImpl = new ReadOnlyTransactionImpl();
    }

    @Test
    @DisplayName("Should execute correctly")
    void shouldExecuteCorrectly() {
        //Given
        doNothing().when(this.mockedAction).run();

        //When
        this.readOnlyTransactionTemplateImpl.execute(this.mockedAction);

        //Then
        verify(this.mockedAction, times(1)).run();
    }
}
