package com.joffrey.irsdkjava.library.model.telemetry;

import lombok.Data;

@Data
public class TelemetryPlayer {

    private int   lap;
    private int   lapBestLap;
    private float lapBestLapTime;
    private float lapCurrentLapTime;
    private float lapDeltaToBestLap;

    private float speed; // m/s
    private float latAccel; // m/s^2
    private float longAccel; // m/s^2
    private float steeringWheelAngle; // rad

    private int playerCarPosition;
    private int raceLaps;
    private int sessionLapsRemain;
    private int sessionTimeRemain;

    private float lapDist;
    private float lapDistPct;

    private float throttle;
    private float brake;                    // %, 0=brake released to 1=max pedal force
    private float clutch;                    // %, 0=disengaged to 1=fully engaged
    private int   gear;                       // -1=reverse 0=neutral 1..n=current gear
    private float RPM;

}
