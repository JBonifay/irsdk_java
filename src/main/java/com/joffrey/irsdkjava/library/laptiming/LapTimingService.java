package com.joffrey.irsdkjava.library.laptiming;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.library.laptiming.model.LapTimingData;
import com.joffrey.irsdkjava.library.utils.LapTimingUtils;
import com.joffrey.irsdkjava.library.yaml.YamlService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
@Service
public class LapTimingService {

    private final YamlService  yamlService;
    private final GameVarUtils gameVarUtilsHelper;

    public final Flux<List<LapTimingData>> listLapTimingDataFlux = Flux.interval(Duration.ofMillis(1))
                                                                       .map(aLong -> loadLapTimingDataList())
                                                                       .cache();

    public Flux<List<LapTimingData>> getLapTimingDataListFlux() {
        return listLapTimingDataFlux;
    }

    /**
     * Get a list of {@link LapTimingData} object filled with each car data
     *
     * @return
     */
    private List<LapTimingData> loadLapTimingDataList() {

        List<LapTimingData> lapTimingDataList = new ArrayList<>();
        int totalCars = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().size();

        for (int i = 0; i < totalCars; i++) {
            lapTimingDataList.add(getLapTimingDataForCarIdx(i));
        }

        return LapTimingUtils.sortLapTimingEntries(lapTimingDataList);
    }


    /**
     * Get lap timing data for a given car idx
     *
     * @param carIdx the car idx
     * @return {@link LapTimingData} filled with values
     */
    private LapTimingData getLapTimingDataForCarIdx(int carIdx) {
        LapTimingData lapTimingData = new LapTimingData();

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarInt("CarIdxPosition", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarInt("CarIdxClassPosition", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarFloat("CarIdxEstTime", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarFloat("CarIdxF2Time", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarInt("CarIdxLap", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarFloat("CarIdxLapDistPct", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarFloat("CarIdxLastLapTime", carIdx)),
                 Mono.just(gameVarUtilsHelper.getVarFloat("CarIdxBestLapTime", carIdx))).map(tuple -> {
            lapTimingData.setCarIdxPosition(tuple.getT1());
            lapTimingData.setCarIdxClassPosition(tuple.getT2());
            lapTimingData.setCarIdxEstTime(tuple.getT3());
            lapTimingData.setCarIdxF2Time(tuple.getT4());
            lapTimingData.setCarIdxLap(tuple.getT5());
            lapTimingData.setCarIdxLapDistPct(tuple.getT6());
            lapTimingData.setCarIdxLastLapTime(tuple.getT7());
            lapTimingData.setCarIdxBestLapTime(tuple.getT8());
            return lapTimingData;
        }).subscribe();

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarInt("CarIdxTrackSurface", carIdx)),
                 Mono.just(yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().get(carIdx))).map(tuple -> {
            lapTimingData.setCarIdx(Integer.parseInt(tuple.getT2().getCarIdx()));
            lapTimingData.setUserName(tuple.getT2().getUserName());
            lapTimingData.setTeamName(tuple.getT2().getTeamName());
            lapTimingData.setCarNumber(tuple.getT2().getCarNumber());
            lapTimingData.setIRating(tuple.getT2().getIRating());
            lapTimingData.setLicLevel(tuple.getT2().getLicLevel());
            lapTimingData.setLicString(tuple.getT2().getLicString());
            lapTimingData.setLicColor(tuple.getT2().getLicColor());
            lapTimingData.setIsSpectator(tuple.getT2().getIsSpectator());
            lapTimingData.setClubName(tuple.getT2().getClubName());
            lapTimingData.setDivisionName(tuple.getT2().getDivisionName());
            return lapTimingData;
        }).subscribe();

        return lapTimingData;
    }


}
