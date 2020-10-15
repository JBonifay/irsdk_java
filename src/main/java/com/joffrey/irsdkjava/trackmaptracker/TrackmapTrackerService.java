package com.joffrey.irsdkjava.trackmaptracker;

import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.trackmaptracker.model.TrackmapTrackerDriver;
import com.joffrey.irsdkjava.yaml.YamlService;
import com.joffrey.irsdkjava.yaml.irsdkyaml.DriverInfoYaml;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log
@Service
public class TrackmapTrackerService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    private final ConnectableFlux<List<TrackmapTrackerDriver>> trackmapTrackerListFlux;

    public TrackmapTrackerService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.trackmapTrackerListFlux = Flux.interval(Duration.ofMillis(50)).filter(aLong -> sdkStarter.isRunning())
                                           .flatMap(aLong -> loadTrackmapTrackerDataList()).publish();
    }

    public Flux<List<TrackmapTrackerDriver>> getTrackmapTrackerListFlux() {
        return trackmapTrackerListFlux.autoConnect();
    }

    private Flux<List<TrackmapTrackerDriver>> loadTrackmapTrackerDataList() {
        List<DriverInfoYaml> driverInfoYamlList = yamlService.getYamlFile().getDriverInfo().getDrivers();
        return Flux.range(0,driverInfoYamlList.size())
                   .subscribeOn(Schedulers.parallel())
                   .flatMap(idx -> getTrackmapTrackerCarIdx(driverInfoYamlList.get(idx)))
                   .buffer(driverInfoYamlList.size());
    }

    private Flux<TrackmapTrackerDriver> getTrackmapTrackerCarIdx(DriverInfoYaml driverInfoYaml) {
        return Flux.zip(Mono.just(Integer.parseInt(driverInfoYaml.getCarIdx())),
                        Mono.just(Integer.parseInt(driverInfoYaml.getCarNumber())),
                        Mono.just(Optional.ofNullable(driverInfoYaml.getInitials()).orElse("")),
                        Mono.just(driverInfoYaml.getUserName().substring(0, 2).toUpperCase()),
                        Mono.just(sdkStarter.getVarFloat("CarIdxLapDistPct", Integer.parseInt(driverInfoYaml.getCarIdx()))))
                   .map(o -> new TrackmapTrackerDriver(o.getT1(), o.getT2(), getDriverInitials(o.getT3(), o.getT4()), o.getT5()));
    }

    private String getDriverInitials(String t3, String t4) {
        if (t3 == null || t3.isEmpty()) {
            return t4.substring(0, 2);
        }
        return t3;
    }
}
