package com.joffrey.irsdkjava.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.Header;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.laptiming.LapTimingService;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.YamlFile;
import java.io.File;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@Configuration
public class UnitTestConfiguration {

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


    void setupVarsForOneDriver() {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_one_driver.yml");

        Mockito.when(sdkStarter.getVarInt("CarIdxPosition", 0)).thenReturn(4);
        Mockito.when(sdkStarter.getVarInt("CarIdxClassPosition", 0)).thenReturn(4);
        Mockito.when(sdkStarter.getVarFloat("CarIdxEstTime", 0)).thenReturn(6.9038405f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxF2Time", 0)).thenReturn(00.003f);
        Mockito.when(sdkStarter.getVarInt("CarIdxLap", 0)).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLap", 0)).thenReturn(0.06200701f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxLastLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarFloat("CarIdxBestLapTime", 0)).thenReturn(0f);
        Mockito.when(sdkStarter.getVarInt("CarIdxTrackSurface", 0)).thenReturn(0);

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


}
