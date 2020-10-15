/*
 *
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.joffrey.irsdkjava.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekendInfoYaml {

    @JsonProperty
    private String               TrackName              = "";
    @JsonProperty
    private String               TrackID                = "";
    @JsonProperty
    private String               TrackLength            = "";
    @JsonProperty
    private String               TrackDisplayName       = "";
    @JsonProperty
    private String               TrackDisplayShortName  = "";
    @JsonProperty
    private String               TrackConfigName        = "";
    @JsonProperty
    private String               TrackCity              = "";
    @JsonProperty
    private String               TrackCountry           = "";
    @JsonProperty
    private String               TrackAltitude          = "";
    @JsonProperty
    private String               TrackLatitude          = "";
    @JsonProperty
    private String               TrackLongitude         = "";
    @JsonProperty
    private String               TrackNorthOffset       = "";
    @JsonProperty
    private String               TrackNumTurns          = "";
    @JsonProperty
    private String               TrackPitSpeedLimit     = "";
    @JsonProperty
    private String               TrackType              = "";
    @JsonProperty
    private String               TrackWeatherType       = "";
    @JsonProperty
    private String               TrackSkies             = "";
    @JsonProperty
    private String               TrackSurfaceTemp       = "";
    @JsonProperty
    private String               TrackAirTemp           = "";
    @JsonProperty
    private String               TrackAirPressure       = "";
    @JsonProperty
    private String               TrackWindVel           = "";
    @JsonProperty
    private String               TrackWindDir           = "";
    @JsonProperty
    private String               TrackRelativeHumidity  = "";
    @JsonProperty
    private String               TrackFogLevel          = "";
    @JsonProperty
    private String               TrackCleanup           = "";
    @JsonProperty
    private String               TrackDynamicTrack      = "";
    @JsonProperty
    private String               SeriesID               = "";
    @JsonProperty
    private String               SeasonID               = "";
    @JsonProperty
    private String               SessionID              = "";
    @JsonProperty
    private String               SubSessionID           = "";
    @JsonProperty
    private String               LeagueID               = "";
    @JsonProperty
    private String               Official               = "";
    @JsonProperty
    private String               RaceWeek               = "";
    @JsonProperty
    private String               EventType              = "";
    @JsonProperty
    private String               Category               = "";
    @JsonProperty
    private String               SimMode                = "";
    @JsonProperty
    private String               TeamRacing             = "";
    @JsonProperty
    private String               MinDrivers             = "";
    @JsonProperty
    private String               MaxDrivers             = "";
    @JsonProperty
    private String               DCRuleSet              = "";
    @JsonProperty
    private String               QualifierMustStartRace = "";
    @JsonProperty
    private String               NumCarClasses          = "";
    @JsonProperty
    private String               NumCarTypes            = "";
    @JsonProperty
    private WeekendOptionsYaml   WeekendOptions;
    @JsonProperty
    private TelemetryOptionsYaml TelemetryOptions;

}
