package com.joffrey.irsdkjava.sdk.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsPositionsDto {

    @JsonProperty
    private String Position      = "";
    @JsonProperty
    private String ClassPosition = "";
    @JsonProperty
    private String CarIdx        = "";
    @JsonProperty
    private String Lap           = "";
    @JsonProperty
    private String Time          = "";
    @JsonProperty
    private String FastestLap    = "";
    @JsonProperty
    private String FastestTime   = "";
    @JsonProperty
    private String LastTime      = "";
    @JsonProperty
    private String LapsLed       = "";
    @JsonProperty
    private String LapsComplete  = "";
    @JsonProperty
    private String LapsDriven    = "";
    @JsonProperty
    private String Incidents     = "";
    @JsonProperty
    private String ReasonOutId   = "";
    @JsonProperty
    private String ReasonOutStr  = "";

}
