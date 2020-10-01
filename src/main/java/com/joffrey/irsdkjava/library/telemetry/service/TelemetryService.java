package com.joffrey.irsdkjava.library.telemetry.service;

import com.joffrey.irsdkjava.GameVarUtils;
import com.joffrey.irsdkjava.library.telemetry.model.TelemetryData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelemetryService {

    private final GameVarUtils gameVarUtilsHelper;

    private final TelemetryData telemetryData = new TelemetryData();

    public TelemetryData loadTelemetryData() {
        telemetryData.setThrottle(gameVarUtilsHelper.getVarFloat("Throttle"));
        telemetryData.setBrake(gameVarUtilsHelper.getVarFloat("Brake"));
        telemetryData.setClutch(gameVarUtilsHelper.getVarFloat("Clutch"));
        telemetryData.setGear(gameVarUtilsHelper.getVarInt("Gear"));
        telemetryData.setShiftGrindRPM(gameVarUtilsHelper.getVarFloat("ShiftGrindRPM"));
        telemetryData.setRPM(gameVarUtilsHelper.getVarFloat("RPM"));
        telemetryData.setSpeed(gameVarUtilsHelper.getVarFloat("Speed"));

        telemetryData.setFuelLevel(gameVarUtilsHelper.getVarFloat("FuelLevel"));
        telemetryData.setFuelLevelPct(gameVarUtilsHelper.getVarFloat("FuelLevelPct"));
        telemetryData.setFuelUsePerHour(gameVarUtilsHelper.getVarFloat("FuelUsePerHour"));

        telemetryData.setLap(gameVarUtilsHelper.getVarInt("Lap"));
        telemetryData.setLapCurrentLapTime(gameVarUtilsHelper.getVarFloat("LapCurrentLapTime"));
        telemetryData.setLapDistPct(gameVarUtilsHelper.getVarFloat("LapDistPct"));

        telemetryData.setLatAccel(gameVarUtilsHelper.getVarFloat("LatAccel"));
        telemetryData.setLongAccel(gameVarUtilsHelper.getVarFloat("LongAccel"));

        telemetryData.setSteeringWheelAngle(gameVarUtilsHelper.getVarFloat("SteeringWheelAngle"));

        telemetryData.setAirPressure(gameVarUtilsHelper.getVarFloat("airPressure"));
        telemetryData.setAirTemp(gameVarUtilsHelper.getVarFloat("airTemp"));
        telemetryData.setRelativeHumidity(gameVarUtilsHelper.getVarFloat("relativeHumidity"));

        // Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
        int skies = gameVarUtilsHelper.getVarInt("Skies");
        if (skies == 0) {
            telemetryData.setSkies("Clear");
        } else if (skies == 1 || skies == 2) {
            telemetryData.setSkies("Cloudy");
        } else if (skies == 3) {
            telemetryData.setSkies("Overcast");
        } else {
            telemetryData.setSkies("");
        }

        telemetryData.setTrackTemp(gameVarUtilsHelper.getVarFloat("trackTemp"));
        telemetryData.setWindDir(gameVarUtilsHelper.getVarFloat("windDir"));
        telemetryData.setWindVel(gameVarUtilsHelper.getVarFloat("windVel"));

        // Weather type (0=constant 1=dynamic)
        int weatherType = gameVarUtilsHelper.getVarInt("WeatherType");
        if (weatherType == 0) {
            telemetryData.setWeatherType("Constant");
        } else if (weatherType == 1) {
            telemetryData.setWeatherType("Dynamic");
        } else {
            telemetryData.setWeatherType("");
        }

        telemetryData.setSessionTime(gameVarUtilsHelper.getVarDouble("SessionTime"));
        telemetryData.setSessionTimeRemain(gameVarUtilsHelper.getVarDouble("SessionTimeRemain"));
        telemetryData.setLapBestLap(gameVarUtilsHelper.getVarInt("LapBestLap"));
        telemetryData.setLapBestLapTime(gameVarUtilsHelper.getVarFloat("LapBestLapTime"));

        return telemetryData;
    }


}
