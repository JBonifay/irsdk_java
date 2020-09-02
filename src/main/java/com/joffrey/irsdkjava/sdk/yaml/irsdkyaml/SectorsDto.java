package com.joffrey.irsdkjava.sdk.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectorsDto {

    @JsonProperty
    private String SectorNum      = "";
    @JsonProperty
    private String SectorStartPct = "";


}
