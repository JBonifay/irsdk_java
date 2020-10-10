package com.joffrey.irsdkjava.library;

import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.telemetry.TelemetryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class TelemetryServiceUT {

    @MockBean
    private SdkStarter sdkStarter;

    private TelemetryService telemetryService;

    @BeforeAll
    void init() {
        telemetryService = new TelemetryService(sdkStarter);
    }


    @DisplayName("getWeatherType() test")
    @ParameterizedTest
    @ValueSource(ints = {-1, 2, 100})
    void When_InvalidIntValueForWeather_Expect_returnEmptyWeatherString(int weahterIntValue) {




    }


}
