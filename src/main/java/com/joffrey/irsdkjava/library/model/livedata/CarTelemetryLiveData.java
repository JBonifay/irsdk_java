/*
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
 */

package com.joffrey.irsdkjava.library.model.livedata;

import lombok.Data;

@Data
public class CarTelemetryLiveData {

    private float    brake;
    private float    brakeRaw;
    private float    clutch;
    private boolean  driverMarker;
    private String[] engineWarnings;
    private float    fuelLevel;
    private float    fuelLevelPct;
    private float    fuelPress;
    private float    fuelUsePerHour;
    private int      gear;
    private boolean  isInGarage;
    private boolean  isOnTrack;
    private boolean  isOnTrackCar;
    private int      lap;
    private int      lapBestLap;
    private float    lapBestLapTime;
    private int      lapBestNLapLap;
    private float    lapBestNLapTime;
    private float    lapCurrentLapTime;
    private float    lapDeltaToBestLap;
    private float    lapDeltaToBestLap_DD;
    private boolean  lapDeltaToBestLap_OK;
    private float    lapDeltaToOptimalLap;
    private float    lapDeltaToOptimalLap_DD;
    private boolean  lapDeltaToOptimalLap_OK;
    private float    lapDeltaToSessionBestLap;
    private float    lapDeltaToSessionBestLap_DD;
    private boolean  lapDeltaToSessionBestLap_OK;
    private float    lapDeltaToSessionLastlLap;
    private float    lapDeltaToSessionLastlLap_DD;
    private boolean  lapDeltaToSessionLastlLap_OK;
    private float    lapDeltaToSessionOptimalLap;
    private float    lapDeltaToSessionOptimalLap_DD;
    private boolean  lapDeltaToSessionOptimalLap_OK;
    private float    lapDist;
    private float    lapDistPct;
    private int      lapLasNLapSeq;
    private float    lapLastLapTime;
    private float    lapLastNLapTime;
    private double   lat;
    private float    latAccel;
    private double   lon;
    private float    lonAccel;
    private float    manifoldPress;
    private float    oilLevel;
    private float    oilPress;
    private float    oilTemp;
    private boolean  onPitRoad;
    private float    pitch;
    private float    pitchRate;
    private float    pitOptRepairLeft;
    private float    pitRepairLeft;
    private String[] pitSvFlags;
    private float    pitSvFuel;
    private float    pitSvLFP;
    private float    pitSvLRP;
    private float    pitSvRFP;
    private float    pitSvRRP;
    private int      playerCarClassPosition;
    private int      playerCarPosition;
    private int      raceLaps;
    private int      radioTransmitCarIdx;
    private int      radioTransmitFrequencyIdx;
    private int      radioTransmitRadioIdx;
    private float    roll;
    private float    rollRate;
    private float    RPM;
    private float    shiftGrindRPM;
    private float    shiftIndicatorPct;
    private float    shiftPowerPct;
    private float    speed;
    private float    throttle;
    private float    throttleRaw;
    private float    velocityX;
    private float    velocityY;
    private float    velocityZ;
    private float    vertAccel;
    private float    voltage;
    private float    waterLevel;
    private float    waterTemp;
    private float    yaw;
    private float    yawNorth;
    private float    yawRate;


}
