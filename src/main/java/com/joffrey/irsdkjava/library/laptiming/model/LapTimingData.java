package com.joffrey.irsdkjava.library.laptiming.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LapTimingData {

    // Yaml Idx
    private int carIdx;

    // Calculated +> Not from iRacing
    private int   carIdxLivePosition;

    // Live data
    private int   carIdxPosition;
    private int   carIdxClassPosition;
    private float carIdxEstTime;
    private float carIdxF2Time;
    private int   carIdxLap;
    private float carIdxLapDistPct;
    private float carIdxLastLapTime;
    private float carIdxBestLapTime;

    // Use this for know if car is in world
    private String carIdxTrackSurface;

    // Player info
    private String userName;
    private String teamName;
    private String carNumber;
    private String iRating;
    private String licLevel;
    private String licString;
    private String licColor;
    private String isSpectator;
    private String clubName;
    private String divisionName;

    @AllArgsConstructor
    @Data
    public static class LiveData {

        private int   carIdxPosition;
        private int   carIdxClassPosition;
        private float carIdxEstTime;
        private float carIdxF2Time;
        private int   carIdxLap;
        private float carIdxLapDistPct;
        private float carIdxLastLapTime;
        private float carIdxBestLapTime;

    }

    @AllArgsConstructor
    @Data
    public static class YamlData {

        // Use this for know if car is in world
        private String carIdxTrackSurface;
        // Player info
        private String userName;
        private String teamName;
        private String carNumber;
        private String iRating;
        private String licLevel;
        private String licString;
        private String licColor;
        private String isSpectator;
        private String clubName;
        private String divisionName;
    }


}
