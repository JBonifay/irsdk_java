package com.joffrey.irsdkjava.library.info.model;

import lombok.Data;

@Data
public class RaceInfo {

    // Track info
    private String trackDisplayName;
    private String trackName;
    private String trackLength;
    private String trackCity;
    private String trackCountry;
    private String trackNumTurns;
    private String eventType;

    // Info on remaining
    private float remainingTime;  // SessionTimeRemain
    private int   remainingLaps;  // SessionLapsRemain
    private float fuelLevel;      // FuelLevel
    private float fuelLevelPct;   // FuelLevelPct
    private float fuelUsePerHour; // FuelUsePerHour

    // Rating
    private int averageSOF;

    // Leader car
    private int    leaderCarIdx;
    private String leaderCarName;
    private float  leaderCarbestLapTime;

    // Fastest Last lap car
    private int    fastestLastLapCarIdx;
    private String fastestLastLapCarName;
    private float  fastestLastLapTime;

    // Fastest car
    private int    bestLapCarIdx;
    private String bestLapCarName;
    private float  bestLapTime;

    // Useful booleans
    private int playerCarIdx;

}
