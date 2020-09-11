package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RadioFrequenciesYaml {

    @JsonProperty
    private String FrequencyNum  = "";
    @JsonProperty
    private String FrequencyName = "";
    @JsonProperty
    private String Priority      = "";
    @JsonProperty
    private String CarIdx        = "";
    @JsonProperty
    private String EntryIdx      = "";
    @JsonProperty
    private String ClubID        = "";
    @JsonProperty
    private String CanScan       = "";
    @JsonProperty
    private String CanSquawk     = "";
    @JsonProperty
    private String Muted         = "";
    @JsonProperty
    private String IsMutable     = "";
    @JsonProperty
    private String IsDeletable   = "";

}
