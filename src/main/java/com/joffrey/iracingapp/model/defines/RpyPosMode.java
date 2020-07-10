package com.joffrey.iracingapp.model.defines;

public enum RpyPosMode {

    irsdk_RpyPos_Begin(0),
    irsdk_RpyPos_Current(1),
    irsdk_RpyPos_End(2),
    irsdk_RpyPos_Last(3)                  // unused placeholder
    ;

    private final int value;

    RpyPosMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
