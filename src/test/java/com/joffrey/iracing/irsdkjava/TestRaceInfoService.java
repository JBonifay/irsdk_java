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

import com.joffrey.iracing.irsdkjava.config.FluxProperties;
import com.joffrey.iracing.irsdkjava.model.Header;
import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.raceinfo.RaceInfoService;
import com.joffrey.iracing.irsdkjava.yaml.YamlService;
import com.joffrey.iracing.irsdkjava.yaml.irsdkyaml.YamlFile;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class TestRaceInfoService {

    @MockBean
    private SdkStarter  sdkStarter;
    @MockBean
    private Header      header;
    @MockBean
    private YamlService yamlService;

    // Class under test
    private RaceInfoService raceInfoService;

    // Helpers Objects
    private ByteBuffer byteBufferYamlFile;

    @BeforeEach
    void init() {
        raceInfoService = new RaceInfoService(new FluxProperties(), sdkStarter, yamlService);
        byteBufferYamlFile = createByteBufferYamlFile("raceinfo/raceinfo.yml");
        YamlFile yamlFile = loadYamlObject(byteBufferYamlFile);

        Mockito.when(sdkStarter.getHeader()).thenReturn(header);
        Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
        Mockito.when(sdkStarter.isRunning()).thenReturn(true);
        Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);

        doReturn(9999d).when(sdkStarter).getVarDouble("SessionTimeRemain");
        doReturn(100).when(sdkStarter).getVarInt("SessionLapsRemain");
        doReturn(50.0f).when(sdkStarter).getVarFloat("FuelLevel");
        doReturn(13.7f).when(sdkStarter).getVarFloat("FuelLevelPct");
        doReturn(9.9f).when(sdkStarter).getVarFloat("FuelUsePerHour");
    }


    @DisplayName("loadRaceInfo() - Assert that values are good transformed into Flux object")
    @Test
    void Given_ValuesFromYamlAndMemMapFile_WhenCallingGetRaceInfoFlux_Then_SHouldReturnFluxWithValidValues() {

        StepVerifier.create(raceInfoService.getRaceInfoFlux()).assertNext(raceInfo -> {
            assertThat(raceInfo.getTrackDisplayName()).isEqualTo("Okayama International Circuit");
            assertThat(raceInfo.getTrackConfigName()).isEqualTo("Short");
            assertThat(raceInfo.getTrackCity()).isEqualTo("Okayama");
            assertThat(raceInfo.getTrackCountry()).isEqualTo("Japan");
            assertThat(raceInfo.getTrackLength()).isEqualTo("1.93 km");
            assertThat(raceInfo.getAverageSOF()).isEqualTo(219);
            assertThat(raceInfo.getEventType()).isEqualTo("Practice");
            assertThat(raceInfo.getFuelLevel()).isEqualTo(50.0f);
            assertThat(raceInfo.getFuelLevelPct()).isEqualTo(13.7f);
            assertThat(raceInfo.getFuelUsePerHour()).isEqualTo(9.9f);
            assertThat(raceInfo.getPlayerCarIdx()).isEqualTo(63);
            assertThat(raceInfo.getRemainingLaps()).isEqualTo(100);
            assertThat(raceInfo.getRemainingTime()).isEqualTo(9999.0d);
        }).thenCancel().verifyThenAssertThat().hasNotDroppedElements().hasNotDiscardedElements().hasNotDroppedErrors();
    }


}
