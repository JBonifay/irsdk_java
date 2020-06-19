package com.joffrey.iracingapp.model.iracing;

import lombok.Data;

@Data
public class VarBuf {

    private int   tickCount;           // used to detect changes in data
    private int   bufOffset;           // offset from header
    private int[] pad = new int[2];    // (16 byte align)

}
