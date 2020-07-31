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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.library.model.LapTimingDriverEntrySmallDto;
import com.joffrey.irsdkjava.library.yaml.IrsdkYamlFileDto;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriversDto;
import com.joffrey.irsdkjava.model.Header;
import com.joffrey.irsdkjava.model.VarHeader;
import com.joffrey.irsdkjava.model.defines.BroadcastMsg;
import com.joffrey.irsdkjava.model.defines.Constant;
import com.joffrey.irsdkjava.model.defines.StatusField;
import com.joffrey.irsdkjava.model.defines.VarType;
import com.joffrey.irsdkjava.model.defines.VarTypeBytes;
import com.joffrey.irsdkjava.service.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IRacingLibrary {

    private final WindowsService windowsService;

    private WinNT.HANDLE memMapFile     = null;
    private WinNT.HANDLE dataValidEvent = null;
    private Pointer      sharedMemory   = null;
    private Header       header         = null;

    private int     lastTickCount = Integer.MAX_VALUE;
    private boolean isInitialized = false;


    // ==================== Init ====================
    public boolean getNewData() {
        if (isInitialized || startup()) {

            // if sim is not active, then no new data
            if (header.getStatus() != StatusField.IRSDK_STCONNECTED.getValue()) {
                lastTickCount = Integer.MAX_VALUE;
                return false;
            }

            int latest = header.getLatestVarBuf();

            // if newer than last recieved, than report new data
            if (lastTickCount < header.getVarBuf(latest).getTickCount()) {

                // try twice to get the data out
                for (int count = 0; count < 2; count++) {

                    int curTickCount = header.getVarBuf(latest).getTickCount();

                    header = new Header(ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBuf(latest).getBufOffset(),
                                                                                  header.getBufLen())));

                    if (curTickCount == header.getVarBuf(latest).getTickCount()) {
                        lastTickCount = curTickCount;
                        return true;
                    }
                }
                // if here, the data changed out from under us
                return false;
            } else {
                lastTickCount = header.getVarBuf(latest).getTickCount();
                return true;
            }
        }
        return false;
    }

    public boolean isConnected() {
        if (isInitialized) {
            return (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0;
        }
        return false;
    }

    private boolean init() {
        if (!isInitialized) {
            startup();
            isInitialized = true;
        }
        return true;
    }

    private boolean startup() {
        // Try to open Memory Mapped File
        if (memMapFile == null) {
            memMapFile = windowsService.openMemoryMapFile(Constant.IRSDK_MEMMAPFILENAME);
            lastTickCount = Integer.MAX_VALUE;
        }

        if (memMapFile != null) {
            if (sharedMemory == null) {
                sharedMemory = windowsService.mapViewOfFile(memMapFile);
                header = new Header(ByteBuffer.wrap(sharedMemory.getByteArray(0, Header.HEADER_SIZE)));
                lastTickCount = Integer.MAX_VALUE;
            }

            if (sharedMemory != null) {
                if (dataValidEvent == null) {
                    dataValidEvent = windowsService.openEvent(Constant.IRSDK_DATAVALIDEVENTNAME);
                    lastTickCount = Integer.MAX_VALUE;
                }
            }

            if (dataValidEvent != null) {
                isInitialized = true;
                return isInitialized;
            }

        }

        isInitialized = false;
        return isInitialized;
    }

    // ==================== Get Values ====================
    public List<LapTimingDriverEntrySmallDto> getLapTimingValuesSmall() {
        IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
        List<LapTimingDriverEntrySmallDto> lapTimingList = new ArrayList<>();

        if (irsdkYamlFileDto != null) {
            int maxCar = irsdkYamlFileDto.getDriverInfo().getDrivers().size();
            List<DriversDto> driverEntryList = irsdkYamlFileDto.getDriverInfo().getDrivers();

            for (int idx = 0; idx < maxCar; idx++) {
                LapTimingDriverEntrySmallDto entry = new LapTimingDriverEntrySmallDto();

                entry.setDriverPos(getVarInt("CarIdxPosition", idx));
                entry.setDriverNum(driverEntryList.get(idx).getCarNumber());
                entry.setDriverName(driverEntryList.get(idx).getUserName());
                entry.setDriverDelta(getVarFloat("CarIdxF2Time", idx));
                entry.setDriverLastLap(getVarFloat("CarIdxLastLapTime", idx));
                entry.setDriverBestLap(getVarFloat("CarIdxBestLapTime", idx));
                entry.setDriverIRating(driverEntryList.get(idx).getIRating());

                lapTimingList.add(entry);
            }
        }
        return lapTimingList;
    }


    // ==================== Get Vars ====================
    private int varNameToIndex(String name) {
        if (!name.isEmpty()) {
            for (int index = 0; index < header.getNumVars(); index++) {
                VarHeader vh = getVarHeaderEntry(index);
                if (vh != null && vh.getName().equals(name)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private VarHeader getVarHeaderEntry(int index) {
        if (isInitialized) {
            if (index >= 0 && index < header.getNumVars()) {
                return new VarHeader(ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarHeaderOffset()
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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = com.joffrey.irsdkjava.model.defines.VarType.get(vh.getType());
                    ByteBuffer data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBufOffset(header.getLatestVarBuf()),
                                                                                header.getBufLen()));
                    data.order(ByteOrder.LITTLE_ENDIAN);
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (data.getChar(vh.getOffset() + (entry
                                                                                       * VarTypeBytes.IRSDK_BOOL.getValue())))
                                                       != 0;

                        case irsdk_int, irsdk_bitField -> (data.getInt(vh.getOffset() + (entry
                                                                                         * VarTypeBytes.IRSDK_BOOL.getValue())))
                                                          != 0;

                        case irsdk_float -> (data.getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                        case irsdk_double -> (data.getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = com.joffrey.irsdkjava.model.defines.VarType.get(vh.getType());
                    ByteBuffer data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBufOffset(header.getLatestVarBuf()),
                                                                                header.getBufLen()));
                    data.order(ByteOrder.LITTLE_ENDIAN);
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (int) data.getChar(vh.getOffset() + (entry
                                                                                            * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_int, irsdk_bitField -> (int) data.getInt(vh.getOffset() + (entry
                                                                                              * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_float -> (int) data.getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_double -> (int) data.getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_INT.getValue()));

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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = com.joffrey.irsdkjava.model.defines.VarType.get(vh.getType());
                    ByteBuffer data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBufOffset(header.getLatestVarBuf()),
                                                                                header.getBufLen()));
                    data.order(ByteOrder.LITTLE_ENDIAN);
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (float) data.getChar(vh.getOffset() + (entry
                                                                                              * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_int, irsdk_bitField -> (float) data.getInt(vh.getOffset() + (entry
                                                                                                * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_float -> (float) data.getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_double -> (float) data.getDouble(vh.getOffset() + (entry
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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = VarType.get(vh.getType());
                    ByteBuffer data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBufOffset(header.getLatestVarBuf()),
                                                                                header.getBufLen()));
                    data.order(ByteOrder.LITTLE_ENDIAN);
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (double) data.getChar(vh.getOffset() + (entry
                                                                                               * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                        case irsdk_int, irsdk_bitField -> (double) data.getInt(vh.getOffset() + (entry * VarTypeBytes.IRSDK_DOUBLE
                                .getValue()));

                        case irsdk_float -> (double) data.getFloat(vh.getOffset() + (entry
                                                                                     * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                        case irsdk_double -> (double) data.getDouble(vh.getOffset() + (entry
                                                                                       * VarTypeBytes.IRSDK_DOUBLE.getValue()));

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

    // ==================== Yaml ====================
    public String getSessionInfoStr() {
        if (isInitialized) {
            return new String(sharedMemory.getByteArray(header.getSessionInfoOffset(), header.getSessionInfoLen()));
        }
        return "";
    }

    private IrsdkYamlFileDto getIrsdkYamlFileBean() {
        if (isConnected()) {
            return createYamlObject(getSessionInfoStr());
        }
        return null;
    }

    private IrsdkYamlFileDto createYamlObject(String yamlString) {
        if (!yamlString.isEmpty()) {
            // Remove 'null' ascii char after '...' yaml ending
            yamlString = yamlString.substring(0, yamlString.indexOf("...") + 3);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            try {
                return objectMapper.readValue(yamlString, IrsdkYamlFileDto.class);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    // ==================== Message Broadcast ====================
    public int getBroadcastMsgID() {
        return windowsService.registerWindowMessage(Constant.IRSDK_BROADCASTMSGNAME);
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2, int var3) {
        broadcastMsg(msg, var1, MAKELONG(var2, var3));
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, float var2) {
        // multiply by 2^16-1 to move fractional part to the integer part
        int real = (int) (var2 * 65536.0f);

        broadcastMsg(msg, var1, real);
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2) {
        int msgId = getBroadcastMsgID();

        if (msgId != 0 && msg.getValue() >= 0 && msg.getValue() < BroadcastMsg.irsdk_BroadcastLast.getValue()) {
            windowsService.sendNotifyMessage(msgId, MAKELONG(msg.getValue(), var1), var2);
        }
    }

    /**
     * C++ MAKELONG for Java
     */
    private int MAKELONG(int lowWord, int highWord) {
        return ((highWord << 16) & 0xFFFF0000) | lowWord;
    }

}