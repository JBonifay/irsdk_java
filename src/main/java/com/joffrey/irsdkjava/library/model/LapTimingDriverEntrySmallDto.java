package com.joffrey.irsdkjava.library.model;

import lombok.Data;

@Data
public class LapTimingDriverEntrySmallDto {

    private int    driverIndex;
    private int    driverPos;
    private String driverNum;
    private String driverName;
    private String driverDelta;
    private String driverLastLap;
    private String driverBestLap;
    private String driverIRating;

}
