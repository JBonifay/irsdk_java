package com.joffrey.iracingapp.model.defines;

public enum RpySrchMode {

    irsdk_RpySrch_ToStart(0),
    irsdk_RpySrch_ToEnd(1),
    irsdk_RpySrch_PrevSession(2),
    irsdk_RpySrch_NextSession(3),
    irsdk_RpySrch_PrevLap(4),
    irsdk_RpySrch_NextLap(5),
    irsdk_RpySrch_PrevFrame(6),
    irsdk_RpySrch_NextFrame(7),
    irsdk_RpySrch_PrevIncident(8),
    irsdk_RpySrch_NextIncident(9),
    irsdk_RpySrch_Last(10)            // unused placeholder
    ;

    private final int value;

    RpySrchMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
