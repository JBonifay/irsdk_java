package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriversInfoYaml {

    @JsonProperty
    private String               DriverCarIdx            = "";
    @JsonProperty
    private String               DriverHeadPosX          = "";
    @JsonProperty
    private String               DriverHeadPosY          = "";
    @JsonProperty
    private String               DriverHeadPosZ          = "";
    @JsonProperty
    private String               DriverCarIdleRPM        = "";
    @JsonProperty
    private String               DriverCarRedLine        = "";
    @JsonProperty
    private String               DriverCarFuelKgPerLtr   = "";
    @JsonProperty
    private String               DriverCarFuelMaxLtr     = "";
    @JsonProperty
    private String               DriverCarMaxFuelPct     = "";
    @JsonProperty
    private String               DriverCarSLFirstRPM     = "";
    @JsonProperty
    private String               DriverCarSLShiftRPM     = "";
    @JsonProperty
    private String               DriverCarSLLastRPM      = "";
    @JsonProperty
    private String               DriverCarSLBlinkRPM     = "";
    @JsonProperty
    private String               DriverPitTrkPct         = "";
    @JsonProperty
    private String               DriverCarEstLapTime     = "";
    @JsonProperty
    private String               DriverSetupName         = "";
    @JsonProperty
    private String               DriverSetupIsModified   = "";
    @JsonProperty
    private String               DriverSetupLoadTypeName = "";
    @JsonProperty
    private String               DriverSetupPassedTech   = "";
    @JsonProperty
    private List<DriverInfoYaml> Drivers;


}
