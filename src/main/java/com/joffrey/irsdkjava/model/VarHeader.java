package com.joffrey.irsdkjava.model;/*
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


import com.joffrey.irsdkjava.model.defines.Constant;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VarHeader {

    public static final int NUMBER_OF_FIELDS = 4;
    public static final int SIZEOF_FIELDS    = 4;
    public static final int VAR_HEADER_SIZE  =
            (NUMBER_OF_FIELDS * SIZEOF_FIELDS) + Constant.IRSDK_MAX_STRING + Constant.IRSDK_MAX_DESC + Constant.IRSDK_MAX_STRING;

    private int type;                                                 // irsdk_VarType
    private int offset;                                               // offset fron start of buffer row
    private int count;                                                // number of entrys (array)

    // so length in bytes would be irsdk_VarTypeBytes[type] * count
    private boolean countAsTime;
    private char[]  pad = new char[3];                                // (16 byte align)

    private String name;
    private String desc;
    private String unit;                                              // something like "kg/m^2"

    public VarHeader(ByteBuffer buffer) {
        this(buffer, 0);
    }

    public VarHeader(ByteBuffer buffer, int varOffset) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] localBuffer;
        type = buffer.getInt(varOffset + 0);
        offset = buffer.getInt(varOffset + 4);
        count = buffer.getInt(varOffset + 8);

        localBuffer = new byte[Constant.IRSDK_MAX_STRING];
        buffer.position(varOffset + 16);
        buffer.get(localBuffer, 0, localBuffer.length);
        name = new String(localBuffer).replaceAll("[\000]", "");

        buffer.position(varOffset + 16 + Constant.IRSDK_MAX_STRING + Constant.IRSDK_MAX_DESC);
        buffer.get(localBuffer, 0, localBuffer.length);
        unit = new String(localBuffer).replaceAll("[\000]", "");

        localBuffer = new byte[Constant.IRSDK_MAX_DESC];
        buffer.position(varOffset + 16 + Constant.IRSDK_MAX_STRING);
        buffer.get(localBuffer, 0, localBuffer.length);
        desc = new String(localBuffer).replaceAll("[\000]", "");
    }


}
