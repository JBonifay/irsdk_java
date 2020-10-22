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

import static com.joffrey.iracing.irsdkjava.YamlHelperTest.createByteBufferYamlFile;
import static com.joffrey.iracing.irsdkjava.YamlHelperTest.loadYamlObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.joffrey.iracing.irsdkjava.laptiming.LapTimingService;
import com.joffrey.iracing.irsdkjava.model.Header;
import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.yaml.YamlService;
import com.joffrey.iracing.irsdkjava.yaml.irsdkyaml.YamlFile;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class TestLapTimingService {

    @MockBean
    private SdkStarter  sdkStarter;
    @MockBean
    private Header      header;
    @MockBean
    private YamlService yamlService;

    // Class under test
    private LapTimingService lapTimingService;

    // Helpers Objects
    private ByteBuffer byteBufferYamlFile;

    void setupGeneral() {
        lapTimingService = new LapTimingService(sdkStarter, yamlService);
        YamlFile yamlFile = loadYamlObject(byteBufferYamlFile);

        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
        Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);

    }


    @DisplayName("getLapTimingDataListFlux() - One Driver: Check if values from MemMapFile and Yaml are ok, values are simulated from a custom yaml, and Mock method calling Memory Mapped File")
    @Test
    void Given_OneDriverWithData_When_SubscribingToLapTimingFlux_Then_ShouldReturnFluxOfOneDriverWithValidData() {
        // Generate Fake data for replace data from MemoryMappedFile
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_one_driver.yml");

        doReturn(1).when(sdkStarter).getVarInt("CarIdxPosition", 0);
        doReturn(1).when(sdkStarter).getVarInt("CarIdxClassPosition", 0);
        doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxF2Time", 0);
        doReturn(1).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(30.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(0f).when(sdkStarter).getVarFloat("CarIdxLastLapTime", 0);
        doReturn(0f).when(sdkStarter).getVarFloat("CarIdxBestLapTime", 0);
        doReturn(0).when(sdkStarter).getVarInt("CarIdxTrackSurface", 0);

        setupGeneral();

        // Launch flux, expect that the flux returned is filled with data generated with 'setupVarsForOneDriver()'
        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isZero();
            assertThat(lapTimingData.get(0).getUserName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxPosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxClassPosition()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxEstTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxF2Time()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxLap()).isEqualTo(1);
            assertThat(lapTimingData.get(0).getCarIdxLapDistPct()).isEqualTo(30.0f);
            assertThat(lapTimingData.get(0).getCarIdxLastLapTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxBestLapTime()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarIdxTrackSurface()).isEqualTo("irsdk_OffTrack");
            assertThat(lapTimingData.get(0).getUserName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getTeamName()).isEqualTo("Joffrey Bonifay");
            assertThat(lapTimingData.get(0).getCarNumber()).isEmpty();
            assertThat(lapTimingData.get(0).getIRating()).isEqualTo("876");
            assertThat(lapTimingData.get(0).getLicLevel()).isEqualTo("7");
            assertThat(lapTimingData.get(0).getLicString()).isEqualTo("D 3.41");
            assertThat(lapTimingData.get(0).getLicColor()).isEqualTo("0xfc8a27");
            assertThat(lapTimingData.get(0).getIsSpectator()).isEqualTo("1");
            assertThat(lapTimingData.get(0).getClubName()).isEqualTo("France");
            assertThat(lapTimingData.get(0).getDivisionName()).isEqualTo("Division 1");
        }).thenCancel().log().verify();
    }

    // getLapTimingDataComparator()
    @DisplayName("getLapTimingDataComparator() - Should sort player positions by lapPct ")
    @ParameterizedTest
    @CsvSource({"30.0f, 80.0f, 70.0f, 60.0f, 3, 0, 1, 2",
            "90.0f, 30.0f, 70.0f, 60.0f, 0, 3, 1, 2",
            "90.0f, 80.0f, 30.0f, 60.0f, 0, 1, 3, 2",
            "90.0f, 80.0f, 70.0f, 30.0f, 0, 1, 2, 3"})
    void Given_DriversList_When_GettingFlux_DriversShouldBeOrderedByLivePositions(float firstPct, float secondPct, float thirdPct,
            float fourthPct, int firstIdxExpected, int secondIdxExpected, int thirdIdxExpected, int fourthIdxExpected) {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_four_driver.yml");

        doReturn(firstPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(secondPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(thirdPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(fourthPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);
            assertThat(lapTimingData.get(1).getCarLivePosition()).isEqualTo(2);
            assertThat(lapTimingData.get(2).getCarLivePosition()).isEqualTo(3);
            assertThat(lapTimingData.get(3).getCarLivePosition()).isEqualTo(4);

            assertThat(lapTimingData.get(firstIdxExpected).getCarIdxLapDistPct()).isEqualTo(firstPct);
            assertThat(lapTimingData.get(secondIdxExpected).getCarIdxLapDistPct()).isEqualTo(secondPct);
            assertThat(lapTimingData.get(thirdIdxExpected).getCarIdxLapDistPct()).isEqualTo(thirdPct);
            assertThat(lapTimingData.get(fourthIdxExpected).getCarIdxLapDistPct()).isEqualTo(fourthPct);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements();
    }

    @DisplayName("getLapTimingDataComparator() - Should sort player positions by pct, drivers are on different laps ")
    @ParameterizedTest
    @CsvSource({"30.0f, 80.0f, 70.0f, 60.0f,    1, 2, 3, 0,     1, 2, 1, 1",
            "90.0f, 30.0f, 70.0f, 60.0f,    0, 2, 3, 1,     2, 1, 1, 1",
            "90.0f, 80.0f, 30.0f, 60.0f,    0, 2, 1, 3,     2, 1, 2, 1",
            "90.0f, 80.0f, 70.0f, 30.0f,    1, 2, 3, 0,     1, 2, 2, 2"})
    void Given_DriversListWithDifferentsLapIdx_When_GettingFlux_DriversShouldBeOrderedByLivePositions(float firstPct,
            float secondPct, float thirdPct, float fourthPct, int firstIdxExpected, int secondIdxExpected, int thirdIdxExpected,
            int fourthIdxExpected, int firstPlayerLap, int secondPlayerLap, int thirdPlayerLap, int fourthPlayerLap) {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_four_driver.yml");

        doReturn(firstPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(secondPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(thirdPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(fourthPct).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);

        doReturn(firstPlayerLap).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(secondPlayerLap).when(sdkStarter).getVarInt("CarIdxLap", 1);
        doReturn(thirdPlayerLap).when(sdkStarter).getVarInt("CarIdxLap", 2);
        doReturn(fourthPlayerLap).when(sdkStarter).getVarInt("CarIdxLap", 3);

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isEqualTo(firstIdxExpected);
            assertThat(lapTimingData.get(1).getCarIdx()).isEqualTo(secondIdxExpected);
            assertThat(lapTimingData.get(2).getCarIdx()).isEqualTo(thirdIdxExpected);
            assertThat(lapTimingData.get(3).getCarIdx()).isEqualTo(fourthIdxExpected);

        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements();
    }

    // SetDriversInterval()
    @DisplayName("setDriversInterval() - One Driver: When list of player is created, the interval between players should be set from EstTime difference")
    @ParameterizedTest
    @ValueSource(floats = {10.0f, 20.0f, 40.0f})
    void Given_OneDriversEstimatedTimeValues_When_GettingFlux_Then_IntervalShouldBeSetAndEqualsToDriversEstTimeSubtraction(
            float firstDriverEstTime) {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_one_driver.yml");

        doReturn(firstDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            // Assert That the leader interval (idx = 0) is equals to zero
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements();
    }

    @DisplayName("setDriversInterval() - Two Drivers: When list of player is created, the interval between players should be set from EstTime difference")
    @ParameterizedTest
    @CsvSource({"10.0f,   0.0f,  10.0f", "40.0f,  30.0f,  10.0f", "99.9f,  10.0f,  89.9f"})
    void Given_TwoDriversEstimatedTimeValues_When_GettingFlux_Then_IntervalShouldBeSetAndEqualsToDriversEstTimeSubtraction(
            float firstDriverEstTime, float secondDriverEstTime, float realInterval) {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_two_driver.yml");

        doReturn(firstDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(secondDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);

        // Set drivers a CarIdxLapDistPct for simulate ordering -> list is sorted by CarIdxLapDistPct
        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(9.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            // Assert That the leader interval (idx = 0) is equals to zero
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);

            // Assert that the interval as been set
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(realInterval);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements();
    }

    @DisplayName("setDriversInterval() - Four Drivers: When list of player is created, the interval between players should be set from EstTime difference")
    @ParameterizedTest
    @CsvSource({"40.0f,  30.0f,  20.0f, 10.0f", "90.0f,  70.0f,  50.0f, 10.0f", "41.0f,  40.0f,  39.9f, 39.8f"})
    void Given_FourDriversEstimatedTimeValues_When_GettingFlux_Then_IntervalShouldBeSetAndEqualsToDriversEstTimeSubtraction(
            float firstDriverEstTime, float secondDriverEstTime, float thirdDriverEstTime, float fourthDriverEstTime) {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_four_driver.yml");

        doReturn(firstDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(secondDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);
        doReturn(thirdDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 2);
        doReturn(fourthDriverEstTime).when(sdkStarter).getVarFloat("CarIdxEstTime", 3);

        // Set drivers a CarIdxLapDistPct for simulate ordering -> list is sorted by CarIdxLapDistPct
        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(9.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(8.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(7.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).assertNext(lapTimingData -> {
            // Assert That the leader interval (idx = 0) is equals to zero
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(firstDriverEstTime - secondDriverEstTime);
            assertThat(lapTimingData.get(2).getCarIntervalWithPreviousCar()).isEqualTo(secondDriverEstTime - thirdDriverEstTime);
            assertThat(lapTimingData.get(3).getCarIntervalWithPreviousCar()).isEqualTo(thirdDriverEstTime - fourthDriverEstTime);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements();
    }

    @DisplayName("Simulation test -> we simulate here some laps with differents data, flux should handle it, sort the list, set the intervals and drivers livePositions")
    @Test
    void Given_MultipleLapsData_When_SubscribingToFlux_ShouldReturnFluxSortedAndValuesShouldBeOk() {
        byteBufferYamlFile = createByteBufferYamlFile("laptiming/Laptiming_four_driver.yml");

        setupGeneral();

        StepVerifier.create(lapTimingService.getLapTimingDataListFlux()).then(this::startingLine).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isEqualTo(0);
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);

            assertThat(lapTimingData.get(1).getCarIdx()).isEqualTo(1);
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(0.5f);
            assertThat(lapTimingData.get(1).getCarLivePosition()).isEqualTo(2);

            assertThat(lapTimingData.get(2).getCarIdx()).isEqualTo(2);
            assertThat(lapTimingData.get(2).getCarIntervalWithPreviousCar()).isEqualTo(0.5f);
            assertThat(lapTimingData.get(2).getCarLivePosition()).isEqualTo(3);

            assertThat(lapTimingData.get(3).getCarIdx()).isEqualTo(3);
            assertThat(lapTimingData.get(3).getCarIntervalWithPreviousCar()).isEqualTo(0.5f);
            assertThat(lapTimingData.get(3).getCarLivePosition()).isEqualTo(4);
        }).then(this::lapOne).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isEqualTo(0);
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);

            assertThat(lapTimingData.get(1).getCarIdx()).isEqualTo(1);
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(5.0f);
            assertThat(lapTimingData.get(1).getCarLivePosition()).isEqualTo(2);

            assertThat(lapTimingData.get(2).getCarIdx()).isEqualTo(3);
            assertThat(lapTimingData.get(2).getCarIntervalWithPreviousCar()).isEqualTo(1.0f);
            assertThat(lapTimingData.get(2).getCarLivePosition()).isEqualTo(3);

            assertThat(lapTimingData.get(3).getCarIdx()).isEqualTo(2);
            assertThat(lapTimingData.get(3).getCarIntervalWithPreviousCar()).isEqualTo(1.0f);
            assertThat(lapTimingData.get(3).getCarLivePosition()).isEqualTo(4);
        }).then(this::lapTwo).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isEqualTo(3);
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);

            assertThat(lapTimingData.get(1).getCarIdx()).isEqualTo(2);
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(40.0f);
            assertThat(lapTimingData.get(1).getCarLivePosition()).isEqualTo(2);

            assertThat(lapTimingData.get(2).getCarIdx()).isEqualTo(1);
            assertThat(lapTimingData.get(2).getCarIntervalWithPreviousCar()).isEqualTo(20.0f);
            assertThat(lapTimingData.get(2).getCarLivePosition()).isEqualTo(3);

            assertThat(lapTimingData.get(3).getCarIdx()).isEqualTo(0);
            assertThat(lapTimingData.get(3).getCarIntervalWithPreviousCar()).isEqualTo(20.0f);
            assertThat(lapTimingData.get(3).getCarLivePosition()).isEqualTo(4);
        }).then(this::lapThree).assertNext(lapTimingData -> {
            assertThat(lapTimingData.get(0).getCarIdx()).isEqualTo(0);
            assertThat(lapTimingData.get(0).getCarIntervalWithPreviousCar()).isEqualTo(0.0f);
            assertThat(lapTimingData.get(0).getCarLivePosition()).isEqualTo(1);

            assertThat(lapTimingData.get(1).getCarIdx()).isEqualTo(2);
            assertThat(lapTimingData.get(1).getCarIntervalWithPreviousCar()).isEqualTo(3.0f);
            assertThat(lapTimingData.get(1).getCarLivePosition()).isEqualTo(2);

            assertThat(lapTimingData.get(2).getCarIdx()).isEqualTo(3);
            assertThat(lapTimingData.get(2).getCarIntervalWithPreviousCar()).isEqualTo(14.0f);
            assertThat(lapTimingData.get(2).getCarLivePosition()).isEqualTo(3);

            assertThat(lapTimingData.get(3).getCarIdx()).isEqualTo(1);
            assertThat(lapTimingData.get(3).getCarIntervalWithPreviousCar()).isEqualTo(22.0f);
            assertThat(lapTimingData.get(3).getCarLivePosition()).isEqualTo(4);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedErrors().hasNotDroppedElements().hasNotDiscardedElements();
    }

    private void startingLine() {
        doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(-0.5f).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);
        doReturn(-1.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 2);
        doReturn(-1.5f).when(sdkStarter).getVarFloat("CarIdxEstTime", 3);

        doReturn(0).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(0).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(0).when(sdkStarter).getVarInt("CarIdxLap", 2);
        doReturn(0).when(sdkStarter).getVarInt("CarIdxLap", 3);

        doReturn(1.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(0.6f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(0.3f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);
    }

    private void lapOne() {
        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(5.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);
        doReturn(3.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 2);
        doReturn(4.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 3);

        doReturn(1).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(1).when(sdkStarter).getVarInt("CarIdxLap", 1);
        doReturn(1).when(sdkStarter).getVarInt("CarIdxLap", 2);
        doReturn(1).when(sdkStarter).getVarInt("CarIdxLap", 3);

        doReturn(15.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(8.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(9.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);
    }

    private void lapTwo() {
        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(30.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);
        doReturn(50.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 2);
        doReturn(90.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 3);

        doReturn(2).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(2).when(sdkStarter).getVarInt("CarIdxLap", 1);
        doReturn(2).when(sdkStarter).getVarInt("CarIdxLap", 2);
        doReturn(2).when(sdkStarter).getVarInt("CarIdxLap", 3);

        doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(30.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(50.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(90.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);
    }

    /*
     * In this lap we simulate the fact that
     *
     */
    private void lapThree() {
        doReturn(60.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 0);
        doReturn(65.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 1);
        doReturn(57.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 2);
        doReturn(43.0f).when(sdkStarter).getVarFloat("CarIdxEstTime", 3);

        doReturn(4).when(sdkStarter).getVarInt("CarIdxLap", 0);
        doReturn(3).when(sdkStarter).getVarInt("CarIdxLap", 1);
        doReturn(4).when(sdkStarter).getVarInt("CarIdxLap", 2);
        doReturn(4).when(sdkStarter).getVarInt("CarIdxLap", 3);

        doReturn(60.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 0);
        doReturn(65.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 1);
        doReturn(57.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 2);
        doReturn(43.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 3);
    }


}
