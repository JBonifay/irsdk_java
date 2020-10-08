package com.joffrey.irsdkjava.library.trackmaptracker;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.trackmaptracker.model.TrackmapTracker;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriverInfoYaml;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Log
@Service
public class TrackmapTrackerService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    // Average 12ms
    public final ConnectableFlux<List<TrackmapTracker>> trackmapTrackerListFlux = Flux.interval(Duration.ofMillis(50))
                                                                                      .map(aLong -> loadTrackmapTrackerDataList())
                                                                                      .publish();

    public Flux<List<TrackmapTracker>> getTrackmapTrackerListFlux() {
        return trackmapTrackerListFlux.autoConnect();
    }

    private List<TrackmapTracker> loadTrackmapTrackerDataList() {
        List<TrackmapTracker> trackmapTrackerList = new ArrayList<>();

        if (sdkStarter.isRunning()) {

            List<DriverInfoYaml> drivers = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers();

            ParallelFlux.from(Flux.fromStream(drivers.stream())
                                  .map(driverInfoYaml -> trackmapTrackerList.add(getTrackmapTrackerCarIdx(driverInfoYaml))))
                        .runOn(Schedulers.parallel())
                        .subscribe();
        }

        return trackmapTrackerList;
    }

    private TrackmapTracker getTrackmapTrackerCarIdx(DriverInfoYaml driverInfoYaml) {
        TrackmapTracker trackmapTracker = new TrackmapTracker();

        Flux.zip(Mono.just(Integer.parseInt(driverInfoYaml.getCarIdx())),
                 Mono.just(Integer.parseInt(driverInfoYaml.getCarNumber())),
                 Mono.just(Optional.ofNullable(driverInfoYaml.getInitials()).orElse("")),
                 Mono.just(driverInfoYaml.getUserName().substring(0, 2).toUpperCase())).map(objects -> {
            trackmapTracker.setDriverIdx(objects.getT1());
            trackmapTracker.setDriverCarNbr(objects.getT2());

            String initials = objects.getT3();
            if (initials.isEmpty()) {
                initials = objects.getT4();
            }
            trackmapTracker.setDriverInitials(initials);

            return trackmapTracker;
        }).subscribe();

        Mono.just(sdkStarter.getVarFloat("CarIdxLapDistPct", Integer.parseInt(driverInfoYaml.getCarIdx()))).map(aFloat -> {
            trackmapTracker.setDriverDistPct(aFloat);
            return trackmapTracker;
        }).subscribe();

        return trackmapTracker;
    }
}
