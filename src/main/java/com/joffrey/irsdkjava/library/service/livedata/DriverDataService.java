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

import com.joffrey.irsdkjava.library.dto.DriverAllDataDto;
import com.joffrey.irsdkjava.library.service.livedata.model.CarLiveData;
import com.joffrey.irsdkjava.library.service.livedata.utils.GameVarUtils;
import com.joffrey.irsdkjava.library.service.yaml.YamlFile;
import com.joffrey.irsdkjava.library.service.yaml.YamlService;
import com.joffrey.irsdkjava.library.service.yaml.irsdkyaml.DriverInfoYaml;
import com.joffrey.irsdkjava.library.service.yaml.irsdkyaml.QualifyResultInfoYaml;
import com.joffrey.irsdkjava.library.service.yaml.irsdkyaml.ResultsFastestLapYaml;
import com.joffrey.irsdkjava.library.service.yaml.irsdkyaml.ResultsPositionsYaml;
import com.joffrey.irsdkjava.library.service.yaml.irsdkyaml.SessionYaml;
import com.joffrey.irsdkjava.sdk.defines.TrkLoc;
import com.joffrey.irsdkjava.sdk.defines.TrkSurf;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverDataService {

    private final        GameVarUtils gameVarUtilsHelper;
    private final        YamlService  yamlService;

    public List<DriverAllDataDto> getDriverAllDataDtoList() {
        List<DriverAllDataDto> driverAllDataDtoList = new ArrayList<>();

        int maxCar = yamlService.getIrsdkYamlFileBean().getDriverInfo().getDrivers().size();
        for (int carIdx = 0; carIdx < maxCar; carIdx++) {
            driverAllDataDtoList.add(getDriverAllDataDto(carIdx));
        }

        return sortLapTimingEntries(driverAllDataDtoList);
    }

    public DriverAllDataDto getDriverAllDataDto(int carIdx) {
        DriverAllDataDto driverAllDataDto = new DriverAllDataDto();

        driverAllDataDto.setDriverInfoYaml(getDriverInfo(carIdx));
        driverAllDataDto.setQualifyResultInfoYaml(getQualifyResults(carIdx));
        driverAllDataDto.setResultsFastestLapYaml(getResultsFastestLap(carIdx));
        driverAllDataDto.setResultsPositionsYaml(getResultsPosition(carIdx));
        driverAllDataDto.setCarLiveData(getCarLiveDataList(carIdx));

        return driverAllDataDto;
    }

    private DriverInfoYaml getDriverInfo(int carIdx) {
        YamlFile irsdkYamlFileBean = yamlService.getIrsdkYamlFileBean();

        return irsdkYamlFileBean.getDriverInfo()
                                .getDrivers()
                                .stream()
                                .filter(driverInfoYaml -> driverInfoYaml.getCarIdx().equals(String.valueOf(carIdx)))
                                .findFirst()
                                .orElse(new DriverInfoYaml());
    }

    private QualifyResultInfoYaml getQualifyResults(int carIdx) {
        YamlFile irsdkYamlFileBean = yamlService.getIrsdkYamlFileBean();

        if (irsdkYamlFileBean.getQualifyResultsInfo() != null) {

            return irsdkYamlFileBean.getQualifyResultsInfo()
                                    .getResults()
                                    .stream()
                                    .filter(qualifyResultInfoYaml -> qualifyResultInfoYaml.getCarIdx()
                                                                                          .equals(String.valueOf(carIdx)))
                                    .findFirst()
                                    .orElse(new QualifyResultInfoYaml());
        } else {
            return new QualifyResultInfoYaml();
        }
    }

    private ResultsPositionsYaml getResultsPosition(int carIdx) {
        YamlFile irsdkYamlFileBean = yamlService.getIrsdkYamlFileBean();

        if (irsdkYamlFileBean.getSessionInfo() != null) {
            for (SessionYaml session : irsdkYamlFileBean.getSessionInfo().getSessions()) {
                if (session.getResultsPositions() != null) {
                    for (ResultsPositionsYaml resultsPosition : session.getResultsPositions()) {
                        if (resultsPosition.getCarIdx().equals(String.valueOf(carIdx))) {
                            return resultsPosition;
                        }
                    }
                }
            }
        }
        return new ResultsPositionsYaml();
    }

    private ResultsFastestLapYaml getResultsFastestLap(int carIdx) {
        YamlFile irsdkYamlFileBean = yamlService.getIrsdkYamlFileBean();

        if (irsdkYamlFileBean.getSessionInfo() != null) {
            return irsdkYamlFileBean.getSessionInfo()
                                    .getSessions()
                                    .stream()
                                    .map(sessionYaml -> sessionYaml.getResultsFastestLap()
                                                                   .stream()
                                                                   .filter(resultsFastestLapYaml -> resultsFastestLapYaml.getCarIdx()
                                                                                                                         .equals(String.valueOf(
                                                                                                                                 carIdx)))
                                                                   .findFirst()
                                                                   .orElse(new ResultsFastestLapYaml()))
                                    .findFirst()
                                    .orElse(new ResultsFastestLapYaml());
        } else {
            return new ResultsFastestLapYaml();
        }
    }

    private CarLiveData getCarLiveDataList(int carIdx) {

        CarLiveData carLiveData = new CarLiveData();

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
        carLiveData.setCarIdxP2P_Count(gameVarUtilsHelper.getVarInt("CarIdxP2P_Count",carIdx));
        carLiveData.setPaceMode(gameVarUtilsHelper.getVarInt("PaceMode", carIdx));
        carLiveData.setCarIdxPaceLine(gameVarUtilsHelper.getVarInt("CarIdxPaceLine", carIdx));
        carLiveData.setCarIdxPaceRow(gameVarUtilsHelper.getVarInt("CarIdxPaceRow", carIdx));
        carLiveData.setCarIdxPaceFlags(gameVarUtilsHelper.getVarInt("CarIdxPaceFlags",carIdx));

        return carLiveData;
    }

    // ============= Utils =============

    /**
     * Sort timing entries, all entries with pos == 0 are add at the end of the list
     *
     * @param driverAllDataDtoList the initial List
     * @return Sorted list with pos == 0 at the end
     */
    private List<DriverAllDataDto> sortLapTimingEntries(List<DriverAllDataDto> driverAllDataDtoList) {
        List<DriverAllDataDto> entriesWithZero = new ArrayList<>();

        // Filter element with pos == 0
        driverAllDataDtoList.forEach(LapTiming -> {
            int driverPos = LapTiming.getCarLiveData().getCarIdxPosition();
            if (driverPos == 0) {
                // Add them in a new list
                entriesWithZero.add(LapTiming);
            }
        });

        // Remove them from initial list
        driverAllDataDtoList.removeIf(l -> l.getCarLiveData().getCarIdxPosition() == 0);

        // Sort initial list
        driverAllDataDtoList.sort(Comparator.comparingInt(value -> value.getCarLiveData().getCarIdxPosition()));

        // Add entries with pos == 0 at the end of the list
        driverAllDataDtoList.addAll(entriesWithZero);
        return driverAllDataDtoList;
    }

}
