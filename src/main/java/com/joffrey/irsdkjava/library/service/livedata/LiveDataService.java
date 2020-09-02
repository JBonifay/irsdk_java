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

package com.joffrey.irsdkjava.library.service.livedata;

import com.joffrey.irsdkjava.library.service.livedata.model.CameraLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.CarSetupLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.CarTelemetryLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.ConfigLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.ReplayLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.SessionLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.SteeringWheelLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.TeamLiveData;
import com.joffrey.irsdkjava.library.service.livedata.model.TrackLiveData;
import com.joffrey.irsdkjava.library.service.livedata.utils.GameVarUtils;
import com.joffrey.irsdkjava.sdk.defines.CameraState;
import com.joffrey.irsdkjava.sdk.defines.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class LiveDataService {

    private final        GameVarUtils gameVarUtilsHelper;

    public CameraLiveData getCameraLiveData() {
        CameraLiveData cameraLiveData = new CameraLiveData();
        cameraLiveData.setCamCameraNumber(gameVarUtilsHelper.getVarInt("CamCameraNumber"));

        gameVarUtilsHelper.getVarInt("CamCameraState");
        // TODO: 2 Sep 2020 Bitfield test
        CameraState cameraState = CameraState.irsdk_IsSessionScreen;
        cameraLiveData.setCamCameraState(cameraState);
        cameraLiveData.setCamCarIdx(gameVarUtilsHelper.getVarInt("CamCarIdx"));
        cameraLiveData.setCamGroupNumber(gameVarUtilsHelper.getVarInt("CamGroupNumber"));
        return cameraLiveData;
    }


    public CarSetupLiveData getCarSetupLiveData() {
        CarSetupLiveData carSetupLiveData = new CarSetupLiveData();

        carSetupLiveData.setCFrideHeight(gameVarUtilsHelper.getVarFloat("CFrideHeight"));
        carSetupLiveData.setCFshockDefl(gameVarUtilsHelper.getVarFloat("CFshockDefl"));
        carSetupLiveData.setCFshockVel(gameVarUtilsHelper.getVarFloat("CFshockVel"));
        carSetupLiveData.setCFSRrideHeight(gameVarUtilsHelper.getVarFloat("CFSRrideHeight"));
        carSetupLiveData.setCRrideHeight(gameVarUtilsHelper.getVarFloat("CRrideHeight"));
        carSetupLiveData.setCRshockDefl(gameVarUtilsHelper.getVarFloat("CRshockDefl"));
        carSetupLiveData.setCRshockVel(gameVarUtilsHelper.getVarFloat("CRshockVel"));
        carSetupLiveData.setDcABS(gameVarUtilsHelper.getVarFloat("DcABS"));
        carSetupLiveData.setDcAntiRollFront(gameVarUtilsHelper.getVarFloat("DcAntiRollFront"));
        carSetupLiveData.setDcAntiRollRear(gameVarUtilsHelper.getVarFloat("DcAntiRollRear"));
        carSetupLiveData.setDcBoostLevel(gameVarUtilsHelper.getVarFloat("DcBoostLevel"));
        carSetupLiveData.setDcBrakeBias(gameVarUtilsHelper.getVarFloat("DcBrakeBias"));
        carSetupLiveData.setDcDiffEntry(gameVarUtilsHelper.getVarFloat("DcDiffEntry"));
        carSetupLiveData.setDcDiffExit(gameVarUtilsHelper.getVarFloat("DcDiffExit"));
        carSetupLiveData.setDcDiffMiddle(gameVarUtilsHelper.getVarFloat("DcDiffMiddle"));
        carSetupLiveData.setDcEngineBraking(gameVarUtilsHelper.getVarFloat("DcEngineBraking"));
        carSetupLiveData.setDcEnginePower(gameVarUtilsHelper.getVarFloat("DcEnginePower"));
        carSetupLiveData.setDcFuelMixture(gameVarUtilsHelper.getVarFloat("DcFuelMixture"));
        carSetupLiveData.setDcRevLimiter(gameVarUtilsHelper.getVarFloat("DcRevLimiter"));
        carSetupLiveData.setDcThrottleShape(gameVarUtilsHelper.getVarFloat("DcThrottleShape"));
        carSetupLiveData.setDcTractionControl(gameVarUtilsHelper.getVarFloat("DcTractionControl"));
        carSetupLiveData.setDcTractionControl2(gameVarUtilsHelper.getVarFloat("DcTractionControl2"));
        carSetupLiveData.setDcTractionControlToggle(gameVarUtilsHelper.getVarFloat("DcTractionControlToggl"));
        carSetupLiveData.setDcWeightJackerLeft(gameVarUtilsHelper.getVarFloat("DcWeightJackerLeft"));
        carSetupLiveData.setDcWeightJackerRight(gameVarUtilsHelper.getVarFloat("DcWeightJackerRight"));
        carSetupLiveData.setDcWingFront(gameVarUtilsHelper.getVarFloat("DcWingFront"));
        carSetupLiveData.setDcWingRear(gameVarUtilsHelper.getVarFloat("DcWingRear"));
        carSetupLiveData.setDpFNOMKnobSetting(gameVarUtilsHelper.getVarFloat("DpFNOMKnobting"));
        carSetupLiveData.setDpFUFangleIndex(gameVarUtilsHelper.getVarFloat("DpFUFangleIndex"));
        carSetupLiveData.setDpFWingAngle(gameVarUtilsHelper.getVarFloat("DpFWingAngle"));
        carSetupLiveData.setDpFWingIndex(gameVarUtilsHelper.getVarFloat("DpFWingIndex"));
        carSetupLiveData.setDpLrWedgeAdj(gameVarUtilsHelper.getVarFloat("DpLrWedgeAdj"));
        carSetupLiveData.setDpPSSetting(gameVarUtilsHelper.getVarFloat("DpPSting"));
        carSetupLiveData.setDpQtape(gameVarUtilsHelper.getVarFloat("DpQtape"));
        carSetupLiveData.setDpRBarSetting(gameVarUtilsHelper.getVarFloat("DpRBarting"));
        carSetupLiveData.setDpRFTruckarmP1Dz(gameVarUtilsHelper.getVarFloat("DpRFTruckarmP1Dz"));
        carSetupLiveData.setDpRRDamperPerchOffsetm(gameVarUtilsHelper.getVarFloat("DpRRDamperPerchOffm"));
        carSetupLiveData.setDpRrPerchOffsetm(gameVarUtilsHelper.getVarFloat("DpRrPerchOffm"));
        carSetupLiveData.setDpRrWedgeAdj(gameVarUtilsHelper.getVarFloat("DpRrWedgeAdj"));
        carSetupLiveData.setDpRWingAngle(gameVarUtilsHelper.getVarFloat("DpRWingAngle"));
        carSetupLiveData.setDpRWingIndex(gameVarUtilsHelper.getVarFloat("DpRWingIndex"));
        carSetupLiveData.setDpRWingSetting(gameVarUtilsHelper.getVarFloat("DpRWingting"));
        carSetupLiveData.setDpTruckarmP1Dz(gameVarUtilsHelper.getVarFloat("DpTruckarmP1Dz"));
        carSetupLiveData.setDpWedgeAdj(gameVarUtilsHelper.getVarFloat("DpWedgeAdj"));
        carSetupLiveData.setLFbrakeLinePress(gameVarUtilsHelper.getVarFloat("LFbrakeLinePress"));
        carSetupLiveData.setLFcoldPressure(gameVarUtilsHelper.getVarFloat("LFcoldPressure"));
        carSetupLiveData.setLFpressure(gameVarUtilsHelper.getVarFloat("LFpressure"));
        carSetupLiveData.setLFrideHeight(gameVarUtilsHelper.getVarFloat("LFrideHeight"));
        carSetupLiveData.setLFshockDefl(gameVarUtilsHelper.getVarFloat("LFshockDefl"));
        carSetupLiveData.setLFshockVel(gameVarUtilsHelper.getVarFloat("LFshockVel"));
        carSetupLiveData.setLFspeed(gameVarUtilsHelper.getVarFloat("LFspeed"));
        carSetupLiveData.setLFtempCL(gameVarUtilsHelper.getVarFloat("LFtempCL"));
        carSetupLiveData.setLFtempCM(gameVarUtilsHelper.getVarFloat("LFtempCM"));
        carSetupLiveData.setLFtempCR(gameVarUtilsHelper.getVarFloat("LFtempCR"));
        carSetupLiveData.setLFtempL(gameVarUtilsHelper.getVarFloat("LFtempL"));
        carSetupLiveData.setLFtempM(gameVarUtilsHelper.getVarFloat("LFtempM"));
        carSetupLiveData.setLFtempR(gameVarUtilsHelper.getVarFloat("LFtempR"));
        carSetupLiveData.setLFwearL(gameVarUtilsHelper.getVarFloat("LFwearL"));
        carSetupLiveData.setLFwearM(gameVarUtilsHelper.getVarFloat("LFwearM"));
        carSetupLiveData.setLFwearR(gameVarUtilsHelper.getVarFloat("LFwearR"));
        carSetupLiveData.setLRbrakeLinePress(gameVarUtilsHelper.getVarFloat("LRbrakeLinePress"));
        carSetupLiveData.setLRcoldPressure(gameVarUtilsHelper.getVarFloat("LRcoldPressure"));
        carSetupLiveData.setLRpressure(gameVarUtilsHelper.getVarFloat("LRpressure"));
        carSetupLiveData.setLRrideHeight(gameVarUtilsHelper.getVarFloat("LRrideHeight"));
        carSetupLiveData.setLRshockDefl(gameVarUtilsHelper.getVarFloat("LRshockDefl"));
        carSetupLiveData.setLRshockVel(gameVarUtilsHelper.getVarFloat("LRshockVel"));
        carSetupLiveData.setLRspeed(gameVarUtilsHelper.getVarFloat("LRspeed"));
        carSetupLiveData.setLRtempCL(gameVarUtilsHelper.getVarFloat("LRtempCL"));
        carSetupLiveData.setLRtempCM(gameVarUtilsHelper.getVarFloat("LRtempCM"));
        carSetupLiveData.setLRtempCR(gameVarUtilsHelper.getVarFloat("LRtempCR"));
        carSetupLiveData.setLRtempL(gameVarUtilsHelper.getVarFloat("LRtempL"));
        carSetupLiveData.setLRtempM(gameVarUtilsHelper.getVarFloat("LRtempM"));
        carSetupLiveData.setLRtempR(gameVarUtilsHelper.getVarFloat("LRtempR"));
        carSetupLiveData.setLRwearL(gameVarUtilsHelper.getVarFloat("LRwearL"));
        carSetupLiveData.setLRwearM(gameVarUtilsHelper.getVarFloat("LRwearM"));
        carSetupLiveData.setLRwearR(gameVarUtilsHelper.getVarFloat("LRwearR"));
        carSetupLiveData.setRFbrakeLinePress(gameVarUtilsHelper.getVarFloat("RFbrakeLinePress"));
        carSetupLiveData.setRFcoldPressure(gameVarUtilsHelper.getVarFloat("RFcoldPressure"));
        carSetupLiveData.setRFpressure(gameVarUtilsHelper.getVarFloat("RFpressure"));
        carSetupLiveData.setRFrideHeight(gameVarUtilsHelper.getVarFloat("RFrideHeight"));
        carSetupLiveData.setRFshockDefl(gameVarUtilsHelper.getVarFloat("RFshockDefl"));
        carSetupLiveData.setRFshockVel(gameVarUtilsHelper.getVarFloat("RFshockVel"));
        carSetupLiveData.setRFspeed(gameVarUtilsHelper.getVarFloat("RFspeed"));
        carSetupLiveData.setRFtempCL(gameVarUtilsHelper.getVarFloat("RFtempCL"));
        carSetupLiveData.setRFtempCM(gameVarUtilsHelper.getVarFloat("RFtempCM"));
        carSetupLiveData.setRFtempCR(gameVarUtilsHelper.getVarFloat("RFtempCR"));
        carSetupLiveData.setRFtempL(gameVarUtilsHelper.getVarFloat("RFtempL"));
        carSetupLiveData.setRFtempM(gameVarUtilsHelper.getVarFloat("RFtempM"));
        carSetupLiveData.setRFtempR(gameVarUtilsHelper.getVarFloat("RFtempR"));
        carSetupLiveData.setRFwearL(gameVarUtilsHelper.getVarFloat("RFwearL"));
        carSetupLiveData.setRFwearM(gameVarUtilsHelper.getVarFloat("RFwearM"));
        carSetupLiveData.setRFwearR(gameVarUtilsHelper.getVarFloat("RFwearR"));
        carSetupLiveData.setRRbrakeLinePress(gameVarUtilsHelper.getVarFloat("RRbrakeLinePress"));
        carSetupLiveData.setRRcoldPressure(gameVarUtilsHelper.getVarFloat("RRcoldPressure"));
        carSetupLiveData.setRRpressure(gameVarUtilsHelper.getVarFloat("RRpressure"));
        carSetupLiveData.setRRrideHeight(gameVarUtilsHelper.getVarFloat("RRrideHeight"));
        carSetupLiveData.setRRshockDefl(gameVarUtilsHelper.getVarFloat("RRshockDefl"));
        carSetupLiveData.setRRshockVel(gameVarUtilsHelper.getVarFloat("RRshockVel"));
        carSetupLiveData.setRRspeed(gameVarUtilsHelper.getVarFloat("RRspeed"));
        carSetupLiveData.setRRtempCL(gameVarUtilsHelper.getVarFloat("RRtempCL"));
        carSetupLiveData.setRRtempCM(gameVarUtilsHelper.getVarFloat("RRtempCM"));
        carSetupLiveData.setRRtempCR(gameVarUtilsHelper.getVarFloat("RRtempCR"));
        carSetupLiveData.setRRtempL(gameVarUtilsHelper.getVarFloat("RRtempL"));
        carSetupLiveData.setRRtempM(gameVarUtilsHelper.getVarFloat("RRtempM"));
        carSetupLiveData.setRRtempR(gameVarUtilsHelper.getVarFloat("RRtempR"));
        carSetupLiveData.setRRwearL(gameVarUtilsHelper.getVarFloat("RRwearL"));
        carSetupLiveData.setRRwearM(gameVarUtilsHelper.getVarFloat("RRwearM"));
        carSetupLiveData.setRRwearR(gameVarUtilsHelper.getVarFloat("RRwearR"));

        return carSetupLiveData;
    }

    public CarTelemetryLiveData getCarTelemetryLiveData() {
        CarTelemetryLiveData carTelemetryLiveData = new CarTelemetryLiveData();

        carTelemetryLiveData.setBrake(gameVarUtilsHelper.getVarFloat("Brake"));
        carTelemetryLiveData.setBrakeRaw(gameVarUtilsHelper.getVarFloat("BrakeRaw"));
        carTelemetryLiveData.setClutch(gameVarUtilsHelper.getVarFloat("Clutch"));
        carTelemetryLiveData.setDriverMarker(gameVarUtilsHelper.getVarBoolean("DriverMarker"));

        // TODO: 2 Sep 2020
        int engineWarnings = gameVarUtilsHelper.getVarInt("EngineWarnings");
        carTelemetryLiveData.setEngineWarnings(new String[0]);

        carTelemetryLiveData.setFuelLevel(gameVarUtilsHelper.getVarFloat("FuelLevel"));
        carTelemetryLiveData.setFuelLevelPct(gameVarUtilsHelper.getVarFloat("FuelLevelPct"));
        carTelemetryLiveData.setFuelPress(gameVarUtilsHelper.getVarFloat("FuelPress"));
        carTelemetryLiveData.setFuelUsePerHour(gameVarUtilsHelper.getVarFloat("FuelUsePerHour"));
        carTelemetryLiveData.setGear(gameVarUtilsHelper.getVarInt("Gear"));
        carTelemetryLiveData.setInGarage(gameVarUtilsHelper.getVarBoolean("IsInGarage"));
        carTelemetryLiveData.setOnTrack(gameVarUtilsHelper.getVarBoolean("IsOnTrack"));
        carTelemetryLiveData.setOnTrackCar(gameVarUtilsHelper.getVarBoolean("IsOnTrackCar"));
        carTelemetryLiveData.setLap(gameVarUtilsHelper.getVarInt("Lap"));
        carTelemetryLiveData.setLapBestLap(gameVarUtilsHelper.getVarInt("LapBestLap"));
        carTelemetryLiveData.setLapBestLapTime(gameVarUtilsHelper.getVarFloat("LapBestLapTime"));
        carTelemetryLiveData.setLapBestNLapLap(gameVarUtilsHelper.getVarInt("LapBestNLapLap"));
        carTelemetryLiveData.setLapBestNLapTime(gameVarUtilsHelper.getVarFloat("LapBestNLapTime"));
        carTelemetryLiveData.setLapCurrentLapTime(gameVarUtilsHelper.getVarFloat("LapCurrentLapTime"));
        carTelemetryLiveData.setLapDeltaToBestLap(gameVarUtilsHelper.getVarFloat("LapDeltaToBestLap"));
        carTelemetryLiveData.setLapDeltaToBestLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToBestLap_DD"));
        carTelemetryLiveData.setLapDeltaToBestLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToBestLap_OK"));
        carTelemetryLiveData.setLapDeltaToOptimalLap(gameVarUtilsHelper.getVarFloat("LapDeltaToOptimalLap"));
        carTelemetryLiveData.setLapDeltaToOptimalLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToOptimalLap_DD"));
        carTelemetryLiveData.setLapDeltaToOptimalLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToOptimalLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionBestLap"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionBestLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionBestLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionBestLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionLastlLap"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionLastlLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionLastlLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionLastlLap_OK"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionOptimalLap"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionOptimalLap_DD"));
        carTelemetryLiveData.setLapDeltaToSessionOptimalLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionOptimalLap_OK"));
        carTelemetryLiveData.setLapDist(gameVarUtilsHelper.getVarFloat("LapDist"));
        carTelemetryLiveData.setLapDistPct(gameVarUtilsHelper.getVarFloat("LapDistPct"));
        carTelemetryLiveData.setLapLasNLapSeq(gameVarUtilsHelper.getVarInt("LapLasNLapSeq"));
        carTelemetryLiveData.setLapLastLapTime(gameVarUtilsHelper.getVarFloat("LapLastLapTime"));
        carTelemetryLiveData.setLapLastNLapTime(gameVarUtilsHelper.getVarFloat("LapLastNLapTime"));
        carTelemetryLiveData.setLat(gameVarUtilsHelper.getVarDouble("Lat"));
        carTelemetryLiveData.setLatAccel(gameVarUtilsHelper.getVarFloat("LatAccel"));
        carTelemetryLiveData.setLon(gameVarUtilsHelper.getVarDouble("Lon"));
        carTelemetryLiveData.setLonAccel(gameVarUtilsHelper.getVarFloat("LonAccel"));
        carTelemetryLiveData.setManifoldPress(gameVarUtilsHelper.getVarFloat("ManifoldPress"));
        carTelemetryLiveData.setOilLevel(gameVarUtilsHelper.getVarFloat("OilLevel"));
        carTelemetryLiveData.setOilPress(gameVarUtilsHelper.getVarFloat("OilPress"));
        carTelemetryLiveData.setOilTemp(gameVarUtilsHelper.getVarFloat("OilTemp"));
        carTelemetryLiveData.setOnPitRoad(gameVarUtilsHelper.getVarBoolean("OnPitRoad"));
        carTelemetryLiveData.setPitch(gameVarUtilsHelper.getVarFloat("Pitch"));
        carTelemetryLiveData.setPitchRate(gameVarUtilsHelper.getVarFloat("PitchRate"));
        carTelemetryLiveData.setPitOptRepairLeft(gameVarUtilsHelper.getVarFloat("PitOptRepairLeft"));
        carTelemetryLiveData.setPitRepairLeft(gameVarUtilsHelper.getVarFloat("PitRepairLeft"));

        // TODO: 2 Sep 2020
        int pitSvFlags = gameVarUtilsHelper.getVarInt("PitSvFlags");
        carTelemetryLiveData.setPitSvFlags(new String[0]);

        carTelemetryLiveData.setPitSvFuel(gameVarUtilsHelper.getVarFloat("PitSvFuel"));
        carTelemetryLiveData.setPitSvLFP(gameVarUtilsHelper.getVarFloat("PitSvLFP"));
        carTelemetryLiveData.setPitSvLRP(gameVarUtilsHelper.getVarFloat("PitSvLRP"));
        carTelemetryLiveData.setPitSvRFP(gameVarUtilsHelper.getVarFloat("PitSvRFP"));
        carTelemetryLiveData.setPitSvRRP(gameVarUtilsHelper.getVarFloat("PitSvRRP"));
        carTelemetryLiveData.setPlayerCarClassPosition(gameVarUtilsHelper.getVarInt("PlayerCarClassPosition"));
        carTelemetryLiveData.setPlayerCarPosition(gameVarUtilsHelper.getVarInt("PlayerCarPosition"));
        carTelemetryLiveData.setRaceLaps(gameVarUtilsHelper.getVarInt("RaceLaps"));
        carTelemetryLiveData.setRadioTransmitCarIdx(gameVarUtilsHelper.getVarInt("RadioTransmitCarIdx"));
        carTelemetryLiveData.setRadioTransmitFrequencyIdx(gameVarUtilsHelper.getVarInt("RadioTransmitFrequencyIdx"));
        carTelemetryLiveData.setRadioTransmitRadioIdx(gameVarUtilsHelper.getVarInt("RadioTransmitRadioIdx"));
        carTelemetryLiveData.setRoll(gameVarUtilsHelper.getVarFloat("Roll"));
        carTelemetryLiveData.setRollRate(gameVarUtilsHelper.getVarFloat("RollRate"));
        carTelemetryLiveData.setRPM(gameVarUtilsHelper.getVarFloat("RPM"));
        carTelemetryLiveData.setShiftGrindRPM(gameVarUtilsHelper.getVarFloat("ShiftGrindRPM"));
        carTelemetryLiveData.setShiftIndicatorPct(gameVarUtilsHelper.getVarFloat("ShiftIndicatorPct"));
        carTelemetryLiveData.setShiftPowerPct(gameVarUtilsHelper.getVarFloat("ShiftPowerPct"));
        carTelemetryLiveData.setSpeed(gameVarUtilsHelper.getVarFloat("Speed"));
        carTelemetryLiveData.setThrottle(gameVarUtilsHelper.getVarFloat("Throttle"));
        carTelemetryLiveData.setThrottleRaw(gameVarUtilsHelper.getVarFloat("ThrottleRaw"));
        carTelemetryLiveData.setVelocityX(gameVarUtilsHelper.getVarFloat("VelocityX"));
        carTelemetryLiveData.setVelocityY(gameVarUtilsHelper.getVarFloat("VelocityY"));
        carTelemetryLiveData.setVelocityZ(gameVarUtilsHelper.getVarFloat("VelocityZ"));
        carTelemetryLiveData.setVertAccel(gameVarUtilsHelper.getVarFloat("VertAccel"));
        carTelemetryLiveData.setVoltage(gameVarUtilsHelper.getVarFloat("Voltage"));
        carTelemetryLiveData.setWaterLevel(gameVarUtilsHelper.getVarFloat("WaterLevel"));
        carTelemetryLiveData.setWaterTemp(gameVarUtilsHelper.getVarFloat("WaterTemp"));
        carTelemetryLiveData.setYaw(gameVarUtilsHelper.getVarFloat("Yaw"));
        carTelemetryLiveData.setYawNorth(gameVarUtilsHelper.getVarFloat("YawNorth"));
        carTelemetryLiveData.setYawRate(gameVarUtilsHelper.getVarFloat("YawRate"));

        return carTelemetryLiveData;
    }

    public ConfigLiveData getConfigLiveData() {
        ConfigLiveData configLiveData = new ConfigLiveData();

        configLiveData.setCpuUsageBG(gameVarUtilsHelper.getVarFloat("CpuUsageBG"));
        configLiveData.setFrameRate(gameVarUtilsHelper.getVarFloat("FrameRate"));
        configLiveData.setDisplayUnits(gameVarUtilsHelper.getVarInt("DisplayUnits"));
        configLiveData.setEnterExitReset(gameVarUtilsHelper.getVarInt("EnterExitReset"));
        configLiveData.setDiskLoggingActive(gameVarUtilsHelper.getVarBoolean("DiskLoggingActive"));
        configLiveData.setDiskLoggingEnabled(gameVarUtilsHelper.getVarBoolean("DiskLoggingEnabled"));

        return configLiveData;
    }

    public TeamLiveData getTeamLiveData() {
        TeamLiveData teamLiveData = new TeamLiveData();

        teamLiveData.setDCDriversSoFar(gameVarUtilsHelper.getVarInt("DCDriversSoFar"));
        teamLiveData.setDCLapStatus(gameVarUtilsHelper.getVarInt("DCLapStatus"));

        return teamLiveData;
    }

    public ReplayLiveData getReplayLiveData() {
        ReplayLiveData replayLiveData = new ReplayLiveData();

        replayLiveData.setReplayPlaying(gameVarUtilsHelper.getVarBoolean("ReplayPlaying"));
        replayLiveData.setReplayFrameNum(gameVarUtilsHelper.getVarInt("ReplayFrameNum"));
        replayLiveData.setReplayFrameNumEnd(gameVarUtilsHelper.getVarInt("ReplayFrameNumEnd"));
        replayLiveData.setReplayPlaySlowMotion(gameVarUtilsHelper.getVarBoolean("ReplayPlaySlowMotion"));
        replayLiveData.setReplayPlaySpeed(gameVarUtilsHelper.getVarInt("ReplayPlaySpeed"));
        replayLiveData.setReplaySessionNum(gameVarUtilsHelper.getVarInt("ReplaySessionNum"));
        replayLiveData.setReplaySessionTime(gameVarUtilsHelper.getVarDouble("ReplaySessionTime"));

        return replayLiveData;
    }

    public SessionLiveData getSessionLiveData() {
        SessionLiveData sessionLiveData = new SessionLiveData();

        // TODO: 2 Sep 2020
        int sessionFlags = gameVarUtilsHelper.getVarInt("SessionFlags");
        sessionLiveData.setSessionFlags(new String[0]);

        sessionLiveData.setSessionLapsRemain(gameVarUtilsHelper.getVarInt("SessionLapsRemain"));
        sessionLiveData.setSessionNum(gameVarUtilsHelper.getVarInt("SessionNum"));
        sessionLiveData.setSessionState(SessionState.getStringOf(gameVarUtilsHelper.getVarInt("SessionState")));
        sessionLiveData.setSessionTime(gameVarUtilsHelper.getVarDouble("SessionTime"));
        sessionLiveData.setSessionTimeRemain(gameVarUtilsHelper.getVarDouble("SessionTimeRemain"));
        sessionLiveData.setSessionUniqueID(gameVarUtilsHelper.getVarInt("SessionUniqueID"));

        return sessionLiveData;
    }

    public SteeringWheelLiveData getSteeringWheelLiveData() {
        SteeringWheelLiveData steeringWheelLiveData = new SteeringWheelLiveData();

        steeringWheelLiveData.setSteeringWheelAngle(gameVarUtilsHelper.getVarFloat("SteeringWheelAngle"));
        steeringWheelLiveData.setSteeringWheelAngleMax(gameVarUtilsHelper.getVarFloat("SteeringWheelAngleMax"));
        steeringWheelLiveData.setSteeringWheelPctDamper(gameVarUtilsHelper.getVarFloat("SteeringWheelPctDamper"));
        steeringWheelLiveData.setSteeringWheelPctTorque(gameVarUtilsHelper.getVarFloat("SteeringWheelPctTorque"));
        steeringWheelLiveData.setSteeringWheelPctTorqueSign(gameVarUtilsHelper.getVarFloat("SteeringWheelPctTorqueSign"));
        steeringWheelLiveData.setSteeringWheelPctTorqueSignStops(gameVarUtilsHelper.getVarFloat("SteeringWheelPctTorqueSignStops"));
        steeringWheelLiveData.setSteeringWheelPeakForceNm(gameVarUtilsHelper.getVarFloat("SteeringWheelPeakForceNm"));
        steeringWheelLiveData.setSteeringWheelTorque(gameVarUtilsHelper.getVarFloat("SteeringWheelTorque"));

        return steeringWheelLiveData;
    }

    public TrackLiveData getTrackLiveData() {
        TrackLiveData trackLiveData = new TrackLiveData();

        trackLiveData.setAirDensity(gameVarUtilsHelper.getVarFloat("AirDensity"));
        trackLiveData.setAirPressure(gameVarUtilsHelper.getVarFloat("AirPressure"));
        trackLiveData.setAirTemp(gameVarUtilsHelper.getVarFloat("AirTemp"));
        trackLiveData.setAlt(gameVarUtilsHelper.getVarFloat("Alt"));
        trackLiveData.setFogLevel(gameVarUtilsHelper.getVarFloat("FogLevel"));
        trackLiveData.setRelativeHumidity(gameVarUtilsHelper.getVarFloat("RelativeHumidity"));
        trackLiveData.setSkies(gameVarUtilsHelper.getVarInt("Skies"));
        trackLiveData.setTrackTemp(gameVarUtilsHelper.getVarFloat("TrackTemp"));
        trackLiveData.setTrackTempCrew(gameVarUtilsHelper.getVarFloat("TrackTempCrew"));
        trackLiveData.setWeatherType(gameVarUtilsHelper.getVarInt("WeatherType"));
        trackLiveData.setWindDir(gameVarUtilsHelper.getVarFloat("WindDir"));
        trackLiveData.setWindVel(gameVarUtilsHelper.getVarFloat("WindVel"));

        return trackLiveData;
    }

}
