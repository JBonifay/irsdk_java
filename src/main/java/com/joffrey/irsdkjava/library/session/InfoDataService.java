package com.joffrey.irsdkjava.library.session;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.session.model.RaceInfo;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.ResultsFastestLapYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.ResultsPositionsYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.SessionYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.WeekendInfoYaml;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class InfoDataService {

    private final SdkStarter   sdkStarter;
    private final YamlService  yamlService;
    private final GameVarUtils gameVarUtils;

    private final RaceInfo        raceInfo = new RaceInfo();
    private       WeekendInfoYaml weekendInfo;

    public RaceInfo getRaceInfo() {
        if (sdkStarter.isConnected()) {

            weekendInfo = yamlService.getIrsdkYamlFileBean().getWeekendInfo();
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

            int averageSOF = yamlService.getIrsdkYamlFileBean()
                                        .getDriverInfo()
                                        .getDrivers()
                                        .stream()
                                        .mapToInt(value -> Integer.parseInt(value.getIRating()))
                                        .sum();

            raceInfo.setAverageSOF(averageSOF);

            SessionYaml sessionYaml = yamlService.getIrsdkYamlFileBean().getSessionInfo().getSessions().get(0);

            ResultsPositionsYaml leaderCar = sessionYaml.getResultsPositions()
                                                        .stream()
                                                        .filter(resultsPositionsYaml -> Integer.parseInt(resultsPositionsYaml.getPosition())
                                                                                        == 1)
                                                        .findFirst()
                                                        .get();

            raceInfo.setLeaderCarIdx(Integer.parseInt(leaderCar.getCarIdx()));
            raceInfo.setLeaderCarName(yamlService.getIrsdkYamlFileBean()
                                                 .getDriverInfo()
                                                 .getDrivers()
                                                 .get(raceInfo.getLeaderCarIdx())
                                                 .getUserName());
            raceInfo.setLeaderCarbestLapTime(Float.parseFloat(leaderCar.getFastestTime()));

            ResultsFastestLapYaml resultsFastestLapYaml = sessionYaml.getResultsFastestLap().get(0);
            raceInfo.setBestLapCarIdx(Integer.parseInt(resultsFastestLapYaml.getCarIdx()));
            raceInfo.setBestLapTime(Float.parseFloat(resultsFastestLapYaml.getFastestTime()));
            raceInfo.setBestLapCarName(yamlService.getIrsdkYamlFileBean()
                                                  .getDriverInfo()
                                                  .getDrivers()
                                                  .get(raceInfo.getBestLapCarIdx())
                                                  .getUserName());
        }

        return raceInfo;
    }


}
