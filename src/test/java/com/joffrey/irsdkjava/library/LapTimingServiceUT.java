package com.joffrey.irsdkjava.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.Header;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.laptiming.LapTimingService;
import com.joffrey.irsdkjava.library.laptiming.model.LapTimingData;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.YamlFile;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class LapTimingServiceUT {

    @MockBean
    private SdkStarter  sdkStarter;
    @MockBean
    private Header      header;
    @MockBean
    private YamlService yamlService;

    // Class under test
    LapTimingService lapTimingService;

    // Helpers Objects
    private ByteBuffer byteBufferYamlFile;
    private YamlFile   yamlFile;


    /**
     * Setup all vars for test who needs 1 driver
     */
    void setupVarsForOneDriver() {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_one_driver.yml");

        Mockito.when(sdkStarter.getVarInt("CarIdxPosition", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarInt("CarIdxClassPosition", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxEstTime", 0)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxF2Time", 0)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxLap", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 0)).thenReturn(30.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLastLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxBestLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxTrackSurface", 0)).thenReturn(0);

        setupGeneral();
    }

    /**
     * Setup all vars for test who needs 1 driver
     */
    void setupVarsForTwoDriver() {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_two_driver.yml");

        Mockito.when(sdkStarter.getVarInt("CarIdxPosition", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarInt("CarIdxClassPosition", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxEstTime", 0)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxF2Time", 0)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxLap", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 0)).thenReturn(30.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLastLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxBestLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxTrackSurface", 0)).thenReturn(0);

        Mockito.when(sdkStarter.getVarInt("CarIdxPosition", 1)).thenReturn(2);
        Mockito.when(sdkStarter.getVarInt("CarIdxClassPosition", 1)).thenReturn(2);
        Mockito.when(sdkStarter.getVarFloat("CarIdxEstTime", 1)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxF2Time", 1)).thenReturn(0.0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxLap", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLapDistPct", 1)).thenReturn(40.0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLastLapTime", 1)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxBestLapTime", 1)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxTrackSurface", 1)).thenReturn(0);

        setupGeneral();
    }

    void setupGeneral() {
        lapTimingService = new LapTimingService(sdkStarter, yamlService);
        yamlFile = loadYamlObject();

        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);

    }

    @SneakyThrows
    ByteBuffer createByteBufferYamlFile(String file) {
        File yamlFile = new ClassPathResource(file).getFile();
        return ByteBuffer.wrap(FileCopyUtils.copyToByteArray(yamlFile));
    }

    @SneakyThrows
    YamlFile loadYamlObject() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        String yamlString = new String(byteBufferYamlFile.array());
        yamlString = yamlString.substring(0, yamlString.indexOf("...") + 3);
        return objectMapper.readValue(yamlString, YamlFile.class);
    }

    @DisplayName("Flux with one driver")
    @Test
    void Given_OneDriverWithData_When_SubscribingToLapTimingFlux_Then_ShouldReturnFluxOfOneDriverWithValidData() {
        // Generate Fake data for replace data from MemoryMappedFile
        setupVarsForOneDriver();

        // Launch flux, expect that the flux returned is filled whith data generated with 'setupVarsForOneDriver()'
        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isZero();
            assertThat(lapTimingData.get(0).getUserName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getCarIdxLivePosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxPosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxClassPosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxEstTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxF2Time()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxLap()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxLapDistPct()).isEqualTo(30.0f);
            assertThat(lapTimingData.get(0).getCarIdxLastLapTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxBestLapTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxTrackSurface()).isEqualTo("irsdk_OffTrack");
            assertThat(lapTimingData.get(0).getUserName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getTeamName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getCarNumber()).isEmpty();
            assertThat(lapTimingData.get(0).getIRating()).isEqualTo("876");
            assertThat(lapTimingData.get(0).getLicLevel()).isEqualTo("7");
            assertThat(lapTimingData.get(0).getLicString()).isEqualTo("D 3.41");
            assertThat(lapTimingData.get(0).getLicColor()).isEqualTo("0xfc8a27");
            assertThat(lapTimingData.get(0).getIsSpectator()).isEqualTo("1");
            assertThat(lapTimingData.get(0).getClubName()).isEqualTo("France");
            assertThat(lapTimingData.get(0).getDivisionName()).isEqualTo("Division 1");
        }).thenCancel().log().verify();

    }


}
