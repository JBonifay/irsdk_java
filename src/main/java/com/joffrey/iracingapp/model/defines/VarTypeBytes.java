package com.joffrey.iracingapp.model.defines;

public enum VarTypeBytes {
    irsdk_char(1),        // irsdk_char
    irsdk_bool(1),        // irsdk_bool
    irsdk_int(4),        // irsdk_int
    irsdk_bitField(4),        // irsdk_bitField
    irsdk_float(4),        // irsdk_float
    irsdk_double(8)        // irsdk_double
    ;

    private int value;

    VarTypeBytes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
