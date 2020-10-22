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

import com.joffrey.iracing.irsdkjava.camera.CameraService;
import com.joffrey.iracing.irsdkjava.camera.model.CameraPacket;
import com.joffrey.iracing.irsdkjava.laptiming.LapTimingService;
import com.joffrey.iracing.irsdkjava.laptiming.model.LapTimingData;
import com.joffrey.iracing.irsdkjava.model.defines.BroadcastMsg;
import com.joffrey.iracing.irsdkjava.model.defines.Constant;
import com.joffrey.iracing.irsdkjava.raceinfo.RaceInfoService;
import com.joffrey.iracing.irsdkjava.raceinfo.model.RaceInfo;
import com.joffrey.iracing.irsdkjava.telemetry.TelemetryService;
import com.joffrey.iracing.irsdkjava.telemetry.model.TelemetryData;
import com.joffrey.iracing.irsdkjava.trackmaptracker.TrackmapTrackerService;
import com.joffrey.iracing.irsdkjava.trackmaptracker.model.TrackmapTrackerDriver;
import com.joffrey.iracing.irsdkjava.windows.WindowsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class IRacingLibrary {

    private final LapTimingService       lapTimingService;
    private final RaceInfoService        raceInfoService;
    private final TelemetryService       telemetryService;
    private final TrackmapTrackerService trackmapTrackerService;
    private final CameraService          cameraService;
    private final WindowsService         windowsService;

    // Flux
    public Flux<List<TrackmapTrackerDriver>> getTrackmapTrackerList() {
        return trackmapTrackerService.getTrackmapTrackerListFlux();
    }

    public Flux<List<LapTimingData>> getLapTimingDataList() {
        return lapTimingService.getLapTimingDataListFlux();
    }

    public Flux<RaceInfo> getRaceInfo() {
        return raceInfoService.getRaceInfoFlux();
    }

    public Flux<TelemetryData> getTelemetryData() {
        return telemetryService.getTelemetryDataFlux();
    }

    public Flux<CameraPacket> getCameraPacket() {
        return cameraService.getCameraPacketFlux();
    }

    // Broadcast
    public void broadcastMsg(BroadcastMsg msg, int var1, int var2, int var3) {
        broadcastMsg(msg, var1, windowsService.MAKELONG(var2, var3));
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, float var2) {
        // multiply by 2^16-1 to move fractional part to the integer part
        int real = (int) (var2 * 65536.0f);

        broadcastMsg(msg, var1, real);
    }

    private void broadcastMsg(BroadcastMsg msg, int var1, int var2) {
        int msgId = windowsService.registerWindowMessage(Constant.IRSDK_BROADCASTMSGNAME);

        if (msgId != 0 && msg.getValue() >= 0 && msg.getValue() < BroadcastMsg.irsdk_BroadcastLast.getValue()) {
            windowsService.sendNotifyMessage(msgId, windowsService.MAKELONG(msg.getValue(), var1), var2);
        }
    }

}
