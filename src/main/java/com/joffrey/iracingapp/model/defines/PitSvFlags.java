package com.joffrey.iracingapp.model.defines;

public enum PitSvFlags {
    irsdk_LFTireChange(0x0001),
    irsdk_RFTireChange(0x0002),
    irsdk_LRTireChange(0x0004),
    irsdk_RRTireChange(0x0008),

    irsdk_FuelFill(0x0010),
    irsdk_WindshieldTearoff(0x0020),
    irsdk_FastRepair(0x0040);

    PitSvFlags(int value) {

    }
}
