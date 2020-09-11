package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QualifyResultInfoYaml {

    @JsonProperty
    private String Position      = "";
    @JsonProperty
    private String ClassPosition = "";
    @JsonProperty
    private String CarIdx        = "";
    @JsonProperty
    private String FastestLap    = "";
    @JsonProperty
    private String FastestTime   = "";

}
