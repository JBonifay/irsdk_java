package com.joffrey.iracingapp.model.iracing.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum VarTypeBytes {
    IRSDK_CHAR(1),        // irsdk_char
    IRSDK_BOOL(1),        // irsdk_bool
    IRSDK_INT(4),        // irsdk_int
    IRSDK_BIT_FIELD(4),        // irsdk_bitField
    IRSDK_FLOAT(4),        // irsdk_float
    IRSDK_DOUBLE(8)        // irsdk_double
    ;

    private static final Map<Integer, VarTypeBytes> varTypeBytesMap = new HashMap();
    private final        int                        value;

    VarTypeBytes(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static VarTypeBytes get(int code) {
        return (VarTypeBytes) varTypeBytesMap.get(code);
    }

    static {
        for (VarTypeBytes varTypeBytes : EnumSet.allOf(VarTypeBytes.class)) {
            varTypeBytesMap.put(varTypeBytes.value, varTypeBytes);
        }
    }

}
