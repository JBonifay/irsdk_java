package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YamlFile {

    @JsonProperty
    private WeekendInfoYaml        WeekendInfo;
    @JsonProperty
    private SessionInfoYaml        SessionInfo;
    @JsonProperty
    private QualifyResultsInfoYaml QualifyResultsInfo;
    @JsonProperty
    private CamerasInfoYaml        CameraInfo;
    @JsonProperty
    private RadiosInfoYaml         RadioInfo;
    @JsonProperty
    private DriversInfoYaml        DriverInfo;
    @JsonProperty
    private SplitTimeInfoYaml      SplitTimeInfo;

}
