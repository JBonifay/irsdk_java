package com.joffrey.iracingapp.irsdk;

import lombok.Data;

@Data
public class IrsdkVarBuf {

    private int   tickCount;           // used to detect changes in data
    private int   bufOffset;           // offset from header
    private int[] pad = new int[2];    // (16 byte align)

}
