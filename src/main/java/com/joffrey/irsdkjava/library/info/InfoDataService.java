package com.joffrey.irsdkjava.library.info;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.library.info.model.RaceInfo;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.ResultsFastestLapYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.ResultsPositionsYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.SessionYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.WeekendInfoYaml;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class InfoDataService {

    private final YamlService  yamlService;
    private final GameVarUtils gameVarUtils;

    @Getter
    private final RaceInfo raceInfo = new RaceInfo();

    @Synchronized
    public void loadRaceInfo() {
        // todo create separated thread for fastest traitment

        WeekendInfoYaml weekendInfo = yamlService.getIrsdkYamlFileBean().getWeekendInfo();
        raceInfo.setTrackDisplayName(weekendInfo.getTrackDisplayName());
        raceInfo.setTrackName(weekendInfo.getTrackName());
        raceInfo.setTrackLength(weekendInfo.getTrackLength());
        raceInfo.setTrackCity(weekendInfo.getTrackCity());
        raceInfo.setTrackCountry(weekendInfo.getTrackCountry());
        raceInfo.setTrackNumTurns(weekendInfo.getTrackNumTurns());
        raceInfo.setEventType(weekendInfo.getEventType());

        raceInfo.setRemainingTime(gameVarUtils.getVarFloat("SessionTimeRemain"));
        raceInfo.setRemainingLaps(gameVarUtils.getVarInt("SessionLapsRemain"));
        raceInfo.setFuelLevel(gameVarUtils.getVarFloat("FuelLevel"));
        raceInfo.setFuelLevelPct(gameVarUtils.getVarFloat("FuelLevelPct"));
        raceInfo.setFuelUsePerHour(gameVarUtils.getVarFloat("FuelUsePerHour"));

        OptionalDouble average = yamlService.getIrsdkYamlFileBean()
                                            .getDriverInfo()
                                            .getDrivers()
                                            .stream()
                                            .mapToInt(value -> Integer.parseInt(value.getIRating()))
                                            .average();
        if (average.isPresent()) {
            raceInfo.setAverageSOF((int) average.getAsDouble());
        }

        SessionYaml sessionYaml = yamlService.getIrsdkYamlFileBean().getSessionInfo().getSessions().get(0);

        Optional<ResultsPositionsYaml> leaderCar = sessionYaml.getResultsPositions()
                                                              .stream()
                                                              .filter(resultsPositionsYaml -> Integer.parseInt(
                                                                      resultsPositionsYaml.getPosition()) == 1)
                                                              .findFirst();

        if (leaderCar.isPresent()) {
            raceInfo.setLeaderCarIdx(Integer.parseInt(leaderCar.get().getCarIdx()));
            raceInfo.setLeaderCarName(yamlService.getIrsdkYamlFileBean()
                                                 .getDriverInfo()
                                                 .getDrivers()
                                                 .get(raceInfo.getLeaderCarIdx())
                                                 .getUserName());
            raceInfo.setLeaderCarbestLapTime(Float.parseFloat(leaderCar.get().getFastestTime()));
        }

        Optional<ResultsPositionsYaml> resultsPositionsYaml = sessionYaml.getResultsPositions()
                                                                         .stream()
                                                                         .filter(resultsPositionsYaml1 -> Float.parseFloat(
                                                                                 resultsPositionsYaml1.getLastTime()) > 0)
                                                                         .min(Comparator.comparing(ResultsPositionsYaml::getLastTime));

        if (resultsPositionsYaml.isPresent()) {
            raceInfo.setFastestLastLapCarIdx(Integer.parseInt(resultsPositionsYaml.get().getCarIdx()));
            raceInfo.setFastestLastLapCarName(yamlService.getIrsdkYamlFileBean()
                                                         .getDriverInfo()
                                                         .getDrivers()
                                                         .get(raceInfo.getFastestLastLapCarIdx())
                                                         .getUserName());
            raceInfo.setFastestLastLapTime(Float.parseFloat(resultsPositionsYaml.get().getLastTime()));
        }

        ResultsFastestLapYaml resultsFastestLapYaml = sessionYaml.getResultsFastestLap().get(0);
        raceInfo.setBestLapCarIdx(Integer.parseInt(resultsFastestLapYaml.getCarIdx()));
        raceInfo.setBestLapTime(Float.parseFloat(resultsFastestLapYaml.getFastestTime()));
        raceInfo.setBestLapCarName(yamlService.getIrsdkYamlFileBean()
                                              .getDriverInfo()
                                              .getDrivers()
                                              .get(raceInfo.getBestLapCarIdx())
                                              .getUserName());
    }


}
