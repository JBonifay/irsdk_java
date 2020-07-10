package com.joffrey.iracingapp.model.defines;

public enum TelemCommandMode {

    irsdk_TelemCommand_Stop(0),               // Turn telemetry recording off
    irsdk_TelemCommand_Start(1),              // Turn telemetry recording on
    irsdk_TelemCommand_Restart(2),
    ;            // Write current file to disk and start a new one

    private int value;

    TelemCommandMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
