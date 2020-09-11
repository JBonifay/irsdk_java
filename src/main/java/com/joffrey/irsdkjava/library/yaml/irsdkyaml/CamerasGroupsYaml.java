package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CamerasGroupsYaml {

    @JsonProperty
    private String               GroupNum  = "";
    @JsonProperty
    private String               GroupName = "";
    @JsonProperty
    private String               IsScenic  = "";
    @JsonProperty
    private List<CameraInfoYaml> Cameras;

}
