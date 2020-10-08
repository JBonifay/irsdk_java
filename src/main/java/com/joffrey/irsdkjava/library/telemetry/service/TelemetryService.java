package com.joffrey.irsdkjava.library.telemetry.service;

import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.telemetry.model.TelemetryData;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
@Service
public class TelemetryService {

    private final SdkStarter sdkStarter;

    public final ConnectableFlux<TelemetryData> telemetryDataFlux = Flux.interval(Duration.ofMillis(1))
                                                                        .map(aLong -> loadTelemetryData())
                                                                        .publish();


    public Flux<TelemetryData> getTelemetryDataFlux() {
        return telemetryDataFlux.autoConnect();
    }

    private TelemetryData loadTelemetryData() {
        final TelemetryData telemetryData = new TelemetryData();

        if (sdkStarter.isRunning()) {

            Flux.zip(Mono.just(sdkStarter.getVarFloat("Throttle")),
                     Mono.just(sdkStarter.getVarFloat("Brake")),
                     Mono.just(sdkStarter.getVarFloat("Clutch")),
                     Mono.just(sdkStarter.getVarInt("Gear")),
                     Mono.just(sdkStarter.getVarFloat("ShiftGrindRPM")),
                     Mono.just(sdkStarter.getVarFloat("RPM")),
                     Mono.just(sdkStarter.getVarFloat("Speed")),
                     Mono.just(sdkStarter.getVarFloat("FuelLevel"))).map(objects -> {
                telemetryData.setThrottle(objects.getT1());
                telemetryData.setBrake(objects.getT2());
                telemetryData.setClutch(objects.getT3());
                telemetryData.setGear(objects.getT4());
                telemetryData.setShiftGrindRPM(objects.getT5());
                telemetryData.setRPM(objects.getT6());
                telemetryData.setSpeed(objects.getT7());
                telemetryData.setFuelLevel(objects.getT8());
                return telemetryData;
            }).subscribe();

            Flux.zip(Mono.just(sdkStarter.getVarFloat("FuelLevelPct")),
                     Mono.just(sdkStarter.getVarFloat("FuelUsePerHour")),
                     Mono.just(sdkStarter.getVarInt("Lap")),
                     Mono.just(sdkStarter.getVarFloat("LapCurrentLapTime")),
                     Mono.just(sdkStarter.getVarFloat("LapDistPct")),
                     Mono.just(sdkStarter.getVarFloat("LatAccel")),
                     Mono.just(sdkStarter.getVarFloat("LongAccel")),
                     Mono.just(sdkStarter.getVarFloat("SteeringWheelAngle"))).map(objects -> {
                telemetryData.setFuelLevelPct(objects.getT1());
                telemetryData.setFuelUsePerHour(objects.getT2());
                telemetryData.setLap(objects.getT3());
                telemetryData.setLapCurrentLapTime(objects.getT4());
                telemetryData.setLapDistPct(objects.getT5());
                telemetryData.setLatAccel(objects.getT6());
                telemetryData.setLongAccel(objects.getT7());
                telemetryData.setSteeringWheelAngle(objects.getT8());
                return telemetryData;
            }).subscribe();

            Flux.zip(Mono.just(sdkStarter.getVarFloat("airPressure")),
                     Mono.just(sdkStarter.getVarFloat("airTemp")),
                     Mono.just(sdkStarter.getVarFloat("relativeHumidity")),
                     Mono.just(sdkStarter.getVarInt("Skies")),
                     Mono.just(sdkStarter.getVarFloat("trackTemp")),
                     Mono.just(sdkStarter.getVarFloat("windDir")),
                     Mono.just(sdkStarter.getVarFloat("windVel")),
                     Mono.just(sdkStarter.getVarInt("WeatherType"))).map(objects -> {
                telemetryData.setAirPressure(objects.getT1());
                telemetryData.setAirTemp(objects.getT2());
                telemetryData.setRelativeHumidity(objects.getT3());

                // Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
                int skies = objects.getT4();
                if (skies == 0) {
                    telemetryData.setSkies("Clear");
                } else if (skies == 1 || skies == 2) {
                    telemetryData.setSkies("Cloudy");
                } else if (skies == 3) {
                    telemetryData.setSkies("Overcast");
                } else {
                    telemetryData.setSkies("");
                }

                telemetryData.setTrackTemp(objects.getT5());
                telemetryData.setWindDir(objects.getT6());
                telemetryData.setWindVel(objects.getT7());

                // Weather type (0=constant 1=dynamic)
                int weatherType = objects.getT8();
                if (weatherType == 0) {
                    telemetryData.setWeatherType("Constant");
                } else if (weatherType == 1) {
                    telemetryData.setWeatherType("Dynamic");
                } else {
                    telemetryData.setWeatherType("");
                }

                return telemetryData;
            }).subscribe();

            Flux.zip(Mono.just(sdkStarter.getVarDouble("SessionTime")),
                     Mono.just(sdkStarter.getVarDouble("SessionTimeRemain")),
                     Mono.just(sdkStarter.getVarInt("LapBestLap")),
                     Mono.just(sdkStarter.getVarFloat("LapBestLapTime"))).map(objects -> {
                telemetryData.setSessionTime(objects.getT1());
                telemetryData.setSessionTimeRemain(objects.getT2());
                telemetryData.setLapBestLap(objects.getT3());
                telemetryData.setLapBestLapTime(objects.getT4());
                return telemetryData;
            }).subscribe();
        }

        return telemetryData;
    }


}
