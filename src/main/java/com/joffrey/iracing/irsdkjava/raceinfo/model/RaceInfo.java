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

package com.joffrey.iracing.irsdkjava.raceinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RaceInfo {

    // Track info
    private String trackDisplayName;
    private String trackConfigName;
    private String trackLength;
    private String trackCity;
    private String trackCountry;
    private String eventType;

    // Info on remaining
    private double remainingTime;  // SessionTimeRemain
    private int    remainingLaps;  // SessionLapsRemain
    private float  fuelLevel;      // FuelLevel
    private float  fuelLevelPct;   // FuelLevelPct
    private float  fuelUsePerHour; // FuelUsePerHour

    // Rating
    private int averageSOF;

    // Useful booleans
    private int playerCarIdx;

    @AllArgsConstructor
    @Data
    public static class LiveData {

        // Info on remaining
        private double remainingTime;  // SessionTimeRemain
        private int    remainingLaps;  // SessionLapsRemain
        private float  fuelLevel;      // FuelLevel
        private float  fuelLevelPct;   // FuelLevelPct
        private float  fuelUsePerHour; // FuelUsePerHour

    }

    @AllArgsConstructor
    @Data
    public static class YamlData {

        private String trackDisplayName;
        private String trackConfigName;
        private String trackLength;
        private String trackCity;
        private String trackCountry;
        private String eventType;
        // Rating
        private int    averageSOF;
        // Useful booleans
        private int    playerCarIdx;

    }


}
