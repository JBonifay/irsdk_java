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

package com.joffrey.iracing.irsdkjava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.joffrey.iracing.irsdkjava.model.Header;
import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.telemetry.TelemetryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class TestTelemetryService {

    @MockBean
    private SdkStarter sdkStarter;
    @MockBean
    private Header     header;

    // Class under test
    private TelemetryService telemetryService;

    @BeforeEach
    void init() {
        telemetryService = new TelemetryService(sdkStarter);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
    }

    @DisplayName("loadTelemetryData() -> Test that all fetched vars are ok")
    @Test
    void Given_DataSimulatingMemMapFile_When_loadingDataPacket_ShouldReturnGoodValues() {
        doReturn(100.0f).when(sdkStarter).getVarFloat("Throttle");
        doReturn(50.0f).when(sdkStarter).getVarFloat("Brake");
        doReturn(90.0f).when(sdkStarter).getVarFloat("Clutch");
        doReturn(5).when(sdkStarter).getVarInt("Gear");
        doReturn(50.0f).when(sdkStarter).getVarFloat("ShiftGrindRPM");
        doReturn(4000.0f).when(sdkStarter).getVarFloat("RPM");
        doReturn(260.0f).when(sdkStarter).getVarFloat("Speed");

        doReturn(90.0f).when(sdkStarter).getVarFloat("FuelLevel");
        doReturn(67.0f).when(sdkStarter).getVarFloat("FuelLevelPct");
        doReturn(5.5f).when(sdkStarter).getVarFloat("FuelUsePerHour");
        doReturn(3.0f).when(sdkStarter).getVarFloat("LatAccel");
        doReturn(2.0f).when(sdkStarter).getVarFloat("LongAccel");
        doReturn(90.0f).when(sdkStarter).getVarFloat("SteeringWheelAngle");

        doReturn(60.0f).when(sdkStarter).getVarFloat("AirPressure");
        doReturn(40.0f).when(sdkStarter).getVarFloat("AirTemp");
        doReturn(10.0f).when(sdkStarter).getVarFloat("RelativeHumidity");
        doReturn(1).when(sdkStarter).getVarInt("Skies");
        doReturn(15.0f).when(sdkStarter).getVarFloat("TrackTemp");
        doReturn(5.0f).when(sdkStarter).getVarFloat("WindDir");
        doReturn(1.0f).when(sdkStarter).getVarFloat("WindVel");
        doReturn(1).when(sdkStarter).getVarInt("WeatherType");

        doReturn(54321d).when(sdkStarter).getVarDouble("SessionTime");
        doReturn(9999d).when(sdkStarter).getVarDouble("SessionTimeRemain");
        doReturn(10.50f).when(sdkStarter).getVarFloat("LapBestLapTime");
        doReturn(11).when(sdkStarter).getVarInt("Lap");
        doReturn(30.0f).when(sdkStarter).getVarFloat("LapCurrentLapTime");
        doReturn(2).when(sdkStarter).getVarInt("LapBestLap");
        doReturn(90.0f).when(sdkStarter).getVarFloat("LapDistPct");

        StepVerifier.create(telemetryService.getTelemetryDataFlux()).assertNext(telemetryData -> {
            assertThat(telemetryData.getThrottle()).isEqualTo(100.0f);
            assertThat(telemetryData.getBrake()).isEqualTo(50.0f);
            assertThat(telemetryData.getClutch()).isEqualTo(90.0f);
            assertThat(telemetryData.getGear()).isEqualTo(5);
            assertThat(telemetryData.getShiftGrindRPM()).isEqualTo(50.0f);
            assertThat(telemetryData.getRPM()).isEqualTo(4000.0f);
            assertThat(telemetryData.getSpeed()).isEqualTo(260.0f);
            assertThat(telemetryData.getFuelLevel()).isEqualTo(90.0f);
            assertThat(telemetryData.getFuelLevelPct()).isEqualTo(67.0f);
            assertThat(telemetryData.getFuelUsePerHour()).isEqualTo(5.5f);
            assertThat(telemetryData.getLatAccel()).isEqualTo(3.0f);
            assertThat(telemetryData.getLongAccel()).isEqualTo(2.0f);
            assertThat(telemetryData.getSteeringWheelAngle()).isEqualTo(90.0f);
            assertThat(telemetryData.getAirPressure()).isEqualTo(60.0f);
            assertThat(telemetryData.getAirTemp()).isEqualTo(40.0f);
            assertThat(telemetryData.getRelativeHumidity()).isEqualTo(10.0f);
            assertThat(telemetryData.getSkies()).isEqualTo("Cloudy");
            assertThat(telemetryData.getTrackTemp()).isEqualTo(15.0f);
            assertThat(telemetryData.getWindDir()).isEqualTo(5.0f);
            assertThat(telemetryData.getWindVel()).isEqualTo(1.0f);
            assertThat(telemetryData.getWeatherType()).isEqualTo("Dynamic");
            assertThat(telemetryData.getSessionTime()).isEqualTo(54321d);
            assertThat(telemetryData.getSessionTimeRemain()).isEqualTo(9999d);
            assertThat(telemetryData.getLapBestLapTime()).isEqualTo(10.50f);
            assertThat(telemetryData.getLap()).isEqualTo(11);
            assertThat(telemetryData.getLapCurrentLapTime()).isEqualTo(30.0f);
            assertThat(telemetryData.getLapBestLap()).isEqualTo(2);
            assertThat(telemetryData.getLapDistPct()).isEqualTo(90.0f);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements().hasNotDiscardedElements().hasNotDroppedErrors();


    }


}
