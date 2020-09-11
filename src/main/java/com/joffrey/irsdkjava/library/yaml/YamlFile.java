package com.joffrey.irsdkjava.library.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.CamerasInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriversInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.QualifyResultsInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.RadiosInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.SessionInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.SplitTimeInfoYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.WeekendInfoYaml;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YamlFile {

    @JsonProperty
    private WeekendInfoYaml WeekendInfo;
    @JsonProperty
    private SessionInfoYaml SessionInfo;
    @JsonProperty
    private QualifyResultsInfoYaml QualifyResultsInfo;
    @JsonProperty
    private CamerasInfoYaml CameraInfo;
    @JsonProperty
    private RadiosInfoYaml  RadioInfo;
    @JsonProperty
    private DriversInfoYaml DriverInfo;
    @JsonProperty
    private SplitTimeInfoYaml SplitTimeInfo;

}
