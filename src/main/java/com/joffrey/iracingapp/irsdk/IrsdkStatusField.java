package com.joffrey.iracingapp.irsdk;

public enum  IrsdkStatusField {

    IRSDK_STCONNECTED(1),;

    private final int value;

    IrsdkStatusField(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
