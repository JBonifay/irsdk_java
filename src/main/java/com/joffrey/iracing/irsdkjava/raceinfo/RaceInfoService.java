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

package com.joffrey.iracing.irsdkjava.raceinfo;

import com.joffrey.iracing.irsdkjava.config.FluxProperties;
import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.raceinfo.model.RaceInfo;
import com.joffrey.iracing.irsdkjava.raceinfo.model.RaceInfo.LiveData;
import com.joffrey.iracing.irsdkjava.raceinfo.model.RaceInfo.YamlData;
import com.joffrey.iracing.irsdkjava.yaml.YamlService;
import java.time.Duration;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@Service
public class RaceInfoService {

    private final FluxProperties fluxProperties;
    private final SdkStarter     sdkStarter;
    private final YamlService    yamlService;

    private final Flux<RaceInfo> raceInfoFlux;

    public RaceInfoService(FluxProperties fluxProperties, SdkStarter sdkStarter, YamlService yamlService) {
        this.fluxProperties = fluxProperties;
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.raceInfoFlux =
                Flux.interval(Duration.ofMillis(fluxProperties.getRaceInfoIntervalInMs())).filter(aLong -> sdkStarter.isRunning())
                    .flatMap(aLong -> loadRaceInfo());
    }

    public Flux<RaceInfo> getRaceInfoFlux() {
        return raceInfoFlux;
    }

    private Flux<RaceInfo> loadRaceInfo() {
        Flux<LiveData> firstGroup = Flux.zip(Mono.just(sdkStarter.getVarDouble("SessionTimeRemain")),
                                             Mono.just(sdkStarter.getVarInt("SessionLapsRemain")),
                                             Mono.just(sdkStarter.getVarFloat("FuelLevel")),
                                             Mono.just(sdkStarter.getVarFloat("FuelLevelPct")),
                                             Mono.just(sdkStarter.getVarFloat("FuelUsePerHour")))
                                        .map(o -> new LiveData(o.getT1(), o.getT2(), o.getT3(), o.getT4(), o.getT5()));

        Flux<YamlData> secondGroup = Flux.zip(Mono.just(yamlService.getYamlFile().getWeekendInfo()),
                                              Mono.just(yamlService.getYamlFile().getDriverInfo().getDrivers().stream()
                                                                   .mapToInt(value -> Integer.parseInt(value.getIRating()))
                                                                   .average()),
                                              Mono.just(yamlService.getYamlFile().getDriverInfo().getDriverCarIdx()))
                                         .map(s -> new YamlData(s.getT1().getTrackDisplayName(),
                                                                s.getT1().getTrackConfigName(),
                                                                s.getT1().getTrackLength(),
                                                                s.getT1().getTrackCity(),
                                                                s.getT1().getTrackCountry(),
                                                                s.getT1().getEventType(),
                                                                (int) s.getT2().orElse(0),
                                                                Integer.parseInt(s.getT3())));

        return Flux.zip(firstGroup, secondGroup, (liveData, yamlData) -> {
            RaceInfo raceInfo = new RaceInfo();
            raceInfo.setRemainingTime(liveData.getRemainingTime());
            raceInfo.setRemainingLaps(liveData.getRemainingLaps());
            raceInfo.setFuelLevel(liveData.getFuelLevel());
            raceInfo.setFuelLevelPct(liveData.getFuelLevelPct());
            raceInfo.setFuelUsePerHour(liveData.getFuelUsePerHour());
            raceInfo.setTrackDisplayName(yamlData.getTrackDisplayName());
            raceInfo.setTrackConfigName(yamlData.getTrackConfigName());
            raceInfo.setTrackLength(yamlData.getTrackLength());
            raceInfo.setTrackCity(yamlData.getTrackCity());
            raceInfo.setTrackCountry(yamlData.getTrackCountry());
            raceInfo.setEventType(yamlData.getEventType());
            raceInfo.setAverageSOF(yamlData.getAverageSOF());
            raceInfo.setPlayerCarIdx(yamlData.getPlayerCarIdx());

            return raceInfo;
        });
    }
}
