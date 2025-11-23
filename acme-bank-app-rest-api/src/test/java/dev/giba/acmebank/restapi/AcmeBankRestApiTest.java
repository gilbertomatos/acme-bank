package dev.giba.acmebank.restapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class AcmeBankRestApiTest {
    @Mock
    private ConfigurableApplicationContext mockedConfigurableApplicationContext;

    @BeforeEach
    void beforeEachTest() {
        reset(this.mockedConfigurableApplicationContext);
    }

    @Test
    @DisplayName("Should run spring application correctly")
    void shouldRunSpringApplicationCorrectly() {
        try (MockedStatic<SpringApplication> springApplicationMockedStatic
                     = Mockito.mockStatic(SpringApplication.class)) {
            //Given
            var args = new String[]{"p0", "p1"};
            springApplicationMockedStatic.when(()->SpringApplication.run(AcmeBankRestApi.class, args))
                    .thenReturn(this.mockedConfigurableApplicationContext);

            //When
            AcmeBankRestApi.main(args);

            //Then
            springApplicationMockedStatic.verify(()->SpringApplication.run(AcmeBankRestApi.class, args));
        }
    }
}
