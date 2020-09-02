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

package com.joffrey.irsdkjava.sdk.defines;

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
