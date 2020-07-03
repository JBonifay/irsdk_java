package com.joffrey.iracingapp.model.iracing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VarBuf {

    private int   tickCount;           // used to detect changes in data
    private int   bufOffset;           // offset from header
    private int[] pad = new int[2];    // (16 byte align)

    public VarBuf(int tickCount, int bufOffset) {
        this.tickCount = tickCount;
        this.bufOffset = bufOffset;
    }
}
