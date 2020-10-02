package com.joffrey.irsdkjava.library.telemetry.service;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.library.telemetry.model.TelemetryData;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
@Service
public class TelemetryService {

    private final GameVarUtils gameVarUtilsHelper;

    public final Flux<TelemetryData> telemetryDataFlux = Flux.interval(Duration.ofMillis(1))
                                                             .map(aLong -> loadTelemetryData())
                                                             .cache();

    public Flux<TelemetryData> getTelemetryDataFlux() {
        return telemetryDataFlux;
    }

    private TelemetryData loadTelemetryData() {
        final TelemetryData telemetryData = new TelemetryData();

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarFloat("Throttle")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("Brake")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("Clutch")),
                 Mono.just(gameVarUtilsHelper.getVarInt("Gear")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("ShiftGrindRPM")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("RPM")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("Speed")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("FuelLevel"))).map(objects -> {
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

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarFloat("FuelLevelPct")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("FuelUsePerHour")),
                 Mono.just(gameVarUtilsHelper.getVarInt("Lap")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("LapCurrentLapTime")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("LapDistPct")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("LatAccel")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("LongAccel")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("SteeringWheelAngle"))).map(objects -> {
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

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarFloat("airPressure")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("airTemp")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("relativeHumidity")),
                 Mono.just(gameVarUtilsHelper.getVarInt("Skies")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("trackTemp")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("windDir")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("windVel")),
                 Mono.just(gameVarUtilsHelper.getVarInt("WeatherType"))).map(objects -> {
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

        Flux.zip(Mono.just(gameVarUtilsHelper.getVarDouble("SessionTime")),
                 Mono.just(gameVarUtilsHelper.getVarDouble("SessionTimeRemain")),
                 Mono.just(gameVarUtilsHelper.getVarInt("LapBestLap")),
                 Mono.just(gameVarUtilsHelper.getVarFloat("LapBestLapTime"))).map(objects -> {
            telemetryData.setSessionTime(objects.getT1());
            telemetryData.setSessionTimeRemain(objects.getT2());
            telemetryData.setLapBestLap(objects.getT3());
            telemetryData.setLapBestLapTime(objects.getT4());
            return telemetryData;
        }).subscribe();

        return telemetryData;
    }


}
