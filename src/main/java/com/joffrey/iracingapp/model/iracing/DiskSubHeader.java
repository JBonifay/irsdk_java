package com.joffrey.iracingapp.model.iracing;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class DiskSubHeader {

    private Timestamp sessionStartDate;
    private double    sessionStartTime;
    private double    sessionEndTime;
    private int       sessionsLapCount;
    private int       sessionRecordCount;

}
