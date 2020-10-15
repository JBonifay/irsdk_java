package com.joffrey.irsdkjava.camera;

import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.camera.model.CameraPacket;
import com.joffrey.irsdkjava.yaml.YamlService;
import java.time.Duration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CameraService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    private final Flux<CameraPacket> cameraPacketFlux;

    public CameraService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.cameraPacketFlux = Flux.interval(Duration.ofSeconds(1))
                                    .filter(aLong -> sdkStarter.isRunning())
                                    .flatMap(aLong -> loadCameraData());
    }

    public Flux<CameraPacket> getCameraPacketFlux() {
        return cameraPacketFlux;
    }

    private Flux<CameraPacket> loadCameraData() {
        return Flux.zip(Mono.just(yamlService.getYamlFile().getCameraInfo().getGroups()),
                        Mono.just(yamlService.getYamlFile().getDriverInfo().getDrivers()))
                   .map(objects -> new CameraPacket(objects.getT1(), objects.getT2()));
    }
}
