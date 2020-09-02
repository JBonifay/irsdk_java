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

package com.joffrey.irsdkjava.sdk.model;

import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;

@Data
public class Header {

    public static final int HEADER_SIZE = 112; // All fields are int (4 bytes), there are 28 fields (28 * 4) = 112
    public final static int VARBUF_SIZE = 4 * 4;

    private Pointer       sharedMemory;
    private ByteBuffer    byteBuffer;
    private DiskSubHeader diskSubHeader;

    public Header(Pointer sharedMemory) {
        this.sharedMemory = sharedMemory;
    }

    public ByteBuffer getHeaderByteBuffer() {
        ByteBuffer headerByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(0, Header.HEADER_SIZE));
        headerByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return headerByteBuffer;
    }

    public ByteBuffer getSessionInfoByteBuffer() {
        ByteBuffer sessionInfoByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(getSessionInfoOffset(), getSessionInfoLen()));
        sessionInfoByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return sessionInfoByteBuffer;
    }

    public ByteBuffer getVarHeaderByteBuffer() {
        ByteBuffer varHeaderByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(getVarHeaderOffset(), getNumVars() * HEADER_SIZE));
        varHeaderByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return varHeaderByteBuffer;
    }

    public ByteBuffer getVarByteBuffer(int idx) {
        ByteBuffer varHeaderByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(getVarBuf_BufOffset(idx), getBufLen()));
        varHeaderByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return varHeaderByteBuffer;
    }

    public ByteBuffer getLatestVarByteBuffer() {
        ByteBuffer varHeaderByteBuffer = ByteBuffer.wrap(sharedMemory.getByteArray(getVarBuf_BufOffset(getLatestVarBuffIdx()), getBufLen()));
        varHeaderByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return varHeaderByteBuffer;
    }


    public int getVer() {
        return getHeaderByteBuffer().getInt(0);
    }

    public int getStatus() {
        return getHeaderByteBuffer().getInt(4);
    }

    public int getTickRate() {
        return getHeaderByteBuffer().getInt(8);
    }

    public int getSessionInfoUpdate() {
        return getHeaderByteBuffer().getInt(12);
    }

    public int getSessionInfoLen() {
        return getHeaderByteBuffer().getInt(16);
    }

    public int getSessionInfoOffset() {
        return getHeaderByteBuffer().getInt(20);
    }

    public int getNumVars() {
        return getHeaderByteBuffer().getInt(24);
    }

    public int getVarHeaderOffset() {
        return getHeaderByteBuffer().getInt(28);
    }

    public int getNumBuf() {
        return getHeaderByteBuffer().getInt(32);
    }

    public int getBufLen() {
        return getHeaderByteBuffer().getInt(36);
    }

    public int getVarBuf_TickCount(int varBuf) {
        return getSharedMemory().getInt((varBuf * VARBUF_SIZE) + 48);
    }

    public int getVarBuf_BufOffset(int varBuf) {
        return getSharedMemory().getInt((varBuf * VARBUF_SIZE) + 52);
    }

    private int getLatestVarBuffIdx() {
        int latest = 0;
        for (int i = 1; i < getNumBuf(); i++) {
            if (getVarBuf_TickCount(latest) < getVarBuf_TickCount(i)) {
                latest = i;
            }
        }
        return latest;
    }

}
