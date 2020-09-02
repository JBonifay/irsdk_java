package com.joffrey.irsdkjava.sdk.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.CameraInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.DriverInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.QualifyResultsInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.RadioInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.SessionInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.SplitTimeInfoDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.WeekendInfoDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IrsdkYamlFileDto {

    @JsonProperty
    private WeekendInfoDto        WeekendInfo;
    @JsonProperty
    private SessionInfoDto        SessionInfo;
    @JsonProperty
    private QualifyResultsInfoDto QualifyResultsInfo;
    @JsonProperty
    private CameraInfoDto         CameraInfo;
    @JsonProperty
    private RadioInfoDto          RadioInfo;
    @JsonProperty
    private DriverInfoDto         DriverInfo;
    @JsonProperty
    private SplitTimeInfoDto      SplitTimeInfo;

}
