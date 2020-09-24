package com.joffrey.irsdkjava.library.laptiming;

import com.joffrey.irsdkjava.library.laptiming.model.LapTimingData;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriverInfoYaml;
import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.defines.TrkLoc;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LapTimingDataService {

    private final SdkStarter   sdkStarter;
    private final YamlService  yamlService;
    private final GameVarUtils gameVarUtilsHelper;


    /**
     * Get a list of {@link LapTimingData} object filled with each car data
     *
     * @return List<LapTimingData>
     */
    @Synchronized
    public List<LapTimingData> getLapTimingDataList() {
        List<LapTimingData> lapTimingDataList = new ArrayList<>();
        if (sdkStarter.isConnected()) {

            int totalCars = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().size();

            for (int carIdx = 0; carIdx < totalCars; carIdx++) {
                // Check if car is 'PaceCar'
                lapTimingDataList.add(getLapTimingDataForCarIdx(carIdx));
            }

            String playerCarIdx = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDriverCarIdx();
            lapTimingDataList.get(Integer.parseInt(playerCarIdx)).setPlayer(true);

        }

        return lapTimingDataList;
    }


    /**
     * Get lap timing data for a given car idx
     *
     * @param carIdx the car idx
     * @return {@link LapTimingData} filled with values
     */
    @Synchronized
    public LapTimingData getLapTimingDataForCarIdx(int carIdx) {
        LapTimingData lapTimingData = new LapTimingData();
        if (sdkStarter.isConnected()) {

            lapTimingData.setCarIdxPosition(gameVarUtilsHelper.getVarInt("CarIdxPosition", carIdx));
            lapTimingData.setCarIdxClassPosition(gameVarUtilsHelper.getVarInt("CarIdxClassPosition", carIdx));
            lapTimingData.setCarIdxEstTime(gameVarUtilsHelper.getVarFloat("CarIdxEstTime", carIdx));
            lapTimingData.setCarIdxF2Time(gameVarUtilsHelper.getVarFloat("CarIdxF2Time", carIdx));
            lapTimingData.setCarIdxLap(gameVarUtilsHelper.getVarInt("CarIdxLap", carIdx));
            lapTimingData.setCarIdxLapDistPct(gameVarUtilsHelper.getVarFloat("CarIdxLapDistPct", carIdx));

            int sessionNum = gameVarUtilsHelper.getVarInt("SessionNum");
            yamlService.getIrsdkYamlFileBean().getSessionInfo().getSessions().get(sessionNum).getResultsPositions().forEach(resultsPositionsYaml -> {
                if (resultsPositionsYaml.getCarIdx().equals(String.valueOf(carIdx))) {
                    lapTimingData.setCarIdxLastLapTime(Float.parseFloat(resultsPositionsYaml.getLastTime()));
                    lapTimingData.setCarIdxBestLapTime(Float.parseFloat(resultsPositionsYaml.getFastestTime()));
                }
            });


            int carIdxTrackSurface = gameVarUtilsHelper.getVarInt("CarIdxTrackSurface", carIdx);
            lapTimingData.setCarIdxTrackSurface(TrkLoc.valueOf(carIdxTrackSurface));

            DriverInfoYaml driverInfoYaml = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().get(carIdx);
            lapTimingData.setCarIdx(Integer.parseInt(driverInfoYaml.getCarIdx()));
            lapTimingData.setUserName(driverInfoYaml.getUserName());
            lapTimingData.setTeamName(driverInfoYaml.getTeamName());
            lapTimingData.setCarNumber(driverInfoYaml.getCarNumber());
            lapTimingData.setIRating(driverInfoYaml.getIRating());
            lapTimingData.setLicLevel(driverInfoYaml.getLicLevel());
            lapTimingData.setLicString(driverInfoYaml.getLicString());
            lapTimingData.setLicColor(driverInfoYaml.getLicColor());
            lapTimingData.setIsSpectator(driverInfoYaml.getIsSpectator());
            lapTimingData.setClubName(driverInfoYaml.getClubName());
            lapTimingData.setDivisionName(driverInfoYaml.getDivisionName());
        }

        return lapTimingData;
    }


}
