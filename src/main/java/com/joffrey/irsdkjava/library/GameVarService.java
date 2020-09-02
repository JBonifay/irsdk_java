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

package com.joffrey.irsdkjava.library;

import com.joffrey.irsdkjava.IRacingSDK;
import com.joffrey.irsdkjava.sdk.VarHeader;
import com.joffrey.irsdkjava.sdk.defines.VarType;
import com.joffrey.irsdkjava.sdk.defines.VarTypeBytes;
import java.nio.ByteBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameVarService {

    private final IRacingSDK iRacingSDK;

    private int varNameToIndex(String name) {
        if (!name.isEmpty()) {
            for (int index = 0; index < iRacingSDK.getHeader().getNumVars(); index++) {
                VarHeader vh = getVarHeaderEntry(index);
                if (vh != null && vh.getName().equals(name)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private VarHeader getVarHeaderEntry(int index) {
        if (iRacingSDK.isConnected()) {
            if (index >= 0 && index < iRacingSDK.getHeader().getNumVars()) {
                return new VarHeader(ByteBuffer.wrap(iRacingSDK.getSharedMemory()
                                                               .getByteArray(iRacingSDK.getHeader().getVarHeaderOffset()
                                                                             + (VarHeader.VAR_HEADER_SIZE * index),
                                                                             VarHeader.VAR_HEADER_SIZE)));
            }
        }
        return null;
    }

    public boolean getVarBoolean(String varName) {
        return getVarBoolean(varNameToIndex(varName), 0);
    }

    public boolean getVarBoolean(String varName, int entry) {
        return getVarBoolean(varNameToIndex(varName), entry);
    }

    private boolean getVarBoolean(int idx, int entry) {
        if (iRacingSDK.isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    VarType vhType = VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (iRacingSDK.getHeader()
                                                                  .getLatestVarByteBuffer()
                                                                  .getChar(vh.getOffset() + (entry
                                                                                                 * VarTypeBytes.IRSDK_BOOL.getValue())))
                                                       != 0;

                        case irsdk_int, irsdk_bitField -> (iRacingSDK.getHeader()
                                                                     .getLatestVarByteBuffer()
                                                                     .getInt(vh.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL
                                                                                 .getValue()))) != 0;

                        case irsdk_float -> (iRacingSDK.getHeader()
                                                       .getLatestVarByteBuffer()
                                                       .getFloat(vh.getOffset() + (entry
                                                                                       * VarTypeBytes.IRSDK_BOOL.getValue())))
                                            != 0;

                        case irsdk_double -> (iRacingSDK.getHeader()
                                                        .getLatestVarByteBuffer()
                                                        .getDouble(vh.getOffset() + (entry
                                                                                         * VarTypeBytes.IRSDK_BOOL.getValue())))
                                             != 0;

                        default -> throw new IllegalStateException("Unexpected value: " + vhType);
                    };
                }
            }
        }
        return false;
    }

    public int getVarInt(String varName) {
        return getVarInt(varNameToIndex(varName), 0);
    }

    public int getVarInt(String varName, int entry) {
        return getVarInt(varNameToIndex(varName), entry);
    }

    private int getVarInt(int idx, int entry) {
        if (iRacingSDK.isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    VarType vhType = VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (int) iRacingSDK.getHeader()
                                                                       .getLatestVarByteBuffer()
                                                                       .getChar(vh.getOffset() + (entry
                                                                                                      * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_int, irsdk_bitField -> (int) iRacingSDK.getHeader()
                                                                          .getLatestVarByteBuffer()
                                                                          .getInt(vh.getOffset() + (entry
                                                                                                        * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_float -> (int) iRacingSDK.getHeader()
                                                            .getLatestVarByteBuffer()
                                                            .getFloat(vh.getOffset() + (entry
                                                                                            * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_double -> (int) iRacingSDK.getHeader()
                                                             .getLatestVarByteBuffer()
                                                             .getDouble(vh.getOffset() + (entry
                                                                                              * VarTypeBytes.IRSDK_INT.getValue()));

                        default -> throw new IllegalStateException("Unexpected value: " + vhType);
                    };
                }
            }
        }
        return 0;
    }

    public float getVarFloat(String varName) {
        return getVarFloat(varNameToIndex(varName), 0);
    }

    public float getVarFloat(String varName, int entry) {
        return getVarFloat(varNameToIndex(varName), entry);
    }

    private float getVarFloat(int idx, int entry) {
        if (iRacingSDK.isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    VarType vhType = VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (float) iRacingSDK.getHeader()
                                                                         .getLatestVarByteBuffer()
                                                                         .getChar(vh.getOffset() + (entry
                                                                                                        * VarTypeBytes.IRSDK_FLOAT
                                                                                                                .getValue()));

                        case irsdk_int, irsdk_bitField -> (float) iRacingSDK.getHeader()
                                                                            .getLatestVarByteBuffer()
                                                                            .getInt(vh.getOffset() + (entry
                                                                                                          * VarTypeBytes.IRSDK_FLOAT
                                                                                                                  .getValue()));

                        case irsdk_float -> (float) iRacingSDK.getHeader()
                                                              .getLatestVarByteBuffer()
                                                              .getFloat(vh.getOffset() + (entry
                                                                                              * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_double -> (float) iRacingSDK.getHeader()
                                                               .getLatestVarByteBuffer()
                                                               .getDouble(vh.getOffset() + (entry
                                                                                                * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        default -> throw new IllegalStateException("Unexpected value: " + vhType);
                    };
                } else {
                    // invalid offset
                }
            } else {
                //invalid variable index
            }
        }
        return 0.0F;

    }

    public double getVarDouble(String varName) {
        return getVarDouble(varNameToIndex(varName), 0);
    }

    public double getVarDouble(String varName, int entry) {
        return getVarDouble(varNameToIndex(varName), entry);
    }

    private double getVarDouble(int idx, int entry) {
        if (iRacingSDK.isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    VarType vhType = VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (double) iRacingSDK.getHeader()
                                                                          .getLatestVarByteBuffer()
                                                                          .getChar(vh.getOffset() + (entry
                                                                                                         * VarTypeBytes.IRSDK_DOUBLE
                                                                                                                 .getValue()));

                        case irsdk_int, irsdk_bitField -> (double) iRacingSDK.getHeader()
                                                                             .getLatestVarByteBuffer()
                                                                             .getInt(vh.getOffset() + (entry
                                                                                                           * VarTypeBytes.IRSDK_DOUBLE
                                                                                                                   .getValue()));

                        case irsdk_float -> (double) iRacingSDK.getHeader()
                                                               .getLatestVarByteBuffer()
                                                               .getFloat(vh.getOffset() + (entry
                                                                                               * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                        case irsdk_double -> (double) iRacingSDK.getHeader()
                                                                .getLatestVarByteBuffer()
                                                                .getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_DOUBLE
                                                                            .getValue()));

                        default -> throw new IllegalStateException("Unexpected value: " + vhType);
                    };
                } else {
                    // invalid offset
                }
            } else {
                //invalid variable index
            }
        }
        return 0.0;

    }


}
