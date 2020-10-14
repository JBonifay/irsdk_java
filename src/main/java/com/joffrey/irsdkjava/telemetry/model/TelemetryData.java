package com.joffrey.irsdkjava.telemetry.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TelemetryData {

    // Pedals && Speed
    private float throttle;
    private float brake;
    private float clutch;
    private int   gear;
    private float shiftGrindRPM;
    private float RPM;
    private float speed;

    // Fuel && Angles
    private float fuelLevel;
    private float fuelLevelPct;
    private float fuelUsePerHour;
    private float latAccel;
    private float longAccel;
    private float steeringWheelAngle;

    // Weather
    private float  airPressure;
    private float  airTemp;
    private float  relativeHumidity;
    private String skies;
    private float  trackTemp;
    private float  windDir;
    private float  windVel;
    private String weatherType;

    // Session
    private double sessionTime;
    private double sessionTimeRemain;
    private float  lapBestLapTime;
    private int    lap;
    private float  lapCurrentLapTime;
    private int    lapBestLap;
    private float  lapDistPct;

    @AllArgsConstructor
    @Data
    public static class PedalsAndSpeed {

        // Pedals && Speed
        private float throttle;
        private float brake;
        private float clutch;
        private int   gear;
        private float shiftGrindRPM;
        private float RPM;
        private float speed;
    }

    @AllArgsConstructor
    @Data
    public static class FuelAndAngles {

        private float fuelLevel;
        private float fuelLevelPct;
        private float fuelUsePerHour;
        private float latAccel;
        private float longAccel;
        private float steeringWheelAngle;
    }

    @AllArgsConstructor
    @Data
    public static class Weather {

        private float  airPressure;
        private float  airTemp;
        private float  relativeHumidity;
        private String skies;
        private float  trackTemp;
        private float  windDir;
        private float  windVel;
        private String weatherType;
    }

    @AllArgsConstructor
    @Data
    public static class Session {

        private double sessionTime;
        private double sessionTimeRemain;
        private float  lapBestLapTime;
        private int    lap;
        private float  lapCurrentLapTime;
        private int    lapBestLap;
        private float  lapDistPct;
    }


}
