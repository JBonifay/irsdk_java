package com.joffrey.irsdkjava.sdk.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QualifyResultsInfoDto {

    @JsonProperty
    private List<ResultsDto> Results;

}
