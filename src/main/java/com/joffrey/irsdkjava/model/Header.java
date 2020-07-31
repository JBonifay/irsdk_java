/*
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.joffrey.irsdkjava.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;

@Data
public class Header {

    public static final int HEADER_SIZE = 112; // All fields are int (4 bytes), there are 28 fields (28 * 4) = 112
    public final static int VARBUF_SIZE = 4 * 4;

    private ByteBuffer    byteBuffer;
    private DiskSubHeader diskSubHeader;

    private int ver;                                                                      // this api header version, see IRSDK_VER
    private int status;                                                                   // bitfield using irsdk_StatusField
    private int tickRate;                                                                 // ticks per second (60 or 360 etc)

    // session information, updated periodicaly
    private int sessionInfoUpdate;                                                        // Incremented when session info changes
    private int sessionInfoLen;                                                           // Length in bytes of session info string
    private int sessionInfoOffset;                                                        // Session info, encoded in YAML format

    // State data, output at tickRate
    private int numVars;                                                                  // length of arra pointed to by varHeaderOffset
    private int varHeaderOffset;                                                          // offset to irsdk_varHeader[numVars] array, Describes the variables received in varBuf

    private       int      numBuf;                                                              // <= IRSDK_MAX_BUFS (3 for now)
    private       int      bufLen;                                                              // length in bytes for one line
    private final int[]    pad1   = new int[2];                                                 // (16 byte align)
    private final VarBuf[] varBuf = new VarBuf[]{new VarBuf(), new VarBuf(), new VarBuf(), new VarBuf()};
    // buffers of data being written to

    public Header(ByteBuffer buffer) {
        //make a copy of the buffer
        byteBuffer = ByteBuffer.allocate(HEADER_SIZE);
        byteBuffer.position(0);
        buffer.position(0);
        byteBuffer.put(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        
        ver =  byteBuffer.getInt(0);
        status =  byteBuffer.getInt(4);
        tickRate = byteBuffer.getInt(8);
        sessionInfoUpdate = byteBuffer.getInt(12);
        sessionInfoLen = byteBuffer.getInt(16);
        sessionInfoOffset = byteBuffer.getInt(20);
        numVars = byteBuffer.getInt(24);
        varHeaderOffset = byteBuffer.getInt(28);
        numBuf = byteBuffer.getInt(32);
        bufLen = byteBuffer.getInt(36);

    }
    
    public int getPadValue(int padIdx) {
        if (padIdx == 0) {
            return byteBuffer.getInt(40);
        } else if (padIdx == 1) {
            return byteBuffer.getInt(44);
        }
        return -1;
    }

    public VarBuf getVarBuf(int varBufIdx) {
        if (varBufIdx == 0) {
            return new VarBuf(byteBuffer.getInt(48), byteBuffer.getInt(52));
        } else if (varBufIdx == 1) {
            return new VarBuf(byteBuffer.getInt(64), byteBuffer.getInt(68));
        } else if (varBufIdx == 2) {
            return new VarBuf(byteBuffer.getInt(80), byteBuffer.getInt(84));
        } else if (varBufIdx == 3) {
            return new VarBuf(byteBuffer.getInt(96), byteBuffer.getInt(100));
        }
        return null;
    }

    public int getLatestVarBuf() {
        int latest = 0;
        for (int i = 1; i < getNumBuf(); i++) {
            if (getVarBuf(latest).getTickCount() < getVarBuf(i).getTickCount()) {
                latest = i;
            }
        }
        return latest;
    }

    public int getVarBufTickCount(int varBuf) {
        return byteBuffer.getInt((varBuf * VARBUF_SIZE) + 48);
    }

    public int getVarBufOffset(int varBuf) {
        return byteBuffer.getInt((varBuf * VARBUF_SIZE) + 52);
    }

}
