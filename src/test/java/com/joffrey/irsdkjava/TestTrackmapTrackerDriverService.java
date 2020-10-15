package com.joffrey.irsdkjava;

import static com.joffrey.irsdkjava.YamlHelperTest.createByteBufferYamlFile;
import static com.joffrey.irsdkjava.YamlHelperTest.loadYamlObject;
import static org.assertj.core.api.Assertions.assertThat;

import com.joffrey.irsdkjava.laptiming.LapTimingService;
import com.joffrey.irsdkjava.model.Header;
import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.trackmaptracker.TrackmapTrackerService;
import com.joffrey.irsdkjava.yaml.YamlService;
import com.joffrey.irsdkjava.yaml.irsdkyaml.YamlFile;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class TestTrackmapTrackerDriverService {

    @MockBean
    private SdkStarter  sdkStarter;
    @MockBean
    private Header      header;
    @MockBean
    private YamlService yamlService;

    // Class under test
    private TrackmapTrackerService trackmapTrackerService;

    // Helpers Objects
    private ByteBuffer byteBufferYamlFile;


    @BeforeEach
    void init() {
        trackmapTrackerService = new TrackmapTrackerService(sdkStarter, yamlService);
        byteBufferYamlFile = createByteBufferYamlFile("trackmaptracker/trackmaptracker.yml");
        YamlFile yamlFile = loadYamlObject(byteBufferYamlFile);

        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
        Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);
    }

    @DisplayName("getTrackmapTrackerListFlux() - Test if data where good fetched from yaml and fake MemMapFile")
    @Test
    void Given_ValidDataFromYamlAndMemMapFile_When_CallingFlux_Then_SHouldReturnGoodDataInFlux() {

        StepVerifier.create(trackmapTrackerService.getTrackmapTrackerListFlux())
                    .then(() -> {
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 63)).thenReturn(0.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 12)).thenReturn(0.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 23)).thenReturn(0.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 49)).thenReturn(0.0f);
                    })
                    .assertNext(trackmapTrackers -> {

                        assertThat(trackmapTrackers.get(0).getDriverIdx()).isEqualTo(63);
                        assertThat(trackmapTrackers.get(0).getDriverCarNbr()).isEqualTo(63);
                        assertThat(trackmapTrackers.get(0).getDriverInitials()).isEqualTo("JO");
                        assertThat(trackmapTrackers.get(0).getDriverDistPct()).isEqualTo(0.0f);

                        assertThat(trackmapTrackers.get(1).getDriverIdx()).isEqualTo(12);
                        assertThat(trackmapTrackers.get(1).getDriverCarNbr()).isEqualTo(12);
                        assertThat(trackmapTrackers.get(1).getDriverInitials()).isEqualTo("DT");
                        assertThat(trackmapTrackers.get(1).getDriverDistPct()).isEqualTo(0.0f);

                        assertThat(trackmapTrackers.get(2).getDriverIdx()).isEqualTo(23);
                        assertThat(trackmapTrackers.get(2).getDriverCarNbr()).isEqualTo(23);
                        assertThat(trackmapTrackers.get(2).getDriverInitials()).isEqualTo("DT");
                        assertThat(trackmapTrackers.get(2).getDriverDistPct()).isEqualTo(0.0f);

                        assertThat(trackmapTrackers.get(3).getDriverIdx()).isEqualTo(49);
                        assertThat(trackmapTrackers.get(3).getDriverCarNbr()).isEqualTo(49);
                        assertThat(trackmapTrackers.get(3).getDriverInitials()).isEqualTo("DF");
                        assertThat(trackmapTrackers.get(3).getDriverDistPct()).isEqualTo(0.0f);


                    })
                    .then(() -> {
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 63)).thenReturn(10.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 12)).thenReturn(20.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 23)).thenReturn(30.0f);
                        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 49)).thenReturn(40.0f);
                    })
                    .assertNext(trackmapTrackers -> {

                        assertThat(trackmapTrackers.get(0).getDriverIdx()).isEqualTo(63);
                        assertThat(trackmapTrackers.get(0).getDriverCarNbr()).isEqualTo(63);
                        assertThat(trackmapTrackers.get(0).getDriverInitials()).isEqualTo("JO");
                        assertThat(trackmapTrackers.get(0).getDriverDistPct()).isEqualTo(10.0f);

                        assertThat(trackmapTrackers.get(1).getDriverIdx()).isEqualTo(12);
                        assertThat(trackmapTrackers.get(1).getDriverCarNbr()).isEqualTo(12);
                        assertThat(trackmapTrackers.get(1).getDriverInitials()).isEqualTo("DT");
                        assertThat(trackmapTrackers.get(1).getDriverDistPct()).isEqualTo(20.0f);

                        assertThat(trackmapTrackers.get(2).getDriverIdx()).isEqualTo(23);
                        assertThat(trackmapTrackers.get(2).getDriverCarNbr()).isEqualTo(23);
                        assertThat(trackmapTrackers.get(2).getDriverInitials()).isEqualTo("DT");
                        assertThat(trackmapTrackers.get(2).getDriverDistPct()).isEqualTo(30.0f);

                        assertThat(trackmapTrackers.get(3).getDriverIdx()).isEqualTo(49);
                        assertThat(trackmapTrackers.get(3).getDriverCarNbr()).isEqualTo(49);
                        assertThat(trackmapTrackers.get(3).getDriverInitials()).isEqualTo("DF");
                        assertThat(trackmapTrackers.get(3).getDriverDistPct()).isEqualTo(40.0f);
                    })
                    .thenCancel()
                    .verifyThenAssertThat()
                    .hasNotDroppedElements()
                    .hasNotDroppedErrors()
                    .hasNotDiscardedElements();



    }



}
