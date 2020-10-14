package com.joffrey.irsdkjava.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectorsYaml {

    @JsonProperty
    private String SectorNum      = "";
    @JsonProperty
    private String SectorStartPct = "";


}
