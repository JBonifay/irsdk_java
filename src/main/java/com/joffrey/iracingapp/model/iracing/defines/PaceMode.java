package com.joffrey.iracingapp.model.iracing.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PaceMode {
    irsdk_PaceModeSingleFileStart(1),
    irsdk_PaceModeDoubleFileStart(2),
    irsdk_PaceModeSingleFileRestart(3),
    irsdk_PaceModeDoubleFileRestart(4),
    irsdk_PaceModeNotPacing(5),
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
