package com.joffrey.iracingapp.model.defines;

public enum PaceFlags {
    irsdk_PaceFlagsEndOfLine(0x01),
    irsdk_PaceFlagsFreePass(0x02),
    irsdk_PaceFlagsWavedAround(0x04),
    ;

    PaceFlags(int value) {

    }
}
