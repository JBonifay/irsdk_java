package com.joffrey.irsdkjava.library.livedata.model.telemetry;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TelemetryLapTiming {

    private int         lap;
    private List<Float> lapTime = new ArrayList<>();
    private float       lapCurrentLapTime;
    private float       lapBestLap;
    private float       lapBestLapTime;
    private float       lapDeltaToBestLap;
    private float       lapDist;
    private float       lapDistPct;


}
