package com.joffrey.irsdkjava.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionInfoYaml {

    @JsonProperty
    private String            NumSession = "";
    @JsonProperty
    private List<SessionYaml> Sessions;

}
