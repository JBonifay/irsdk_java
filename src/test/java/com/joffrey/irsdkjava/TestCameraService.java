package com.joffrey.irsdkjava;

import static com.joffrey.irsdkjava.YamlHelperTest.createByteBufferYamlFile;
import static com.joffrey.irsdkjava.YamlHelperTest.loadYamlObject;
import static org.assertj.core.api.Assertions.assertThat;

import com.joffrey.irsdkjava.camera.CameraService;
import com.joffrey.irsdkjava.model.Header;
import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.yaml.YamlService;
import com.joffrey.irsdkjava.yaml.irsdkyaml.CamerasGroupsYaml;
import com.joffrey.irsdkjava.yaml.irsdkyaml.YamlFile;
import java.nio.ByteBuffer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class TestCameraService {

    @MockBean
    private SdkStarter  sdkStarter;
    @MockBean
    private Header      header;
    @MockBean
    private YamlService yamlService;

    // Class under test
    private CameraService cameraService;

    // Helpers Objects
    private ByteBuffer byteBufferYamlFile;

    @BeforeEach
    void init() {
        cameraService = new CameraService(sdkStarter, yamlService);
        byteBufferYamlFile = createByteBufferYamlFile("camera/cameras.yml");
        YamlFile yamlFile = loadYamlObject(byteBufferYamlFile);

        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
        Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);
    }

    @DisplayName("getLapTimingDataListFlux() - One Driver: Check if values from MemMapFile and Yaml are ok, values are simulated from a custom yaml, and Mock method calling Memory Mapped File")
    @Test
    void Given_() {

        StepVerifier.create(cameraService.getCameraPacketFlux()).assertNext(cameraPacket -> {
            List<CamerasGroupsYaml> cameraInfoList = cameraPacket.getCameraInfoList();
            assertThat(cameraInfoList.size()).isEqualTo(20);
            assertThat(cameraInfoList.toString()).contains("Nose",
                                                           "Gearbox",
                                                           "Roll Bar",
                                                           "LF Susp",
                                                           "LR Susp",
                                                           "Gyro",
                                                           "RF Susp",
                                                           "RR Susp",
                                                           "Cockpit",
                                                           "Blimp",
                                                           "Chopper",
                                                           "Chase",
                                                           "Far Chase",
                                                           "Rear Chase",
                                                           "Pit Lane",
                                                           "Pit Lane 2",
                                                           "Scenic",
                                                           "TV1",
                                                           "TV2",
                                                           "TV3");


        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements().hasNotDroppedErrors().hasNotDiscardedElements();


    }


}
