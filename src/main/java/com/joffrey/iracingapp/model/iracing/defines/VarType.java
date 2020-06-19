package com.joffrey.iracingapp.model.iracing.defines;

public enum VarType {
    irsdk_char, irsdk_bool, // 1 byte
    irsdk_int, irsdk_bitField, irsdk_float, // 4 bytes
    irsdk_double, // 8 bytes
    irsdk_ETCount //index, don't use
}
