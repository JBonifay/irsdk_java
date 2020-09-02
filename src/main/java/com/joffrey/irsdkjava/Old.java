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

package com.joffrey.irsdkjava;

import com.joffrey.irsdkjava.sdk.yaml.IrsdkYamlFileDto;
import com.joffrey.irsdkjava.sdk.yaml.irsdkyaml.DriversDto;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Old {

    // public List<Driver> getDriverEntries() {
    //     IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
    //     List<Driver> driverList = new ArrayList<>();
    //
    //     if (irsdkYamlFileDto != null) {
    //         int maxCar = irsdkYamlFileDto.getDriverInfo().getDrivers().size();
    //         List<DriversDto> driverEntryList = irsdkYamlFileDto.getDriverInfo().getDrivers();
    //
    //         for (int idx = 0; idx < maxCar; idx++) {
    //
    //             // Check if car is present on track ( or in pit)
    //             if (isCarActive(idx)) {
    //
    //                 Driver Driver = new Driver();
    //                 Driver.setDriverName(driverEntryList.get(idx).getUserName());
    //
    //                 String carNumber = driverEntryList.get(idx).getCarNumber();
    //                 if (!carNumber.isEmpty()) {
    //                     Driver.setDriverNumber(Integer.parseInt(carNumber));
    //                 }
    //
    //                 Driver.setDriverposition(getVarInt("CarIdxPosition", idx));
    //                 driverList.add(Driver);
    //
    //             }
    //
    //         }
    //     }
    //
    //     return driverList;
    // }
    //
    // public List<Camera> getCameraEntries() {
    //     IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
    //     return irsdkYamlFileDto.getCameraInfo()
    //                            .getGroups()
    //                            .stream()
    //                            .map(groupsDto -> new Camera(Integer.parseInt(groupsDto.getGroupNum()), groupsDto.getGroupName()))
    //                            .collect(Collectors.toList());
    //
    // }
    //
    // public List<LapTiming> getLapTimingValuesSmall() {
    //     IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
    //     List<LapTiming> lapTimingList = new ArrayList<>();
    //
    //     if (irsdkYamlFileDto != null) {
    //         int maxCar = irsdkYamlFileDto.getDriverInfo().getDrivers().size();
    //         List<DriversDto> driverEntryList = irsdkYamlFileDto.getDriverInfo().getDrivers();
    //
    //         for (int idx = 0; idx < maxCar; idx++) {
    //             LapTiming entry = new LapTiming();
    //
    //             entry.setDriverPos(getVarInt("CarIdxPosition", idx));
    //             entry.setDriverNum(driverEntryList.get(idx).getCarNumber());
    //             entry.setDriverName(driverEntryList.get(idx).getUserName());
    //             entry.setDriverDelta(convertToLapTimingFormat(getVarFloat("CarIdxF2Time", idx)));
    //             entry.setDriverLastLap(convertToLapTimingFormat(getVarFloat("CarIdxLastLapTime", idx)));
    //             entry.setDriverBestLap(convertToLapTimingFormat(getVarFloat("CarIdxBestLapTime", idx)));
    //             entry.setDriverIRating(driverEntryList.get(idx).getIRating());
    //
    //             lapTimingList.add(entry);
    //         }
    //     }
    //
    //     sortLapTimingEntries(lapTimingList);
    //
    //     return lapTimingList;
    // }
    //

}
