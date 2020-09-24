package com.joffrey.irsdkjava.library.laptiming;

import com.joffrey.irsdkjava.library.laptiming.model.LapTimingData;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriverInfoYaml;
import com.joffrey.irsdkjava.sdk.GameVarUtils;
import com.joffrey.irsdkjava.sdk.SdkStarter;
import com.joffrey.irsdkjava.sdk.defines.TrkLoc;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public List<LapTimingData> getLapTimingDataList() {

        List<LapTimingData> lapTimingDataList = new ArrayList<>();

        int totalCars = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().size();

        for (int i = 0; i < totalCars; i++) {
            lapTimingDataList.add(getLapTimingDataForCarIdx(i));
        }

        String playerCarIdx = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDriverCarIdx();
        lapTimingDataList.get(Integer.parseInt(playerCarIdx)).setPlayer(true);

        return lapTimingDataList;
    }


    /**
     * Get lap timing data for a given car idx
     *
     * @param carIdx the car idx
     * @return {@link LapTimingData} filled with values
     */
    public LapTimingData getLapTimingDataForCarIdx(int carIdx) {
        LapTimingData lapTimingData = new LapTimingData();

        lapTimingData.setCarIdx(gameVarUtilsHelper.getVarInt("CarIdx", carIdx));
        lapTimingData.setCarIdxPosition(gameVarUtilsHelper.getVarInt("CarIdxPosition", carIdx));
        lapTimingData.setCarIdxClassPosition(gameVarUtilsHelper.getVarInt("CarIdxClassPosition", carIdx));
        lapTimingData.setCarIdxEstTime(gameVarUtilsHelper.getVarInt("CarIdxEstTime", carIdx));
        lapTimingData.setCarIdxF2Time(gameVarUtilsHelper.getVarInt("CarIdxF2Time", carIdx));
        lapTimingData.setCarIdxLap(gameVarUtilsHelper.getVarInt("CarIdxLap", carIdx));
        lapTimingData.setCarIdxLapDistPct(gameVarUtilsHelper.getVarInt("CarIdxLapDistPct", carIdx));
        lapTimingData.setCarIdxOnPitRoad(gameVarUtilsHelper.getVarBoolean("CarIdxOnPitRoad", carIdx));
        lapTimingData.setCarIdxLastLapTime(gameVarUtilsHelper.getVarInt("CarIdxLastLapTime", carIdx));
        lapTimingData.setCarIdxBestLapTime(gameVarUtilsHelper.getVarInt("CarIdxBestLapTime", carIdx));

        int carIdxTrackSurface = gameVarUtilsHelper.getVarInt("CarIdxTrackSurface", carIdx);
        lapTimingData.setCarIdxTrackSurface(TrkLoc.valueOf(carIdxTrackSurface));

        DriverInfoYaml driverInfoYaml = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().get(carIdx);
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

        return lapTimingData;
    }


}
