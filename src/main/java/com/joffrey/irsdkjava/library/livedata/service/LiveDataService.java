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
package com.joffrey.irsdkjava.library.livedata.service;

import com.joffrey.irsdkjava.library.livedata.model.camera.CameraLiveData;
import com.joffrey.irsdkjava.library.livedata.model.car.CarLiveData;
import com.joffrey.irsdkjava.library.livedata.model.carsetup.CarSetupLiveData;
import com.joffrey.irsdkjava.library.livedata.model.config.ConfigLiveData;
import com.joffrey.irsdkjava.library.livedata.model.replay.ReplayLiveData;
import com.joffrey.irsdkjava.library.livedata.model.session.SessionLiveData;
import com.joffrey.irsdkjava.library.livedata.model.steeringwheel.SteeringWheelLiveData;
import com.joffrey.irsdkjava.library.livedata.model.team.TeamLiveData;
import com.joffrey.irsdkjava.library.livedata.model.telemetry.TelemetryFuel;
import com.joffrey.irsdkjava.library.livedata.model.telemetry.TelemetryFullData;
import com.joffrey.irsdkjava.library.livedata.model.telemetry.TelemetryGauges;
import com.joffrey.irsdkjava.library.livedata.model.telemetry.TelemetryLapTiming;
import com.joffrey.irsdkjava.library.livedata.model.track.TrackLiveData;
import com.joffrey.irsdkjava.sdk.GameVarUtils;
import com.joffrey.irsdkjava.sdk.defines.CameraState;
import com.joffrey.irsdkjava.sdk.defines.SessionState;
import com.joffrey.irsdkjava.sdk.defines.TrkLoc;
import com.joffrey.irsdkjava.sdk.defines.TrkSurf;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class LiveDataService {

    private final GameVarUtils gameVarUtilsHelper;

    private final TelemetryGauges       telemetryGauges       = new TelemetryGauges();
    private final TelemetryFuel         telemetryFuel         = new TelemetryFuel();
    private final TelemetryFullData     telemetryFullData     = new TelemetryFullData();
    private final ConfigLiveData        configLiveData        = new ConfigLiveData();
    private final CameraLiveData        cameraLiveData        = new CameraLiveData();
    private final CarSetupLiveData      carSetupLiveData      = new CarSetupLiveData();
    private final TeamLiveData          teamLiveData          = new TeamLiveData();
    private final ReplayLiveData        replayLiveData        = new ReplayLiveData();
    private final SessionLiveData       sessionLiveData       = new SessionLiveData();
    private final SteeringWheelLiveData steeringWheelLiveData = new SteeringWheelLiveData();
    private final TrackLiveData         trackLiveData         = new TrackLiveData();
    private final CarLiveData           carLiveData           = new CarLiveData();
    private final TelemetryLapTiming    telemetryLapTiming    = new TelemetryLapTiming();

    public CameraLiveData getCameraLiveData() {
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

    public TelemetryLapTiming getTelemetryLapTiming() {
        telemetryLapTiming.setLap(gameVarUtilsHelper.getVarInt("Lap"));
        telemetryLapTiming.setLapCurrentLapTime(gameVarUtilsHelper.getVarFloat("LapCurrentLapTime"));
        telemetryLapTiming.setLapBestLap(gameVarUtilsHelper.getVarFloat("LapBestLap"));
        telemetryLapTiming.setLapBestLapTime(gameVarUtilsHelper.getVarFloat("LapBestLapTime"));
        telemetryLapTiming.setLapDeltaToBestLap(gameVarUtilsHelper.getVarFloat("LapDeltaToBestLap"));
        telemetryLapTiming.setLapDist(gameVarUtilsHelper.getVarFloat("LapDist"));
        telemetryLapTiming.setLapDistPct(gameVarUtilsHelper.getVarFloat("LapDistPct"));

        return telemetryLapTiming;
    }

    public TelemetryGauges getTelemetryGauges() {
        telemetryGauges.setBrake(gameVarUtilsHelper.getVarFloat("Brake"));
        telemetryGauges.setBrakeRaw(gameVarUtilsHelper.getVarFloat("BrakeRaw"));
        telemetryGauges.setClutch(gameVarUtilsHelper.getVarFloat("Clutch"));
        telemetryGauges.setGear(gameVarUtilsHelper.getVarInt("Gear"));
        telemetryGauges.setRPM(gameVarUtilsHelper.getVarFloat("RPM"));
        telemetryGauges.setShiftGrindRPM(gameVarUtilsHelper.getVarFloat("ShiftGrindRPM"));
        telemetryGauges.setShiftIndicatorPct(gameVarUtilsHelper.getVarFloat("ShiftIndicatorPct"));
        telemetryGauges.setShiftPowerPct(gameVarUtilsHelper.getVarFloat("ShiftPowerPct"));
        telemetryGauges.setSpeed(gameVarUtilsHelper.getVarFloat("Speed"));
        telemetryGauges.setThrottle(gameVarUtilsHelper.getVarFloat("Throttle"));
        telemetryGauges.setThrottleRaw(gameVarUtilsHelper.getVarFloat("ThrottleRaw"));

        return telemetryGauges;
    }

    public TelemetryFuel getTelemetryFuel() {
        telemetryFuel.setFuelLevel(gameVarUtilsHelper.getVarFloat("FuelLevel"));
        telemetryFuel.setFuelLevelPct(gameVarUtilsHelper.getVarFloat("FuelLevelPct"));
        telemetryFuel.setFuelPress(gameVarUtilsHelper.getVarFloat("FuelPress"));
        telemetryFuel.setFuelUsePerHour(gameVarUtilsHelper.getVarFloat("FuelUsePerHour"));

        return telemetryFuel;
    }

    public TelemetryFullData getCarTelemetryFullData() {
        telemetryFullData.setBrake(gameVarUtilsHelper.getVarFloat("Brake"));
        telemetryFullData.setBrakeRaw(gameVarUtilsHelper.getVarFloat("BrakeRaw"));
        telemetryFullData.setClutch(gameVarUtilsHelper.getVarFloat("Clutch"));
        telemetryFullData.setDriverMarker(gameVarUtilsHelper.getVarBoolean("DriverMarker"));

        // TODO: 2 Sep 2020
        int engineWarnings = gameVarUtilsHelper.getVarInt("EngineWarnings");
        telemetryFullData.setEngineWarnings(new String[0]);

        telemetryFullData.setFuelLevel(gameVarUtilsHelper.getVarFloat("FuelLevel"));
        telemetryFullData.setFuelLevelPct(gameVarUtilsHelper.getVarFloat("FuelLevelPct"));
        telemetryFullData.setFuelPress(gameVarUtilsHelper.getVarFloat("FuelPress"));
        telemetryFullData.setFuelUsePerHour(gameVarUtilsHelper.getVarFloat("FuelUsePerHour"));
        telemetryFullData.setGear(gameVarUtilsHelper.getVarInt("Gear"));
        telemetryFullData.setInGarage(gameVarUtilsHelper.getVarBoolean("IsInGarage"));
        telemetryFullData.setOnTrack(gameVarUtilsHelper.getVarBoolean("IsOnTrack"));
        telemetryFullData.setOnTrackCar(gameVarUtilsHelper.getVarBoolean("IsOnTrackCar"));
        telemetryFullData.setLap(gameVarUtilsHelper.getVarInt("Lap"));
        telemetryFullData.setLapBestLap(gameVarUtilsHelper.getVarInt("LapBestLap"));
        telemetryFullData.setLapBestLapTime(gameVarUtilsHelper.getVarFloat("LapBestLapTime"));
        telemetryFullData.setLapBestNLapLap(gameVarUtilsHelper.getVarInt("LapBestNLapLap"));
        telemetryFullData.setLapBestNLapTime(gameVarUtilsHelper.getVarFloat("LapBestNLapTime"));
        telemetryFullData.setLapCurrentLapTime(gameVarUtilsHelper.getVarFloat("LapCurrentLapTime"));
        telemetryFullData.setLapDeltaToBestLap(gameVarUtilsHelper.getVarFloat("LapDeltaToBestLap"));
        telemetryFullData.setLapDeltaToBestLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToBestLap_DD"));
        telemetryFullData.setLapDeltaToBestLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToBestLap_OK"));
        telemetryFullData.setLapDeltaToOptimalLap(gameVarUtilsHelper.getVarFloat("LapDeltaToOptimalLap"));
        telemetryFullData.setLapDeltaToOptimalLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToOptimalLap_DD"));
        telemetryFullData.setLapDeltaToOptimalLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToOptimalLap_OK"));
        telemetryFullData.setLapDeltaToSessionBestLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionBestLap"));
        telemetryFullData.setLapDeltaToSessionBestLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionBestLap_DD"));
        telemetryFullData.setLapDeltaToSessionBestLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionBestLap_OK"));
        telemetryFullData.setLapDeltaToSessionLastlLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionLastlLap"));
        telemetryFullData.setLapDeltaToSessionLastlLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionLastlLap_DD"));
        telemetryFullData.setLapDeltaToSessionLastlLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionLastlLap_OK"));
        telemetryFullData.setLapDeltaToSessionOptimalLap(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionOptimalLap"));
        telemetryFullData.setLapDeltaToSessionOptimalLap_DD(gameVarUtilsHelper.getVarFloat("LapDeltaToSessionOptimalLap_DD"));
        telemetryFullData.setLapDeltaToSessionOptimalLap_OK(gameVarUtilsHelper.getVarBoolean("LapDeltaToSessionOptimalLap_OK"));
        telemetryFullData.setLapDist(gameVarUtilsHelper.getVarFloat("LapDist"));
        telemetryFullData.setLapDistPct(gameVarUtilsHelper.getVarFloat("LapDistPct"));
        telemetryFullData.setLapLasNLapSeq(gameVarUtilsHelper.getVarInt("LapLasNLapSeq"));
        telemetryFullData.setLapLastLapTime(gameVarUtilsHelper.getVarFloat("LapLastLapTime"));
        telemetryFullData.setLapLastNLapTime(gameVarUtilsHelper.getVarFloat("LapLastNLapTime"));
        telemetryFullData.setLat(gameVarUtilsHelper.getVarDouble("Lat"));
        telemetryFullData.setLatAccel(gameVarUtilsHelper.getVarFloat("LatAccel"));
        telemetryFullData.setLon(gameVarUtilsHelper.getVarDouble("Lon"));
        telemetryFullData.setLonAccel(gameVarUtilsHelper.getVarFloat("LonAccel"));
        telemetryFullData.setManifoldPress(gameVarUtilsHelper.getVarFloat("ManifoldPress"));
        telemetryFullData.setOilLevel(gameVarUtilsHelper.getVarFloat("OilLevel"));
        telemetryFullData.setOilPress(gameVarUtilsHelper.getVarFloat("OilPress"));
        telemetryFullData.setOilTemp(gameVarUtilsHelper.getVarFloat("OilTemp"));
        telemetryFullData.setOnPitRoad(gameVarUtilsHelper.getVarBoolean("OnPitRoad"));
        telemetryFullData.setPitch(gameVarUtilsHelper.getVarFloat("Pitch"));
        telemetryFullData.setPitchRate(gameVarUtilsHelper.getVarFloat("PitchRate"));
        telemetryFullData.setPitOptRepairLeft(gameVarUtilsHelper.getVarFloat("PitOptRepairLeft"));
        telemetryFullData.setPitRepairLeft(gameVarUtilsHelper.getVarFloat("PitRepairLeft"));

        // TODO: 2 Sep 2020
        int pitSvFlags = gameVarUtilsHelper.getVarInt("PitSvFlags");
        telemetryFullData.setPitSvFlags(new String[0]);

        telemetryFullData.setPitSvFuel(gameVarUtilsHelper.getVarFloat("PitSvFuel"));
        telemetryFullData.setPitSvLFP(gameVarUtilsHelper.getVarFloat("PitSvLFP"));
        telemetryFullData.setPitSvLRP(gameVarUtilsHelper.getVarFloat("PitSvLRP"));
        telemetryFullData.setPitSvRFP(gameVarUtilsHelper.getVarFloat("PitSvRFP"));
        telemetryFullData.setPitSvRRP(gameVarUtilsHelper.getVarFloat("PitSvRRP"));
        telemetryFullData.setPlayerCarClassPosition(gameVarUtilsHelper.getVarInt("PlayerCarClassPosition"));
        telemetryFullData.setPlayerCarPosition(gameVarUtilsHelper.getVarInt("PlayerCarPosition"));
        telemetryFullData.setRadioTransmitCarIdx(gameVarUtilsHelper.getVarInt("RadioTransmitCarIdx"));
        telemetryFullData.setRadioTransmitFrequencyIdx(gameVarUtilsHelper.getVarInt("RadioTransmitFrequencyIdx"));
        telemetryFullData.setRadioTransmitRadioIdx(gameVarUtilsHelper.getVarInt("RadioTransmitRadioIdx"));
        telemetryFullData.setRoll(gameVarUtilsHelper.getVarFloat("Roll"));
        telemetryFullData.setRollRate(gameVarUtilsHelper.getVarFloat("RollRate"));
        telemetryFullData.setRPM(gameVarUtilsHelper.getVarFloat("RPM"));
        telemetryFullData.setShiftGrindRPM(gameVarUtilsHelper.getVarFloat("ShiftGrindRPM"));
        telemetryFullData.setShiftIndicatorPct(gameVarUtilsHelper.getVarFloat("ShiftIndicatorPct"));
        telemetryFullData.setShiftPowerPct(gameVarUtilsHelper.getVarFloat("ShiftPowerPct"));
        telemetryFullData.setSpeed(gameVarUtilsHelper.getVarFloat("Speed"));
        telemetryFullData.setThrottle(gameVarUtilsHelper.getVarFloat("Throttle"));
        telemetryFullData.setThrottleRaw(gameVarUtilsHelper.getVarFloat("ThrottleRaw"));
        telemetryFullData.setVelocityX(gameVarUtilsHelper.getVarFloat("VelocityX"));
        telemetryFullData.setVelocityY(gameVarUtilsHelper.getVarFloat("VelocityY"));
        telemetryFullData.setVelocityZ(gameVarUtilsHelper.getVarFloat("VelocityZ"));
        telemetryFullData.setVertAccel(gameVarUtilsHelper.getVarFloat("VertAccel"));
        telemetryFullData.setVoltage(gameVarUtilsHelper.getVarFloat("Voltage"));
        telemetryFullData.setWaterLevel(gameVarUtilsHelper.getVarFloat("WaterLevel"));
        telemetryFullData.setWaterTemp(gameVarUtilsHelper.getVarFloat("WaterTemp"));
        telemetryFullData.setYaw(gameVarUtilsHelper.getVarFloat("Yaw"));
        telemetryFullData.setYawNorth(gameVarUtilsHelper.getVarFloat("YawNorth"));
        telemetryFullData.setYawRate(gameVarUtilsHelper.getVarFloat("YawRate"));

        return telemetryFullData;
    }

    public ConfigLiveData getConfigLiveData() {
        configLiveData.setCpuUsageBG(gameVarUtilsHelper.getVarFloat("CpuUsageBG"));
        configLiveData.setFrameRate(gameVarUtilsHelper.getVarFloat("FrameRate"));
        configLiveData.setDisplayUnits(gameVarUtilsHelper.getVarInt("DisplayUnits"));
        configLiveData.setEnterExitReset(gameVarUtilsHelper.getVarInt("EnterExitReset"));
        configLiveData.setDiskLoggingActive(gameVarUtilsHelper.getVarBoolean("DiskLoggingActive"));
        configLiveData.setDiskLoggingEnabled(gameVarUtilsHelper.getVarBoolean("DiskLoggingEnabled"));

        return configLiveData;
    }

    public TeamLiveData getTeamLiveData() {
        teamLiveData.setDCDriversSoFar(gameVarUtilsHelper.getVarInt("DCDriversSoFar"));
        teamLiveData.setDCLapStatus(gameVarUtilsHelper.getVarInt("DCLapStatus"));

        return teamLiveData;
    }

    public ReplayLiveData getReplayLiveData() {
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
        // TODO: 2 Sep 2020
        int sessionFlags = gameVarUtilsHelper.getVarInt("SessionFlags");
        sessionLiveData.setSessionFlags(new String[0]);

        sessionLiveData.setSessionLapsRemain(gameVarUtilsHelper.getVarInt("SessionLapsRemain"));
        sessionLiveData.setSessionNum(gameVarUtilsHelper.getVarInt("SessionNum"));
        sessionLiveData.setSessionState(SessionState.getStringOf(gameVarUtilsHelper.getVarInt("SessionState")));
        sessionLiveData.setSessionTime(gameVarUtilsHelper.getVarDouble("SessionTime"));
        sessionLiveData.setSessionTimeRemain(gameVarUtilsHelper.getVarDouble("SessionTimeRemain"));
        sessionLiveData.setSessionUniqueID(gameVarUtilsHelper.getVarInt("SessionUniqueID"));
        sessionLiveData.setRaceLaps(gameVarUtilsHelper.getVarInt("RaceLaps"));

        return sessionLiveData;
    }

    public SteeringWheelLiveData getSteeringWheelLiveData() {
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

    public CarLiveData getCarLiveData(int carIdx) {
        carLiveData.setCarIdxClassPosition(gameVarUtilsHelper.getVarInt("CarIdxClassPosition", carIdx));
        carLiveData.setCarIdxEstTime(gameVarUtilsHelper.getVarFloat("CarIdxEstTime", carIdx));
        carLiveData.setCarIdxF2Time(gameVarUtilsHelper.getVarFloat("CarIdxF2Time", carIdx));
        carLiveData.setCarIdxGear(gameVarUtilsHelper.getVarInt("CarIdxGear", carIdx));
        carLiveData.setCarIdxLap(gameVarUtilsHelper.getVarInt("CarIdxLap", carIdx));
        carLiveData.setCarIdxLapDistPct(gameVarUtilsHelper.getVarFloat("CarIdxLapDistPct", carIdx));
        carLiveData.setCarIdxOnPitRoad(gameVarUtilsHelper.getVarBoolean("CarIdxOnPitRoad", carIdx));
        carLiveData.setCarIdxPosition(gameVarUtilsHelper.getVarInt("CarIdxPosition", carIdx));
        carLiveData.setCarIdxRPM(gameVarUtilsHelper.getVarFloat("CarIdxRPM", carIdx));
        carLiveData.setCarIdxSteer(gameVarUtilsHelper.getVarFloat("CarIdxSteer", carIdx));
        carLiveData.setCarIdxTrackSurface(TrkLoc.valueOf(gameVarUtilsHelper.getVarInt("CarIdxTrackSurface", carIdx)));
        carLiveData.setCarIdxTrackSurface(TrkSurf.valueOf(gameVarUtilsHelper.getVarInt("carIdxTrackSurfaceMaterial", carIdx)));
        carLiveData.setCarIdxLastLapTime(gameVarUtilsHelper.getVarFloat("CarIdxLastLapTime", carIdx));
        carLiveData.setCarIdxBestLapTime(gameVarUtilsHelper.getVarFloat("CarIdxBestLapTime", carIdx));
        carLiveData.setCarIdxBestLapNum(gameVarUtilsHelper.getVarInt("carIdxBestLapNum", carIdx));
        carLiveData.setCarIdxP2P_Status(gameVarUtilsHelper.getVarBoolean("CarIdxP2P_Status", carIdx));
        carLiveData.setCarIdxP2P_Count(gameVarUtilsHelper.getVarInt("CarIdxP2P_Count", carIdx));
        carLiveData.setPaceMode(gameVarUtilsHelper.getVarInt("PaceMode", carIdx));
        carLiveData.setCarIdxPaceLine(gameVarUtilsHelper.getVarInt("CarIdxPaceLine", carIdx));
        carLiveData.setCarIdxPaceRow(gameVarUtilsHelper.getVarInt("CarIdxPaceRow", carIdx));
        carLiveData.setCarIdxPaceFlags(gameVarUtilsHelper.getVarInt("CarIdxPaceFlags", carIdx));

        return carLiveData;
    }

    public void recordLapTiming() {

        int lap = gameVarUtilsHelper.getVarInt("Lap");

        if (lap > 0) {

            while (telemetryLapTiming.getLapTime().size() < lap) {
                telemetryLapTiming.getLapTime().add(0.0f);
            }

            if (telemetryLapTiming.getLapTime().get(lap - 1) == 0.0f) {
                telemetryLapTiming.getLapTime().add(lap - 1, gameVarUtilsHelper.getVarFloat("LapLastLapTime"));
            }
        }

    }
}
