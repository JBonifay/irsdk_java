package com.joffrey.irsdkjava.library.info;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.info.model.RaceInfo;
import com.joffrey.irsdkjava.library.info.model.RaceInfo.LiveData;
import com.joffrey.irsdkjava.library.info.model.RaceInfo.YamlData;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import java.time.Duration;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log
@Service
public class InfoDataService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    public final Flux<RaceInfo> raceInfoFlux;

    public InfoDataService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.raceInfoFlux =
                Flux.interval(Duration.ofSeconds(1)).filter(aLong -> sdkStarter.isRunning()).flatMap(aLong -> loadRaceInfo());
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
