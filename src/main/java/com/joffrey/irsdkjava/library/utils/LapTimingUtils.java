package com.joffrey.irsdkjava.library.utils;

import com.joffrey.irsdkjava.library.laptiming.model.LapTimingData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LapTimingUtils {

    /**
     * Sort timing entries, all  entries with pos ==0 are add at  the end of the list
     *
     * @param lapTimingDataList the initial List
     * @return Sorted list with pos ==0 at the end
     */
    public static List<LapTimingData> sortLapTimingEntries(List<LapTimingData> lapTimingDataList) {
        List<LapTimingData> entriesWithZero = new ArrayList<>();

        // Filter element with pos == 0
        lapTimingDataList.forEach(lapTimingData -> {
            int driverPos = lapTimingData.getCarIdxPosition();
            if (driverPos == 0) {
                // Add them in a new list
                entriesWithZero.add(lapTimingData);
            }
        });

        lapTimingDataList.removeIf(l -> l.getCarIdxPosition() == 0);
        lapTimingDataList.sort(Comparator.comparingInt(LapTimingData::getCarIdxPosition));
        lapTimingDataList.addAll(entriesWithZero);

        return lapTimingDataList;
    }

}
