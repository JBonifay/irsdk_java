package com.joffrey.irsdkjava.library.service.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraInfoYaml {

    @JsonProperty
    private String CameraNum  = "";
    @JsonProperty
    private String CameraName = "";

}
