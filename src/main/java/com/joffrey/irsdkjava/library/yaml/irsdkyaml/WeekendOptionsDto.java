package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekendOptionsDto {

    @JsonProperty
    private String NumStarters         = "";
    @JsonProperty
    private String StartingGrid        = "";
    @JsonProperty
    private String QualifyScoring      = "";
    @JsonProperty
    private String CourseCautions      = "";
    @JsonProperty
    private String StandingStart       = "";
    @JsonProperty
    private String Restarts            = "";
    @JsonProperty
    private String WeatherType         = "";
    @JsonProperty
    private String Skies               = "";
    @JsonProperty
    private String WindDirection       = "";
    @JsonProperty
    private String WindSpeed           = "";
    @JsonProperty
    private String WeatherTemp         = "";
    @JsonProperty
    private String RelativeHumidity    = "";
    @JsonProperty
    private String FogLevel            = "";
    @JsonProperty
    private String Unofficial          = "";
    @JsonProperty
    private String CommercialMode      = "";
    @JsonProperty
    private String NightMode           = "";
    @JsonProperty
    private String IsFixedSetup        = "";
    @JsonProperty
    private String StrictLapsChecking  = "";
    @JsonProperty
    private String HasOpenRegistration = "";

}
