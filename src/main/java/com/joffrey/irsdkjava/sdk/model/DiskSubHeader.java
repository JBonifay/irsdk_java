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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;

@Data
public class DiskSubHeader {

    public final static int SUB_HEADER_SIZE = 8 + 8 + 8 + 4 + 4;

    private ByteBuffer byteBuffer;

    private long   sessionStartDate;
    private double sessionStartTime;
    private double sessionEndTime;
    private int    sessionsLapCount;
    private int    sessionRecordCount;


    public DiskSubHeader(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.byteBuffer = ByteBuffer.allocate(SUB_HEADER_SIZE);
        this.byteBuffer.position(0);
        byteBuffer.position(0);
        this.byteBuffer.put(byteBuffer);
        this.byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        sessionStartDate = byteBuffer.getLong(0);
        sessionStartTime = byteBuffer.getDouble(8);
        sessionEndTime = byteBuffer.getLong(16);
        sessionsLapCount = byteBuffer.getInt(24);
        sessionRecordCount = byteBuffer.getInt(28);
    }
}
