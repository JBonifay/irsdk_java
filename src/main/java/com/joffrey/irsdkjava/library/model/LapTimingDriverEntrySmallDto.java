package com.joffrey.irsdkjava.library.model;

import lombok.Data;

@Data
public class LapTimingDriverEntrySmallDto {

    private int    driverPos;
    private String driverNum;
    private String driverName;
    private float  driverDelta;
    private float  driverLastLap;
    private float  driverBestLap;
    private String driverIRating;

}
