package com.joffrey.iracingapp.model.defines;

public enum Flags {
    // global flags
    irsdk_checkered(0x00000001),
    irsdk_white(0x00000002),
    irsdk_green(0x00000004),
    irsdk_yellow(0x00000008),
    irsdk_red(0x00000010),
    irsdk_blue(0x00000020),
    irsdk_debris(0x00000040),
    irsdk_crossed(0x00000080),
    irsdk_yellowWaving(0x00000100),
    irsdk_oneLapToGreen(0x00000200),
    irsdk_greenHeld(0x00000400),
    irsdk_tenToGo(0x00000800),
    irsdk_fiveToGo(0x00001000),
    irsdk_randomWaving(0x00002000),
    irsdk_caution(0x00004000),
    irsdk_cautionWaving(0x00008000),

    // drivers black flags
    irsdk_black(0x00010000),
    irsdk_disqualify(0x00020000),
    irsdk_servicible(0x00040000), // car is allowed service (not a flag)
    irsdk_furled(0x00080000),
    irsdk_repair(0x00100000),

    // start lights
    irsdk_startHidden(0x10000000),
    irsdk_startReady(0x20000000),
    irsdk_startSet(0x40000000),
    irsdk_startGo(0x80000000),
    ;

    private int value;

    Flags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
