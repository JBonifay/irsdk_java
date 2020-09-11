package com.joffrey.irsdkjava.library.livedata.model.telemetry;

import lombok.Data;

@Data
public class TelemetryGauges {

    private float brake;
    private float brakeRaw;
    private float clutch;
    private int   gear;
    private float RPM;
    private float shiftGrindRPM;
    private float shiftIndicatorPct;
    private float shiftPowerPct;
    private float speed;
    private float throttle;
    private float throttleRaw;

}
