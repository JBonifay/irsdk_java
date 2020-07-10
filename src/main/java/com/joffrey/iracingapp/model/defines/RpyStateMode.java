package com.joffrey.iracingapp.model.defines;

public enum RpyStateMode {

    irsdk_RpyState_EraseTape(0),               // clear any data in the replay tape
    irsdk_RpyState_Last(1)               // unused place holder
    ;

    private final int value;

    RpyStateMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
