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

package com.joffrey.irsdkjava.library.service;

import com.joffrey.irsdkjava.library.GameVarService;
import com.joffrey.irsdkjava.library.model.livedata.CameraLiveData;
import com.joffrey.irsdkjava.library.model.livedata.CarLiveData;
import com.joffrey.irsdkjava.library.model.livedata.CarSetupLiveData;
import com.joffrey.irsdkjava.library.model.livedata.CarTelemetryLiveData;
import com.joffrey.irsdkjava.library.model.livedata.ConfigLiveData;
import com.joffrey.irsdkjava.library.model.livedata.ReplayLiveData;
import com.joffrey.irsdkjava.library.model.livedata.SessionLiveData;
import com.joffrey.irsdkjava.library.model.livedata.SteeringWheelLiveData;
import com.joffrey.irsdkjava.library.model.livedata.TeamLiveData;
import com.joffrey.irsdkjava.library.model.livedata.TrackLiveData;
import com.joffrey.irsdkjava.sdk.defines.CameraState;
import com.joffrey.irsdkjava.sdk.defines.SessionState;
import com.joffrey.irsdkjava.sdk.defines.TrkSurf;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class LiveDataService {

    private static final int            MAX_CAR = 64;
    private final        GameVarService gameVarService;

    public CameraLiveData getCameraLiveData() {
        CameraLiveData cameraLiveData = new CameraLiveData();
        cameraLiveData.setCamCameraNumber(gameVarService.getVarInt("CamCameraNumber"));

        gameVarService.getVarInt("CamCameraState");
        // TODO: 2 Sep 2020 Bitfield test
        CameraState cameraState = CameraState.irsdk_IsSessionScreen;
        cameraLiveData.setCamCameraState(cameraState);
        cameraLiveData.setCamCarIdx(gameVarService.getVarInt("CamCarIdx"));
        cameraLiveData.setCamGroupNumber(gameVarService.getVarInt("CamGroupNumber"));
        return cameraLiveData;
    }

    public List<CarLiveData> getCarLiveDataList() {
        List<CarLiveData> carLiveDataList = new ArrayList<>();

        for (int carIdx = 0; carIdx < MAX_CAR; carIdx++) {
            CarLiveData carLiveData = new CarLiveData();

            carLiveData.setCarIdxClassPosition(gameVarService.getVarInt("CarIdxClassPosition", carIdx));
            carLiveData.setCarIdxEstTime(gameVarService.getVarFloat("CarIdxEstTime", carIdx));
            carLiveData.setCarIdxF2Time(gameVarService.getVarFloat("CarIdxF2Time", carIdx));
            carLiveData.setCarIdxGear(gameVarService.getVarInt("CarIdxGear", carIdx));
            carLiveData.setCarIdxLap(gameVarService.getVarInt("CarIdxLap", carIdx));
            carLiveData.setCarIdxLapDistPct(gameVarService.getVarFloat("CarIdxLapDistPct", carIdx));
            carLiveData.setCarIdxOnPitRoad(gameVarService.getVarBoolean("CarIdxOnPitRoad", carIdx));
            carLiveData.setCarIdxPosition(gameVarService.getVarInt("CarIdxPosition", carIdx));
            carLiveData.setCarIdxRPM(gameVarService.getVarFloat("CarIdxRPM", carIdx));
            carLiveData.setCarIdxSteer(gameVarService.getVarFloat("CarIdxSteer", carIdx));
            carLiveData.setCarIdxTrackSurface(TrkSurf.valueOf(gameVarService.getVarInt("CarIdxTrackSurface", carIdx)));

            carLiveDataList.add(carLiveData);
        }

        return carLiveDataList;
    }

    public CarSetupLiveData getCarSetupLiveData() {
        CarSetupLiveData carSetupLiveData = new CarSetupLiveData();

        carSetupLiveData.setCFrideHeight(gameVarService.getVarFloat("CFrideHeight"));
        carSetupLiveData.setCFshockDefl(gameVarService.getVarFloat("CFshockDefl"));
        carSetupLiveData.setCFshockVel(gameVarService.getVarFloat("CFshockVel"));
        carSetupLiveData.setCFSRrideHeight(gameVarService.getVarFloat("CFSRrideHeight"));
        carSetupLiveData.setCRrideHeight(gameVarService.getVarFloat("CRrideHeight"));
        carSetupLiveData.setCRshockDefl(gameVarService.getVarFloat("CRshockDefl"));
        carSetupLiveData.setCRshockVel(gameVarService.getVarFloat("CRshockVel"));
        carSetupLiveData.setDcABS(gameVarService.getVarFloat("DcABS"));
        carSetupLiveData.setDcAntiRollFront(gameVarService.getVarFloat("DcAntiRollFront"));
        carSetupLiveData.setDcAntiRollRear(gameVarService.getVarFloat("DcAntiRollRear"));
        carSetupLiveData.setDcBoostLevel(gameVarService.getVarFloat("DcBoostLevel"));
        carSetupLiveData.setDcBrakeBias(gameVarService.getVarFloat("DcBrakeBias"));
        carSetupLiveData.setDcDiffEntry(gameVarService.getVarFloat("DcDiffEntry"));
        carSetupLiveData.setDcDiffExit(gameVarService.getVarFloat("DcDiffExit"));
        carSetupLiveData.setDcDiffMiddle(gameVarService.getVarFloat("DcDiffMiddle"));
        carSetupLiveData.setDcEngineBraking(gameVarService.getVarFloat("DcEngineBraking"));
        carSetupLiveData.setDcEnginePower(gameVarService.getVarFloat("DcEnginePower"));
        carSetupLiveData.setDcFuelMixture(gameVarService.getVarFloat("DcFuelMixture"));
        carSetupLiveData.setDcRevLimiter(gameVarService.getVarFloat("DcRevLimiter"));
        carSetupLiveData.setDcThrottleShape(gameVarService.getVarFloat("DcThrottleShape"));
        carSetupLiveData.setDcTractionControl(gameVarService.getVarFloat("DcTractionControl"));
        carSetupLiveData.setDcTractionControl2(gameVarService.getVarFloat("DcTractionControl2"));
        carSetupLiveData.setDcTractionControlToggle(gameVarService.getVarFloat("DcTractionControlToggl"));
        carSetupLiveData.setDcWeightJackerLeft(gameVarService.getVarFloat("DcWeightJackerLeft"));
        carSetupLiveData.setDcWeightJackerRight(gameVarService.getVarFloat("DcWeightJackerRight"));
        carSetupLiveData.setDcWingFront(gameVarService.getVarFloat("DcWingFront"));
        carSetupLiveData.setDcWingRear(gameVarService.getVarFloat("DcWingRear"));
        carSetupLiveData.setDpFNOMKnobSetting(gameVarService.getVarFloat("DpFNOMKnobting"));
        carSetupLiveData.setDpFUFangleIndex(gameVarService.getVarFloat("DpFUFangleIndex"));
        carSetupLiveData.setDpFWingAngle(gameVarService.getVarFloat("DpFWingAngle"));
        carSetupLiveData.setDpFWingIndex(gameVarService.getVarFloat("DpFWingIndex"));
        carSetupLiveData.setDpLrWedgeAdj(gameVarService.getVarFloat("DpLrWedgeAdj"));
        carSetupLiveData.setDpPSSetting(gameVarService.getVarFloat("DpPSting"));
        carSetupLiveData.setDpQtape(gameVarService.getVarFloat("DpQtape"));
        carSetupLiveData.setDpRBarSetting(gameVarService.getVarFloat("DpRBarting"));
        carSetupLiveData.setDpRFTruckarmP1Dz(gameVarService.getVarFloat("DpRFTruckarmP1Dz"));
        carSetupLiveData.setDpRRDamperPerchOffsetm(gameVarService.getVarFloat("DpRRDamperPerchOffm"));
        carSetupLiveData.setDpRrPerchOffsetm(gameVarService.getVarFloat("DpRrPerchOffm"));
        carSetupLiveData.setDpRrWedgeAdj(gameVarService.getVarFloat("DpRrWedgeAdj"));
        carSetupLiveData.setDpRWingAngle(gameVarService.getVarFloat("DpRWingAngle"));
        carSetupLiveData.setDpRWingIndex(gameVarService.getVarFloat("DpRWingIndex"));
        carSetupLiveData.setDpRWingSetting(gameVarService.getVarFloat("DpRWingting"));
        carSetupLiveData.setDpTruckarmP1Dz(gameVarService.getVarFloat("DpTruckarmP1Dz"));
        carSetupLiveData.setDpWedgeAdj(gameVarService.getVarFloat("DpWedgeAdj"));
        carSetupLiveData.setLFbrakeLinePress(gameVarService.getVarFloat("LFbrakeLinePress"));
        carSetupLiveData.setLFcoldPressure(gameVarService.getVarFloat("LFcoldPressure"));
        carSetupLiveData.setLFpressure(gameVarService.getVarFloat("LFpressure"));
        carSetupLiveData.setLFrideHeight(gameVarService.getVarFloat("LFrideHeight"));
        carSetupLiveData.setLFshockDefl(gameVarService.getVarFloat("LFshockDefl"));
        carSetupLiveData.setLFshockVel(gameVarService.getVarFloat("LFshockVel"));
        carSetupLiveData.setLFspeed(gameVarService.getVarFloat("LFspeed"));
        carSetupLiveData.setLFtempCL(gameVarService.getVarFloat("LFtempCL"));
        carSetupLiveData.setLFtempCM(gameVarService.getVarFloat("LFtempCM"));
        carSetupLiveData.setLFtempCR(gameVarService.getVarFloat("LFtempCR"));
        carSetupLiveData.setLFtempL(gameVarService.getVarFloat("LFtempL"));
        carSetupLiveData.setLFtempM(gameVarService.getVarFloat("LFtempM"));
        carSetupLiveData.setLFtempR(gameVarService.getVarFloat("LFtempR"));
        carSetupLiveData.setLFwearL(gameVarService.getVarFloat("LFwearL"));
        carSetupLiveData.setLFwearM(gameVarService.getVarFloat("LFwearM"));
        carSetupLiveData.setLFwearR(gameVarService.getVarFloat("LFwearR"));
        carSetupLiveData.setLRbrakeLinePress(gameVarService.getVarFloat("LRbrakeLinePress"));
        carSetupLiveData.setLRcoldPressure(gameVarService.getVarFloat("LRcoldPressure"));
        carSetupLiveData.setLRpressure(gameVarService.getVarFloat("LRpressure"));
        carSetupLiveData.setLRrideHeight(gameVarService.getVarFloat("LRrideHeight"));
        carSetupLiveData.setLRshockDefl(gameVarService.getVarFloat("LRshockDefl"));
        carSetupLiveData.setLRshockVel(gameVarService.getVarFloat("LRshockVel"));
        carSetupLiveData.setLRspeed(gameVarService.getVarFloat("LRspeed"));
        carSetupLiveData.setLRtempCL(gameVarService.getVarFloat("LRtempCL"));
        carSetupLiveData.setLRtempCM(gameVarService.getVarFloat("LRtempCM"));
        carSetupLiveData.setLRtempCR(gameVarService.getVarFloat("LRtempCR"));
        carSetupLiveData.setLRtempL(gameVarService.getVarFloat("LRtempL"));
        carSetupLiveData.setLRtempM(gameVarService.getVarFloat("LRtempM"));
        carSetupLiveData.setLRtempR(gameVarService.getVarFloat("LRtempR"));
        carSetupLiveData.setLRwearL(gameVarService.getVarFloat("LRwearL"));
        carSetupLiveData.setLRwearM(gameVarService.getVarFloat("LRwearM"));
        carSetupLiveData.setLRwearR(gameVarService.getVarFloat("LRwearR"));
        carSetupLiveData.setRFbrakeLinePress(gameVarService.getVarFloat("RFbrakeLinePress"));
        carSetupLiveData.setRFcoldPressure(gameVarService.getVarFloat("RFcoldPressure"));
        carSetupLiveData.setRFpressure(gameVarService.getVarFloat("RFpressure"));
        carSetupLiveData.setRFrideHeight(gameVarService.getVarFloat("RFrideHeight"));
        carSetupLiveData.setRFshockDefl(gameVarService.getVarFloat("RFshockDefl"));
        carSetupLiveData.setRFshockVel(gameVarService.getVarFloat("RFshockVel"));
        carSetupLiveData.setRFspeed(gameVarService.getVarFloat("RFspeed"));
        carSetupLiveData.setRFtempCL(gameVarService.getVarFloat("RFtempCL"));
        carSetupLiveData.setRFtempCM(gameVarService.getVarFloat("RFtempCM"));
        carSetupLiveData.setRFtempCR(gameVarService.getVarFloat("RFtempCR"));
        carSetupLiveData.setRFtempL(gameVarService.getVarFloat("RFtempL"));
        carSetupLiveData.setRFtempM(gameVarService.getVarFloat("RFtempM"));
        carSetupLiveData.setRFtempR(gameVarService.getVarFloat("RFtempR"));
        carSetupLiveData.setRFwearL(gameVarService.getVarFloat("RFwearL"));
        carSetupLiveData.setRFwearM(gameVarService.getVarFloat("RFwearM"));
        carSetupLiveData.setRFwearR(gameVarService.getVarFloat("RFwearR"));
        carSetupLiveData.setRRbrakeLinePress(gameVarService.getVarFloat("RRbrakeLinePress"));
        carSetupLiveData.setRRcoldPressure(gameVarService.getVarFloat("RRcoldPressure"));
        carSetupLiveData.setRRpressure(gameVarService.getVarFloat("RRpressure"));
        carSetupLiveData.setRRrideHeight(gameVarService.getVarFloat("RRrideHeight"));
        carSetupLiveData.setRRshockDefl(gameVarService.getVarFloat("RRshockDefl"));
        carSetupLiveData.setRRshockVel(gameVarService.getVarFloat("RRshockVel"));
        carSetupLiveData.setRRspeed(gameVarService.getVarFloat("RRspeed"));
        carSetupLiveData.setRRtempCL(gameVarService.getVarFloat("RRtempCL"));
        carSetupLiveData.setRRtempCM(gameVarService.getVarFloat("RRtempCM"));
        carSetupLiveData.setRRtempCR(gameVarService.getVarFloat("RRtempCR"));
        carSetupLiveData.setRRtempL(gameVarService.getVarFloat("RRtempL"));
        carSetupLiveData.setRRtempM(gameVarService.getVarFloat("RRtempM"));
        carSetupLiveData.setRRtempR(gameVarService.getVarFloat("RRtempR"));
        carSetupLiveData.setRRwearL(gameVarService.getVarFloat("RRwearL"));
        carSetupLiveData.setRRwearM(gameVarService.getVarFloat("RRwearM"));
        carSetupLiveData.setRRwearR(gameVarService.getVarFloat("RRwearR"));

        return carSetupLiveData;
    }

    public CarTelemetryLiveData getCarTelemetryLiveData() {
        CarTelemetryLiveData carTelemetryLiveData = new CarTelemetryLiveData();

        carTelemetryLiveData.setBrake(gameVarService.getVarFloat("Brake"));
        carTelemetryLiveData.setBrakeRaw(gameVarService.getVarFloat("BrakeRaw"));
        carTelemetryLiveData.setClutch(gameVarService.getVarFloat("Clutch"));
        carTelemetryLiveData.setDriverMarker(gameVarService.getVarBoolean("DriverMarker"));

        // TODO: 2 Sep 2020
        int engineWarnings = gameVarService.getVarInt("EngineWarnings");
        carTelemetryLiveData.setEngineWarnings(new String[0]);

        carTelemetryLiveData.setFuelLevel(gameVarService.getVarFloat("FuelLevel"));
        carTelemetryLiveData.setFuelLevelPct(gameVarService.getVarFloat("FuelLevelPct"));
        carTelemetryLiveData.setFuelPress(gameVarService.getVarFloat("FuelPress"));
        carTelemetryLiveData.setFuelUsePerHour(gameVarService.getVarFloat("FuelUsePerHour"));
        carTelemetryLiveData.setGear(gameVarService.getVarInt("Gear"));
        carTelemetryLiveData.setInGarage(gameVarService.getVarBoolean("IsInGarage"));
        carTelemetryLiveData.setOnTrack(gameVarService.getVarBoolean("IsOnTrack"));
        carTelemetryLiveData.setOnTrackCar(gameVarService.getVarBoolean("IsOnTrackCar"));
        carTelemetryLiveData.setLap(gameVarService.getVarInt("Lap"));
        carTelemetryLiveData.setLapBestLap(gameVarService.getVarInt("LapBestLap"));
        carTelemetryLiveData.setLapBestLapTime(gameVarService.getVarFloat("LapBestLapTime"));
        carTelemetryLiveData.setLapBestNLapLap(gameVarService.getVarInt("LapBestNLapLap"));
        carTelemetryLiveData.setLapBestNLapTime(gameVarService.getVarFloat("LapBestNLapTime"));
        carTelemetryLiveData.setLapCurrentLapTime(gameVarService.getVarFloat("LapCurrentLapTime"));
        carTelemetryLiveData.setLapDeltaToBestLap(gameVarService.getVarFloat("LapDeltaToBestLap"));
        carTelemetryLiveData.setLapDeltaToBestLap_DD(gameVarService.getVarFloat("LapDeltaToBestLap_DD"));
        carTelemetryLiveData.setLapDeltaToBestLap_OK(gameVarService.getVarBoolean("LapDeltaToBestLap_OK"));
        carTelemetryLiveData.setLapDeltaToOptimalLap(gameVarService.getVarFloat("LapDeltaToOptimalLap"));
        carTelemetryLiveData.setLapDeltaToOptimalLap_DD(gameVarService.getVarFloat("LapDeltaToOptimalLap_DD"));
        carTelemetryLiveData.setLapDeltaToOptimalLap_OK(gameVarService.getVarBoolean("LapDeltaToOptimalLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap(gameVarService.getVarFloat("LapDeltaToSessionBestLap"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap_DD(gameVarService.getVarFloat("LapDeltaToSessionBestLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap_OK(gameVarService.getVarBoolean("LapDeltaToSessionBestLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap(gameVarService.getVarFloat("LapDeltaToSessionLastlLap"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap_DD(gameVarService.getVarFloat("LapDeltaToSessionLastlLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap_OK(gameVarService.getVarBoolean("LapDeltaToSessionLastlLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap(gameVarService.getVarFloat("LapDeltaToSessionOptimalLap"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap_DD(gameVarService.getVarFloat("LapDeltaToSessionOptimalLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap_OK(gameVarService.getVarBoolean("LapDeltaToSessionOptimalLap_OK"));
        carTelemetryLiveData.setLapDist(gameVarService.getVarFloat("LapDist"));
        carTelemetryLiveData.setLapDistPct(gameVarService.getVarFloat("LapDistPct"));
        carTelemetryLiveData.setLapLasNLapSeq(gameVarService.getVarInt("LapLasNLapSeq"));
        carTelemetryLiveData.setLapLastLapTime(gameVarService.getVarFloat("LapLastLapTime"));
        carTelemetryLiveData.setLapLastNLapTime(gameVarService.getVarFloat("LapLastNLapTime"));
        carTelemetryLiveData.setLat(gameVarService.getVarDouble("Lat"));
        carTelemetryLiveData.setLatAccel(gameVarService.getVarFloat("LatAccel"));
        carTelemetryLiveData.setLon(gameVarService.getVarDouble("Lon"));
        carTelemetryLiveData.setLonAccel(gameVarService.getVarFloat("LonAccel"));
        carTelemetryLiveData.setManifoldPress(gameVarService.getVarFloat("ManifoldPress"));
        carTelemetryLiveData.setOilLevel(gameVarService.getVarFloat("OilLevel"));
        carTelemetryLiveData.setOilPress(gameVarService.getVarFloat("OilPress"));
        carTelemetryLiveData.setOilTemp(gameVarService.getVarFloat("OilTemp"));
        carTelemetryLiveData.setOnPitRoad(gameVarService.getVarBoolean("OnPitRoad"));
        carTelemetryLiveData.setPitch(gameVarService.getVarFloat("Pitch"));
        carTelemetryLiveData.setPitchRate(gameVarService.getVarFloat("PitchRate"));
        carTelemetryLiveData.setPitOptRepairLeft(gameVarService.getVarFloat("PitOptRepairLeft"));
        carTelemetryLiveData.setPitRepairLeft(gameVarService.getVarFloat("PitRepairLeft"));

        // TODO: 2 Sep 2020
        int pitSvFlags = gameVarService.getVarInt("PitSvFlags");
        carTelemetryLiveData.setPitSvFlags(new String[0]);

        carTelemetryLiveData.setPitSvFuel(gameVarService.getVarFloat("PitSvFuel"));
        carTelemetryLiveData.setPitSvLFP(gameVarService.getVarFloat("PitSvLFP"));
        carTelemetryLiveData.setPitSvLRP(gameVarService.getVarFloat("PitSvLRP"));
        carTelemetryLiveData.setPitSvRFP(gameVarService.getVarFloat("PitSvRFP"));
        carTelemetryLiveData.setPitSvRRP(gameVarService.getVarFloat("PitSvRRP"));
        carTelemetryLiveData.setPlayerCarClassPosition(gameVarService.getVarInt("PlayerCarClassPosition"));
        carTelemetryLiveData.setPlayerCarPosition(gameVarService.getVarInt("PlayerCarPosition"));
        carTelemetryLiveData.setRaceLaps(gameVarService.getVarInt("RaceLaps"));
        carTelemetryLiveData.setRadioTransmitCarIdx(gameVarService.getVarInt("RadioTransmitCarIdx"));
        carTelemetryLiveData.setRadioTransmitFrequencyIdx(gameVarService.getVarInt("RadioTransmitFrequencyIdx"));
        carTelemetryLiveData.setRadioTransmitRadioIdx(gameVarService.getVarInt("RadioTransmitRadioIdx"));
        carTelemetryLiveData.setRoll(gameVarService.getVarFloat("Roll"));
        carTelemetryLiveData.setRollRate(gameVarService.getVarFloat("RollRate"));
        carTelemetryLiveData.setRPM(gameVarService.getVarFloat("RPM"));
        carTelemetryLiveData.setShiftGrindRPM(gameVarService.getVarFloat("ShiftGrindRPM"));
        carTelemetryLiveData.setShiftIndicatorPct(gameVarService.getVarFloat("ShiftIndicatorPct"));
        carTelemetryLiveData.setShiftPowerPct(gameVarService.getVarFloat("ShiftPowerPct"));
        carTelemetryLiveData.setSpeed(gameVarService.getVarFloat("Speed"));
        carTelemetryLiveData.setThrottle(gameVarService.getVarFloat("Throttle"));
        carTelemetryLiveData.setThrottleRaw(gameVarService.getVarFloat("ThrottleRaw"));
        carTelemetryLiveData.setVelocityX(gameVarService.getVarFloat("VelocityX"));
        carTelemetryLiveData.setVelocityY(gameVarService.getVarFloat("VelocityY"));
        carTelemetryLiveData.setVelocityZ(gameVarService.getVarFloat("VelocityZ"));
        carTelemetryLiveData.setVertAccel(gameVarService.getVarFloat("VertAccel"));
        carTelemetryLiveData.setVoltage(gameVarService.getVarFloat("Voltage"));
        carTelemetryLiveData.setWaterLevel(gameVarService.getVarFloat("WaterLevel"));
        carTelemetryLiveData.setWaterTemp(gameVarService.getVarFloat("WaterTemp"));
        carTelemetryLiveData.setYaw(gameVarService.getVarFloat("Yaw"));
        carTelemetryLiveData.setYawNorth(gameVarService.getVarFloat("YawNorth"));
        carTelemetryLiveData.setYawRate(gameVarService.getVarFloat("YawRate"));

        return carTelemetryLiveData;
    }

    public ConfigLiveData getConfigLiveData() {
        ConfigLiveData configLiveData = new ConfigLiveData();

        configLiveData.setCpuUsageBG(gameVarService.getVarFloat("CpuUsageBG"));
        configLiveData.setFrameRate(gameVarService.getVarFloat("FrameRate"));
        configLiveData.setDisplayUnits(gameVarService.getVarInt("DisplayUnits"));
        configLiveData.setEnterExitReset(gameVarService.getVarInt("EnterExitReset"));
        configLiveData.setDiskLoggingActive(gameVarService.getVarBoolean("DiskLoggingActive"));
        configLiveData.setDiskLoggingEnabled(gameVarService.getVarBoolean("DiskLoggingEnabled"));

        return configLiveData;
    }

    public TeamLiveData getTeamLiveData() {
        TeamLiveData teamLiveData = new TeamLiveData();

        teamLiveData.setDCDriversSoFar(gameVarService.getVarInt("DCDriversSoFar"));
        teamLiveData.setDCLapStatus(gameVarService.getVarInt("DCLapStatus"));

        return teamLiveData;
    }

    public ReplayLiveData getReplayLiveData() {
        ReplayLiveData replayLiveData = new ReplayLiveData();

        replayLiveData.setReplayPlaying(gameVarService.getVarBoolean("ReplayPlaying"));
        replayLiveData.setReplayFrameNum(gameVarService.getVarInt("ReplayFrameNum"));
        replayLiveData.setReplayFrameNumEnd(gameVarService.getVarInt("ReplayFrameNumEnd"));
        replayLiveData.setReplayPlaySlowMotion(gameVarService.getVarBoolean("ReplayPlaySlowMotion"));
        replayLiveData.setReplayPlaySpeed(gameVarService.getVarInt("ReplayPlaySpeed"));
        replayLiveData.setReplaySessionNum(gameVarService.getVarInt("ReplaySessionNum"));
        replayLiveData.setReplaySessionTime(gameVarService.getVarDouble("ReplaySessionTime"));

        return replayLiveData;
    }

    public SessionLiveData getSessionLiveData() {
        SessionLiveData sessionLiveData = new SessionLiveData();

        // TODO: 2 Sep 2020
        int sessionFlags = gameVarService.getVarInt("SessionFlags");
        sessionLiveData.setSessionFlags(new String[0]);

        sessionLiveData.setSessionLapsRemain(gameVarService.getVarInt("SessionLapsRemain"));
        sessionLiveData.setSessionNum(gameVarService.getVarInt("SessionNum"));
        sessionLiveData.setSessionState(SessionState.getStringOf(gameVarService.getVarInt("SessionState")));
        sessionLiveData.setSessionTime(gameVarService.getVarDouble("SessionTime"));
        sessionLiveData.setSessionTimeRemain(gameVarService.getVarDouble("SessionTimeRemain"));
        sessionLiveData.setSessionUniqueID(gameVarService.getVarInt("SessionUniqueID"));

        return sessionLiveData;
    }

    public SteeringWheelLiveData getSteeringWheelLiveData() {
        SteeringWheelLiveData steeringWheelLiveData = new SteeringWheelLiveData();

        steeringWheelLiveData.setSteeringWheelAngle(gameVarService.getVarFloat("SteeringWheelAngle"));
        steeringWheelLiveData.setSteeringWheelAngleMax(gameVarService.getVarFloat("SteeringWheelAngleMax"));
        steeringWheelLiveData.setSteeringWheelPctDamper(gameVarService.getVarFloat("SteeringWheelPctDamper"));
        steeringWheelLiveData.setSteeringWheelPctTorque(gameVarService.getVarFloat("SteeringWheelPctTorque"));
        steeringWheelLiveData.setSteeringWheelPctTorqueSign(gameVarService.getVarFloat("SteeringWheelPctTorqueSign"));
        steeringWheelLiveData.setSteeringWheelPctTorqueSignStops(gameVarService.getVarFloat("SteeringWheelPctTorqueSignStops"));
        steeringWheelLiveData.setSteeringWheelPeakForceNm(gameVarService.getVarFloat("SteeringWheelPeakForceNm"));
        steeringWheelLiveData.setSteeringWheelTorque(gameVarService.getVarFloat("SteeringWheelTorque"));

        return steeringWheelLiveData;
    }

    public TrackLiveData getTrackLiveData() {
        TrackLiveData trackLiveData = new TrackLiveData();

        trackLiveData.setAirDensity(gameVarService.getVarFloat("AirDensity"));
        trackLiveData.setAirPressure(gameVarService.getVarFloat("AirPressure"));
        trackLiveData.setAirTemp(gameVarService.getVarFloat("AirTemp"));
        trackLiveData.setAlt(gameVarService.getVarFloat("Alt"));
        trackLiveData.setFogLevel(gameVarService.getVarFloat("FogLevel"));
        trackLiveData.setRelativeHumidity(gameVarService.getVarFloat("RelativeHumidity"));
        trackLiveData.setSkies(gameVarService.getVarInt("Skies"));
        trackLiveData.setTrackTemp(gameVarService.getVarFloat("TrackTemp"));
        trackLiveData.setTrackTempCrew(gameVarService.getVarFloat("TrackTempCrew"));
        trackLiveData.setWeatherType(gameVarService.getVarInt("WeatherType"));
        trackLiveData.setWindDir(gameVarService.getVarFloat("WindDir"));
        trackLiveData.setWindVel(gameVarService.getVarFloat("WindVel"));

        return trackLiveData;
    }

}
