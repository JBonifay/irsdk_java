package com.joffrey.irsdkjava.library.info;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.library.info.model.RaceInfo;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.WeekendInfoYaml;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
@Service
public class InfoDataService {

    private final YamlService  yamlService;
    private final GameVarUtils gameVarUtils;

    public final Flux<RaceInfo> raceInfoFlux = Flux.interval(Duration.ofMillis(1)).map(aLong -> loadRaceInfo()).cache();

    public Flux<RaceInfo> getRaceInfoFlux() {
        return raceInfoFlux;
    }

    private RaceInfo loadRaceInfo() {
        final RaceInfo raceInfo = new RaceInfo();

        WeekendInfoYaml weekendInfo = yamlService.getIrsdkYamlFileBean().getWeekendInfo();

        Flux.zip(Mono.just(weekendInfo.getTrackDisplayName()),
                 Mono.just(weekendInfo.getTrackConfigName()),
                 Mono.just(weekendInfo.getTrackLength()),
                 Mono.just(weekendInfo.getTrackCity()),
                 Mono.just(weekendInfo.getTrackCountry()),
                 Mono.just(weekendInfo.getEventType())).map(objects -> {
            raceInfo.setTrackDisplayName(objects.getT1());
            raceInfo.setTrackConfigName(objects.getT2());
            raceInfo.setTrackLength(objects.getT3());
            raceInfo.setTrackCity(objects.getT4());
            raceInfo.setTrackCountry(objects.getT5());
            raceInfo.setEventType(objects.getT6());
            return raceInfo;
        }).subscribe();

        Flux.zip(Mono.just(gameVarUtils.getVarDouble("SessionTimeRemain")),
                 Mono.just(gameVarUtils.getVarInt("SessionLapsRemain")),
                 Mono.just(gameVarUtils.getVarFloat("FuelLevel")),
                 Mono.just(gameVarUtils.getVarFloat("FuelLevelPct")),
                 Mono.just(gameVarUtils.getVarFloat("FuelUsePerHour"))).map(objects -> {
            raceInfo.setRemainingTime(objects.getT1());
            raceInfo.setRemainingLaps(objects.getT2());
            raceInfo.setFuelLevel(objects.getT3());
            raceInfo.setFuelLevelPct(objects.getT4());
            raceInfo.setFuelUsePerHour(objects.getT5());
            return raceInfo;
        }).subscribe();

        Mono.just(yamlService.getIrsdkYamlFileBean()
                             .getDriverInfo()
                             .getDrivers()
                             .stream()
                             .mapToInt(value -> Integer.parseInt(value.getIRating()))
                             .average()).map(average -> {
            if (average.isPresent()) {
                raceInfo.setAverageSOF((int) average.getAsDouble());
            }
            raceInfo.setPlayerCarIdx(Integer.parseInt(yamlService.getIrsdkYamlFileBean().getDriverInfo().getDriverCarIdx()));
            return raceInfo;
        }).subscribe();

        Mono.just(yamlService.getIrsdkYamlFileBean().getDriverInfo().getDriverCarIdx()).map(s -> {
            raceInfo.setPlayerCarIdx(Integer.parseInt(s));
            return raceInfo;
        }).subscribe();

        return raceInfo;
    }


}
