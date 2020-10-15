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

package com.joffrey.irsdkjava;

import static org.assertj.core.api.Assertions.assertThat;

import com.joffrey.irsdkjava.model.Header;
import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.telemetry.TelemetryService;
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
        Mockito.when(sdkStarter.getVarFloat("Throttle")).thenReturn(100.0f);
        Mockito.when(sdkStarter.getVarFloat("Brake")).thenReturn(50.0f);
        Mockito.when(sdkStarter.getVarFloat("Clutch")).thenReturn(90.0f);
        Mockito.when(sdkStarter.getVarInt("Gear")).thenReturn(5);
        Mockito.when(sdkStarter.getVarFloat("ShiftGrindRPM")).thenReturn(50.0f);
        Mockito.when(sdkStarter.getVarFloat("RPM")).thenReturn(4000.0f);
        Mockito.when(sdkStarter.getVarFloat("Speed")).thenReturn(260.0f);

        Mockito.when(sdkStarter.getVarFloat("FuelLevel")).thenReturn(90.0f);
        Mockito.when(sdkStarter.getVarFloat("FuelLevelPct")).thenReturn(67.0f);
        Mockito.when(sdkStarter.getVarFloat("FuelUsePerHour")).thenReturn(5.5f);
        Mockito.when(sdkStarter.getVarFloat("LatAccel")).thenReturn(3.0f);
        Mockito.when(sdkStarter.getVarFloat("LongAccel")).thenReturn(2.0f);
        Mockito.when(sdkStarter.getVarFloat("SteeringWheelAngle")).thenReturn(90.0f);

        Mockito.when(sdkStarter.getVarFloat("AirPressure")).thenReturn(60.0f);
        Mockito.when(sdkStarter.getVarFloat("AirTemp")).thenReturn(40.0f);
        Mockito.when(sdkStarter.getVarFloat("RelativeHumidity")).thenReturn(10.0f);
        Mockito.when(sdkStarter.getVarInt("Skies")).thenReturn(1);
        Mockito.when(sdkStarter.getVarFloat("TrackTemp")).thenReturn(15.0f);
        Mockito.when(sdkStarter.getVarFloat("WindDir")).thenReturn(5.0f);
        Mockito.when(sdkStarter.getVarFloat("WindVel")).thenReturn(1.0f);
        Mockito.when(sdkStarter.getVarInt("WeatherType")).thenReturn(1);

        Mockito.when(sdkStarter.getVarDouble("SessionTime")).thenReturn(54321d);
        Mockito.when(sdkStarter.getVarDouble("SessionTimeRemain")).thenReturn(9999d);
        Mockito.when(sdkStarter.getVarFloat("LapBestLapTime")).thenReturn(10.50f);
        Mockito.when(sdkStarter.getVarInt("Lap")).thenReturn(11);
        Mockito.when(sdkStarter.getVarFloat("LapCurrentLapTime")).thenReturn(30.0f);
        Mockito.when(sdkStarter.getVarInt("LapBestLap")).thenReturn(2);
        Mockito.when(sdkStarter.getVarFloat("LapDistPct")).thenReturn(90.0f);

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
