package com.joffrey.irsdkjava.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverInfoYaml {

    @JsonProperty
    private String CarIdx                = "";
    @JsonProperty
    private String UserName              = "";
    @JsonProperty
    private String AbbrevName            = "";
    @JsonProperty
    private String Initials              = "";
    @JsonProperty
    private String UserID                = "";
    @JsonProperty
    private String TeamID                = "";
    @JsonProperty
    private String TeamName              = "";
    @JsonProperty
    private String CarNumber             = "";
    @JsonProperty
    private String CarNumberRaw          = "";
    @JsonProperty
    private String CarPath               = "";
    @JsonProperty
    private String CarClassID            = "";
    @JsonProperty
    private String CarID                 = "";
    @JsonProperty
    private String CarIsPaceCar          = "";
    @JsonProperty
    private String CarIsAI               = "";
    @JsonProperty
    private String CarScreenName         = "";
    @JsonProperty
    private String CarScreenNameShort    = "";
    @JsonProperty
    private String CarClassShortName     = "";
    @JsonProperty
    private String CarClassRelSpeed      = "";
    @JsonProperty
    private String CarClassLicenseLevel  = "";
    @JsonProperty
    private String CarClassMaxFuelPct    = "";
    @JsonProperty
    private String CarClassWeightPenalty = "";
    @JsonProperty
    private String CarClassColor         = "";
    @JsonProperty
    private String IRating               = "";
    @JsonProperty
    private String LicLevel              = "";
    @JsonProperty
    private String LicSubLevel           = "";
    @JsonProperty
    private String LicString             = "";
    @JsonProperty
    private String LicColor              = "";
    @JsonProperty
    private String IsSpectator           = "";
    @JsonProperty
    private String CarDesignStr          = "";
    @JsonProperty
    private String HelmetDesignStr       = "";
    @JsonProperty
    private String SuitDesignStr         = "";
    @JsonProperty
    private String CarNumberDesignStr    = "";
    @JsonProperty
    private String CarSponsor_1          = "";
    @JsonProperty
    private String CarSponsor_2          = "";
    @JsonProperty
    private String ClubName              = "";
    @JsonProperty
    private String DivisionName          = "";


}
