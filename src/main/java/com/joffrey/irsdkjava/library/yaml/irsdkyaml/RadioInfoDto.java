package com.joffrey.irsdkjava.library.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RadioInfoDto {

    @JsonProperty
    private String         SelectedRadioNum = "";
    @JsonProperty
    private List<RadioDto> Radio;

}
