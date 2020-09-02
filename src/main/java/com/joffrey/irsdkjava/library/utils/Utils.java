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

package com.joffrey.irsdkjava.library.utils;

import com.joffrey.irsdkjava.IRacingSDK;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Utils {

    private final IRacingSDK iRacingSDK;

    // private static String convertToLapTimingFormat(double seconds) {
    //     // If seconds == -1 || 0, return "-" for better UI
    //     if (seconds == -1 || seconds == 0) {
    //         return "-";
    //     }
    //     Date d = new Date((long) (seconds * 1000L));
    //     SimpleDateFormat df;
    //     if (seconds < 60) {
    //         df = new SimpleDateFormat("ss.SSS");
    //     } else {
    //         df = new SimpleDateFormat("mm:ss.SSS");
    //     }
    //     df.setTimeZone(TimeZone.getTimeZone("GMT"));
    //     return df.format(d).replaceAll(":", "'");
    // }
    //
    // /**
    //  * Sort timing entries, all entries with pos == 0 are add at the end of the list
    //  *
    //  * @param lapTimingList the initial List
    //  */
    // private void sortLapTimingEntries(List<LapTiming> lapTimingList) {
    //     List<LapTiming> entriesWithZero = new ArrayList<>();
    //
    //     // Remove all entries with pos == 0
    //     lapTimingList.forEach(LapTiming -> {
    //         int driverPos = LapTiming.getDriverPos();
    //         if (driverPos == 0) {
    //             // Add them in a new list
    //             entriesWithZero.add(LapTiming);
    //         }
    //     });
    //
    //     // Remove them from initial list
    //     lapTimingList.removeIf(l -> l.getDriverPos() == 0);
    //
    //     // Sort initial list
    //     lapTimingList.sort(Comparator.comparingInt(LapTiming::getDriverPos));
    //
    //     // Add entries with pos == 0 at the end of the list
    //     lapTimingList.addAll(entriesWithZero);
    // }
    //
    // private boolean isCarActive(int carIdx) {
    //     return TrkLoc.getValue(getVarInt("CarIdxTrackSurface", carIdx)).getValue() >= 1;
    // }


}
