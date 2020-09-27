package com.joffrey.irsdkjava.library.telemetry.model;

import lombok.Data;

@Data
public class TelemetryData {

    private float throttle;
    private float brake;
    private float clutch;
    private int   gear;
    private float shiftGrindRPM;
    private float RPM;
    private float speed;

    private float fuelLevel;
    private float fuelLevelPct;
    private float fuelUsePerHour;

    private int   lap;
    private float lapCurrentLapTime;
    private float lapDistPct;

    private float latAccel;
    private float longAccel;

    private float steeringWheelAngle;

    private float  airPressure;
    private float  airTemp;
    private float  relativeHumidity;
    private String skies;
    private float  trackTemp;
    private float  windDir;
    private float  windVel;
    private String weatherType;

    private double sessionTime;
    private double sessionTimeRemain;

    private int   lapBestLap;
    private float lapBestLapTime;

}
