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

package com.joffrey.iracing.irsdkjava.yaml.irsdkyaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriversInfoYaml {

    @JsonProperty
    private String               DriverCarIdx            = "";
    @JsonProperty
    private String               DriverHeadPosX          = "";
    @JsonProperty
    private String               DriverHeadPosY          = "";
    @JsonProperty
    private String               DriverHeadPosZ          = "";
    @JsonProperty
    private String               DriverCarIdleRPM        = "";
    @JsonProperty
    private String               DriverCarRedLine        = "";
    @JsonProperty
    private String               DriverCarFuelKgPerLtr   = "";
    @JsonProperty
    private String               DriverCarFuelMaxLtr     = "";
    @JsonProperty
    private String               DriverCarMaxFuelPct     = "";
    @JsonProperty
    private String               DriverCarSLFirstRPM     = "";
    @JsonProperty
    private String               DriverCarSLShiftRPM     = "";
    @JsonProperty
    private String               DriverCarSLLastRPM      = "";
    @JsonProperty
    private String               DriverCarSLBlinkRPM     = "";
    @JsonProperty
    private String               DriverPitTrkPct         = "";
    @JsonProperty
    private String               DriverCarEstLapTime     = "";
    @JsonProperty
    private String               DriverSetupName         = "";
    @JsonProperty
    private String               DriverSetupIsModified   = "";
    @JsonProperty
    private String               DriverSetupLoadTypeName = "";
    @JsonProperty
    private String               DriverSetupPassedTech   = "";
    @JsonProperty
    private List<DriverInfoYaml> Drivers;


}
