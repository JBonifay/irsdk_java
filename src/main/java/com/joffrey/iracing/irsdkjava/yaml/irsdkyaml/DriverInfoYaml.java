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
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverInfoYaml {

    @JsonProperty
    private String CarIdx                = "";
    @JsonProperty
    private String UserName              = "";
    @JsonProperty
    private String AbbrevName            = "";
    @JsonProperty
    private String Initials              = "";
    @JsonProperty
    private String UserID                = "";
    @JsonProperty
    private String TeamID                = "";
    @JsonProperty
    private String TeamName              = "";
    @JsonProperty
    private String CarNumber             = "";
    @JsonProperty
    private String CarNumberRaw          = "";
    @JsonProperty
    private String CarPath               = "";
    @JsonProperty
    private String CarClassID            = "";
    @JsonProperty
    private String CarID                 = "";
    @JsonProperty
    private String CarIsPaceCar          = "";
    @JsonProperty
    private String CarIsAI               = "";
    @JsonProperty
    private String CarScreenName         = "";
    @JsonProperty
    private String CarScreenNameShort    = "";
    @JsonProperty
    private String CarClassShortName     = "";
    @JsonProperty
    private String CarClassRelSpeed      = "";
    @JsonProperty
    private String CarClassLicenseLevel  = "";
    @JsonProperty
    private String CarClassMaxFuelPct    = "";
    @JsonProperty
    private String CarClassWeightPenalty = "";
    @JsonProperty
    private String CarClassColor         = "";
    @JsonProperty
    private String IRating               = "";
    @JsonProperty
    private String LicLevel              = "";
    @JsonProperty
    private String LicSubLevel           = "";
    @JsonProperty
    private String LicString             = "";
    @JsonProperty
    private String LicColor              = "";
    @JsonProperty
    private String IsSpectator           = "";
    @JsonProperty
    private String CarDesignStr          = "";
    @JsonProperty
    private String HelmetDesignStr       = "";
    @JsonProperty
    private String SuitDesignStr         = "";
    @JsonProperty
    private String CarNumberDesignStr    = "";
    @JsonProperty
    private String CarSponsor_1          = "";
    @JsonProperty
    private String CarSponsor_2          = "";
    @JsonProperty
    private String ClubName              = "";
    @JsonProperty
    private String DivisionName          = "";


}
