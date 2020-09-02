package com.joffrey.irsdkjava.sdk.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsFastestLapDto {

    @JsonProperty
    private String CarIdx      = "";
    @JsonProperty
    private String FastestLap  = "";
    @JsonProperty
    private String FastestTime = "";

}
