package com.joffrey.irsdkjava.library.trackmaptracker;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.trackmaptracker.model.TrackmapTracker;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriverInfoYaml;
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

    // Average 12ms
    public final ConnectableFlux<List<TrackmapTracker>> trackmapTrackerListFlux;

    public TrackmapTrackerService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.trackmapTrackerListFlux = Flux.interval(Duration.ofMillis(50)).filter(aLong -> sdkStarter.isRunning())
                                           .flatMap(aLong -> loadTrackmapTrackerDataList()).publish();
    }

    public Flux<List<TrackmapTracker>> getTrackmapTrackerListFlux() {
        return trackmapTrackerListFlux.autoConnect();
    }

    private Flux<List<TrackmapTracker>> loadTrackmapTrackerDataList() {
        List<DriverInfoYaml> driverInfoYamlList = yamlService.getYamlFile().getDriverInfo().getDrivers();
        return Flux.range(0,driverInfoYamlList.size())
                   .subscribeOn(Schedulers.parallel())
                   .flatMap(idx -> getTrackmapTrackerCarIdx(driverInfoYamlList.get(idx)))
                   .buffer(driverInfoYamlList.size());
    }

    private Flux<TrackmapTracker> getTrackmapTrackerCarIdx(DriverInfoYaml driverInfoYaml) {
        return Flux.zip(Mono.just(Integer.parseInt(driverInfoYaml.getCarIdx())),
                        Mono.just(Integer.parseInt(driverInfoYaml.getCarNumber())),
                        Mono.just(Optional.ofNullable(driverInfoYaml.getInitials()).orElse("")),
                        Mono.just(driverInfoYaml.getUserName().substring(0, 2).toUpperCase()),
                        Mono.just(sdkStarter.getVarFloat("CarIdxLapDistPct", Integer.parseInt(driverInfoYaml.getCarIdx()))))
                   .map(o -> new TrackmapTracker(o.getT1(), o.getT2(), switch (o.getT3()) {
                       case "" -> o.getT4();
                       default -> o.getT3();
                   }, o.getT5()));
    }
}