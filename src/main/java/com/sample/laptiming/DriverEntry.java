package com.sample.laptiming;

import lombok.Data;

@Data
public class DriverEntry {

    private int    carIdx;
    private int    carClassId;
    private char[] driverName = new char[64];
    private char[] teamName   = new char[64];
    private char[] carNumStr  = new char[10]; // the player car number as a character string so we can handle 001 and other oddities

}
