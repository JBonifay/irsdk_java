package com.joffrey.irsdkjava.library.service.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionYaml {

    @JsonProperty
    private String                      SessionNum              = "";
    @JsonProperty
    private String                      SessionLaps             = "";
    @JsonProperty
    private String                      SessionTime             = "";
    @JsonProperty
    private String                      SessionNumLapsToAvg     = "";
    @JsonProperty
    private String                      SessionType             = "";
    @JsonProperty
    private String                      SessionTrackRubberState = "";
    @JsonProperty
    private List<ResultsPositionsYaml>  ResultsPositions;
    @JsonProperty
    private List<ResultsFastestLapYaml> ResultsFastestLap;
    @JsonProperty
    private String                      ResultsAverageLapTime   = "";
    @JsonProperty
    private String                      ResultsNumCautionFlags  = "";
    @JsonProperty
    private String                      ResultsNumCautionLaps   = "";
    @JsonProperty
    private String                      ResultsNumLeadChanges   = "";
    @JsonProperty
    private String                      ResultsLapsComplete     = "";
    @JsonProperty
    private String                      ResultsOfficial         = "";
}
