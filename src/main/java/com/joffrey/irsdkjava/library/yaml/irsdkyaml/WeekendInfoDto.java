package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekendInfoDto {

    @JsonProperty
    private String           TrackName = "";
    @JsonProperty
    private String           TrackID = "";
    @JsonProperty
    private String           TrackLength = "";
    @JsonProperty
    private String           TrackDisplayName = "";
    @JsonProperty
    private String           TrackDisplayShortName = "";
    @JsonProperty
    private String           TrackConfigName = "";
    @JsonProperty
    private String           TrackCity = "";
    @JsonProperty
    private String           TrackCountry = "";
    @JsonProperty
    private String           TrackAltitude = "";
    @JsonProperty
    private String           TrackLatitude = "";
    @JsonProperty
    private String           TrackLongitude = "";
    @JsonProperty
    private String           TrackNorthOffset = "";
    @JsonProperty
    private String           TrackNumTurns = "";
    @JsonProperty
    private String           TrackPitSpeedLimit = "";
    @JsonProperty
    private String           TrackType = "";
    @JsonProperty
    private String           TrackWeatherType = "";
    @JsonProperty
    private String           TrackSkies = "";
    @JsonProperty
    private String           TrackSurfaceTemp = "";
    @JsonProperty
    private String           TrackAirTemp = "";
    @JsonProperty
    private String           TrackAirPressure = "";
    @JsonProperty
    private String           TrackWindVel = "";
    @JsonProperty
    private String           TrackWindDir = "";
    @JsonProperty
    private String           TrackRelativeHumidity = "";
    @JsonProperty
    private String           TrackFogLevel = "";
    @JsonProperty
    private String           TrackCleanup = "";
    @JsonProperty
    private String           TrackDynamicTrack = "";
    @JsonProperty
    private String           SeriesID = "";
    @JsonProperty
    private String           SeasonID = "";
    @JsonProperty
    private String           SessionID = "";
    @JsonProperty
    private String           SubSessionID = "";
    @JsonProperty
    private String           LeagueID = "";
    @JsonProperty
    private String           Official = "";
    @JsonProperty
    private String           RaceWeek = "";
    @JsonProperty
    private String           RventType = "";
    @JsonProperty
    private String           Category = "";
    @JsonProperty
    private String           SimMode = "";
    @JsonProperty
    private String           TeamRacing = "";
    @JsonProperty
    private String           MinDrivers = "";
    @JsonProperty
    private String           MaxDrivers = "";
    @JsonProperty
    private String           DCRuleSet = "";
    @JsonProperty
    private String           QualifierMustStartRace = "";
    @JsonProperty
    private String           NumCarClasses = "";
    @JsonProperty
    private String              NumCarTypes = "";
    @JsonProperty
    private WeekendOptionsDto   WeekendOptions;
    @JsonProperty
    private TelemetryOptionsDto TelemetryOptions;

}
