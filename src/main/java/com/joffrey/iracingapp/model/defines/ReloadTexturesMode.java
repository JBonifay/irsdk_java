package com.joffrey.iracingapp.model.defines;

public enum ReloadTexturesMode {

    irsdk_ReloadTextures_All(0),              // reload all textuers
    irsdk_ReloadTextures_CarIdx(1)            // reload only textures for the specific carIdx
    ;


    private final int value;

    ReloadTexturesMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
