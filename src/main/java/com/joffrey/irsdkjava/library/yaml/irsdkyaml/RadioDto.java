package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RadioDto {

    @JsonProperty
    private String            RadioNum            = "";
    @JsonProperty
    private String            HopCount            = "";
    @JsonProperty
    private String            NumFrequencies      = "";
    @JsonProperty
    private String            TunedToFrequencyNum = "";
    @JsonProperty
    private String               ScanningIsOn        = "";
    @JsonProperty
    private List<FrequenciesDto> Frequencies;

}
