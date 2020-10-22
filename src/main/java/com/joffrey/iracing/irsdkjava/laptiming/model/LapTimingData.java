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

package com.joffrey.iracing.irsdkjava.laptiming.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LapTimingData {

    // Yaml Idx
    private int carIdx;

    // Calculated +> Not from iRacing
    private int   carLivePosition;
    private float carIntervalWithPreviousCar;

    // Live data
    private int   carIdxPosition;
    private int   carIdxClassPosition;
    private float carIdxEstTime;
    private float carIdxF2Time;
    private int   carIdxLap;
    private float carIdxLapDistPct;
    private float carIdxLastLapTime;
    private float carIdxBestLapTime;

    private String carIdxTrackSurface; // Use this for know if car is in world
    private String carIsPaceCar;
    private String carIsAI;
    private String userName;
    private String teamName;
    private String carNumber;
    private String iRating;
    private String licLevel;
    private String licString;
    private String licColor;
    private String isSpectator;
    private String clubName;
    private String divisionName;

    @AllArgsConstructor
    @Data
    public static class LiveData {

        private int   carIdxPosition;
        private int   carIdxClassPosition;
        private float carIdxEstTime;
        private float carIdxF2Time;
        private int   carIdxLap;
        private float carIdxLapDistPct;
        private float carIdxLastLapTime;
        private float carIdxBestLapTime;

    }

    @AllArgsConstructor
    @Data
    public static class YamlData {

        private String carIdxTrackSurface; // Use this for know if car is in world
        private String carIsPaceCar;
        private String carIsAI;
        private String userName;
        private String teamName;
        private String carNumber;
        private String iRating;
        private String licLevel;
        private String licString;
        private String licColor;
        private String isSpectator;
        private String clubName;
        private String divisionName;
    }


}
