/*
 *
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.joffrey.iracing.irsdkjava.trackmaptracker;

import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.trackmaptracker.model.TrackmapTrackerDriver;
import com.joffrey.iracing.irsdkjava.yaml.YamlService;
import com.joffrey.iracing.irsdkjava.yaml.irsdkyaml.DriverInfoYaml;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log
@Service
public class TrackmapTrackerService {

    @Value("${irsdkjava.config.flux.interval.trackmap-tracker}")
    private int millis;

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    private final ConnectableFlux<List<TrackmapTrackerDriver>> trackmapTrackerListFlux;

    public TrackmapTrackerService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.trackmapTrackerListFlux = Flux.interval(Duration.ofMillis(millis)).filter(aLong -> sdkStarter.isRunning())
                                           .flatMap(aLong -> loadTrackmapTrackerDataList()).publish();
    }

    public Flux<List<TrackmapTrackerDriver>> getTrackmapTrackerListFlux() {
        return trackmapTrackerListFlux.autoConnect();
    }

    private Flux<List<TrackmapTrackerDriver>> loadTrackmapTrackerDataList() {
        List<DriverInfoYaml> driverInfoYamlList = yamlService.getYamlFile().getDriverInfo().getDrivers();
        return Flux.range(0, driverInfoYamlList.size()).subscribeOn(Schedulers.parallel())
                   .flatMap(idx -> getTrackmapTrackerCarIdx(driverInfoYamlList.get(idx))).buffer(driverInfoYamlList.size());
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
