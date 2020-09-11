package com.joffrey.irsdkjava.library.livedata.model.telemetry;

import lombok.Data;

@Data
public class TelemetryFuel {

    private float fuelLevel;
    private float fuelLevelPct;
    private float fuelPress;
    private float fuelUsePerHour;


}
