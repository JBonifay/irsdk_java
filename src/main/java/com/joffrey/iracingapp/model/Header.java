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

package com.joffrey.iracingapp.model;

import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {

    public static final int HEADER_SIZE = 112; // All fields are int (4 bytes), there are 28 fields (28 * 4) = 112

    private Pointer sharedMemory;

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

    public Header(Pointer sharedMemory) {
        this.sharedMemory = sharedMemory;
    }

    private ByteBuffer getSharedMemoryByteBuffer() {
        ByteBuffer headerByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(0, Header.HEADER_SIZE));
        headerByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return headerByteBuffer;
    }

    public int getVer() {
        return getSharedMemoryByteBuffer().getInt(0);
    }

    public int getStatus() {
        return getSharedMemoryByteBuffer().getInt(4);
    }

    public int getTickRate() {
        return getSharedMemoryByteBuffer().getInt(8);
    }

    public int getSessionInfoUpdate() {
        return getSharedMemoryByteBuffer().getInt(12);
    }

    public int getSessionInfoLen() {
        return getSharedMemoryByteBuffer().getInt(16);
    }

    public int getSessionInfoOffset() {
        return getSharedMemoryByteBuffer().getInt(20);
    }

    public int getNumVars() {
        return getSharedMemoryByteBuffer().getInt(24);
    }

    public int getHeaderOffset() {
        return getSharedMemoryByteBuffer().getInt(28);
    }

    public int getNumBuf() {
        return getSharedMemoryByteBuffer().getInt(32);
    }

    public int getBufLen() {
        return getSharedMemoryByteBuffer().getInt(36);
    }

    public int getPadValue(int padIdx) {
        if (padIdx == 0) {
            return getSharedMemoryByteBuffer().getInt(40);
        } else if (padIdx == 1) {
            return getSharedMemoryByteBuffer().getInt(44);
        }
        return -1;
    }

    public VarBuf getVarBuf(int varBufIdx) {
        if (varBufIdx == 0) {
            return new VarBuf(getSharedMemoryByteBuffer().getInt(48), getSharedMemoryByteBuffer().getInt(52));
        } else if (varBufIdx == 1) {
            return new VarBuf(getSharedMemoryByteBuffer().getInt(64), getSharedMemoryByteBuffer().getInt(68));
        } else if (varBufIdx == 2) {
            return new VarBuf(getSharedMemoryByteBuffer().getInt(80), getSharedMemoryByteBuffer().getInt(84));
        } else if (varBufIdx == 3) {
            return new VarBuf(getSharedMemoryByteBuffer().getInt(96), getSharedMemoryByteBuffer().getInt(100));
        }
        return null;
    }

}
