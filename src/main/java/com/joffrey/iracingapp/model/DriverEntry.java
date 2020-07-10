package com.joffrey.iracingapp.model;

import lombok.Data;

@Data
public class DriverEntry {

    private int carIdx;
    private int carClassId;
    private String driverName;
    private String teamName;
    private String carNumStr;

}
