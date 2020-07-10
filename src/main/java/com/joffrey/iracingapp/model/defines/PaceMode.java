package com.joffrey.iracingapp.model.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PaceMode {
    irsdk_PaceModeSingleFileStart(0),
    irsdk_PaceModeDoubleFileStart(1),
    irsdk_PaceModeSingleFileRestart(2),
    irsdk_PaceModeDoubleFileRestart(3),
    irsdk_PaceModeNotPacing(4),
    ;

    private static final Map<Integer, PaceMode> paceModeMap = new HashMap();
    private final        int                        value;

    PaceMode(int value) {
        this.value = value;
    }


    public static PaceMode get(int code) {
        return (PaceMode) paceModeMap.get(code);
    }

    static {
        for (PaceMode paceMode : EnumSet.allOf(PaceMode.class)) {
            paceModeMap.put(paceMode.value, paceMode);
        }
    }
}
