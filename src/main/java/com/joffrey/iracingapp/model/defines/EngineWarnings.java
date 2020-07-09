package com.joffrey.iracingapp.model.defines;

public enum EngineWarnings {
    irsdk_waterTempWarning((byte) 0x01),
    irsdk_fuelPressureWarning((byte) 0x02),
    irsdk_oilPressureWarning((byte) 0x04),
    irsdk_engineStalled((byte) 0x08),
    irsdk_pitSpeedLimiter((byte) 0x10),
    irsdk_revLimiterActive((byte) 0x20),
    ;

    private int value;

    EngineWarnings(byte value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
