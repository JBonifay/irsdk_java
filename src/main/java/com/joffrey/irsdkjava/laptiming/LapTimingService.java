package com.joffrey.irsdkjava.laptiming;

import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.model.defines.TrkLoc;
import com.joffrey.irsdkjava.laptiming.model.LapTimingData;
import com.joffrey.irsdkjava.laptiming.model.LapTimingData.LiveData;
import com.joffrey.irsdkjava.laptiming.model.LapTimingData.YamlData;
import com.joffrey.irsdkjava.yaml.YamlService;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Log
@Service
public class LapTimingService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    private final ConnectableFlux<List<LapTimingData>> listLapTimingDataFlux;

    public LapTimingService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.listLapTimingDataFlux = Flux.interval(Duration.ofSeconds(1)).filter(aLong -> sdkStarter.isRunning())
                                         .flatMap(aLong -> loadLapTimingDataList()).publish();
    }

    /**
     * Used to connect to the {@literal Flux<List<LapTimingData>>}
     *
     * @return the {@literal Flux<List<LapTimingData>>} {@link ConnectableFlux}
     */
    public Flux<List<LapTimingData>> getLapTimingDataListFlux() {
        return listLapTimingDataFlux.autoConnect();
    }

    /**
     * Get a list of {@link LapTimingData} object filled with each car data
     */
    private Flux<List<LapTimingData>> loadLapTimingDataList() {
        int totalSize = yamlService.getYamlFile().getDriverInfo().getDrivers().size();
        return Flux.range(0, totalSize).subscribeOn(Schedulers.parallel()).flatMap(this::getLapTimingDataForCarIdx)
                   .sort(getLapTimingDataComparator()).buffer(totalSize).map(this::setDriversNewPosition)
                   .map(this::setDriversInterval);
    }

    /**
     * Get lap timing data for a given car idx
     *
     * @param carIdx the car idx
     * @return {@link LapTimingData} filled with values
     */
    private Flux<LapTimingData> getLapTimingDataForCarIdx(int carIdx) {

        Flux<LapTimingData.LiveData> firstGroup = Flux.zip(Mono.just(sdkStarter.getVarInt("CarIdxPosition", carIdx)),
                                                           Mono.just(sdkStarter.getVarInt("CarIdxClassPosition", carIdx)),
                                                           Mono.just(sdkStarter.getVarFloat("CarIdxEstTime", carIdx)),
                                                           Mono.just(sdkStarter.getVarFloat("CarIdxF2Time", carIdx)),
                                                           Mono.just(sdkStarter.getVarInt("CarIdxLap", carIdx)),
                                                           Mono.just(sdkStarter.getVarFloat("CarIdxLapDistPct", carIdx)),
                                                           Mono.just(sdkStarter.getVarFloat("CarIdxLastLapTime", carIdx)),
                                                           Mono.just(sdkStarter.getVarFloat("CarIdxBestLapTime", carIdx)))
                                                      .map(t -> new LiveData(t.getT1(),
                                                                             t.getT2(),
                                                                             t.getT3(),
                                                                             t.getT4(),
                                                                             t.getT5(),
                                                                             t.getT6(),
                                                                             t.getT7(),
                                                                             t.getT8()));

        Flux<LapTimingData.YamlData> secondGroup = Flux.zip(Mono.just(sdkStarter.getVarInt("CarIdxTrackSurface", carIdx)),
                                                            Mono.just(yamlService.getYamlFile().getDriverInfo().getDrivers()
                                                                                 .get(carIdx)))
                                                       .map(o -> new YamlData(TrkLoc.valueOf(o.getT1()),
                                                                              o.getT2().getCarIsPaceCar(),
                                                                              o.getT2().getCarIsAI(),
                                                                              o.getT2().getUserName(),
                                                                              o.getT2().getTeamName(),
                                                                              o.getT2().getCarNumber(),
                                                                              o.getT2().getIRating(),
                                                                              o.getT2().getLicLevel(),
                                                                              o.getT2().getLicString(),
                                                                              o.getT2().getLicColor(),
                                                                              o.getT2().getIsSpectator(),
                                                                              o.getT2().getClubName(),
                                                                              o.getT2().getDivisionName()));

        return Flux.zip(firstGroup, secondGroup, (liveData, yamlData) -> {
            LapTimingData lapTimingData = new LapTimingData();
            lapTimingData.setCarIdx(carIdx);
            lapTimingData.setCarIdxPosition(liveData.getCarIdxPosition());
            lapTimingData.setCarIdxClassPosition(liveData.getCarIdxClassPosition());
            lapTimingData.setCarIdxEstTime(liveData.getCarIdxEstTime());
            lapTimingData.setCarIdxF2Time(liveData.getCarIdxF2Time());
            lapTimingData.setCarIdxLap(liveData.getCarIdxLap());
            lapTimingData.setCarIdxLapDistPct(liveData.getCarIdxLapDistPct());
            lapTimingData.setCarIdxLastLapTime(liveData.getCarIdxLastLapTime());
            lapTimingData.setCarIdxBestLapTime(liveData.getCarIdxBestLapTime());
            lapTimingData.setCarIdxTrackSurface(yamlData.getCarIdxTrackSurface());
            lapTimingData.setCarIsPaceCar(yamlData.getCarIsPaceCar());
            lapTimingData.setCarIsAI(yamlData.getCarIsAI());
            lapTimingData.setUserName(yamlData.getUserName());
            lapTimingData.setTeamName(yamlData.getTeamName());
            lapTimingData.setCarNumber(yamlData.getCarNumber());
            lapTimingData.setIRating(yamlData.getIRating());
            lapTimingData.setLicLevel(yamlData.getLicLevel());
            lapTimingData.setLicString(yamlData.getLicString());
            lapTimingData.setLicColor(yamlData.getLicColor());
            lapTimingData.setIsSpectator(yamlData.getIsSpectator());
            lapTimingData.setClubName(yamlData.getClubName());
            lapTimingData.setDivisionName(yamlData.getDivisionName());
            return lapTimingData;
        });
    }

    /**
     * Sort {@link LapTimingData} flux for place drivers with pos = 0 at the end Sort by position and DistPct
     *
     * @return a comparator
     */
    private Comparator<LapTimingData> getLapTimingDataComparator() {
        return (o1, o2) -> {
            // Check for drivers track pct
            if (o1.getCarIdxLap() == o2.getCarIdxLap()) {
                if (o1.getCarIdxLapDistPct() < o2.getCarIdxLapDistPct()) {
                    return 1;
                } else if (o1.getCarIdxLapDistPct() == o2.getCarIdxLapDistPct()) {
                    return 0;
                }
            } else {
                // Put all pos = 0 at end of array
                if (o1.getCarIdxLap() > o2.getCarIdxLap()) {
                    return -1;
                } else if (o1.getCarIdxLap() == o2.getCarIdxLap()) {
                    return 0;
                } else {
                    return 1;
                }
            }
            return -1;
        };
    }

    /**
     * After sorting drivers with getLapTimingDataForCarIdx, position are still unordered
     *
     * @param lapTimingData the {@link LapTimingData} list to modify
     * @return a {@link LapTimingData} list modified
     */
    private List<LapTimingData> setDriversNewPosition(List<LapTimingData> lapTimingData) {
        IntStream.range(0, lapTimingData.size()).forEachOrdered(i -> lapTimingData.get(i).setCarLivePosition(i + 1));
        return lapTimingData;
    }

    /**
     * Set the interval between drivers
     * @param lapTimingData the list containing all drivers
     * @return the entry list filled with interval values
     */
    private List<LapTimingData> setDriversInterval(List<LapTimingData> lapTimingData) {
        IntStream.range(0, lapTimingData.size()).forEachOrdered(i -> {
            float time = 0.0f;
            if (i != 0) {
                LapTimingData prevDriver = lapTimingData.get(i - 1);
                time = Math.abs(prevDriver.getCarIdxEstTime() - lapTimingData.get(i).getCarIdxEstTime());
            }
            lapTimingData.get(i).setCarIntervalWithPreviousCar(time);
        });

        return lapTimingData;
    }

}
