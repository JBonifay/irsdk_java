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
import com.joffrey.irsdkjava.library.model.laptiming.LapTimingDriverEntrySmallDto;
import com.joffrey.irsdkjava.library.model.tveditor.TvCamera;
import com.joffrey.irsdkjava.library.model.tveditor.TvDriverEntry.TvDriverEntry;
import com.joffrey.irsdkjava.library.yaml.IrsdkYamlFileDto;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriversDto;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.ResultsPositionsDto;
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
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
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
    // private ByteBuffer   header.getLatestVarByteBuffer();

    private int     lastTickCount = Integer.MAX_VALUE;
    private boolean isInitialized = false;


    // ==================== Init ====================
    private boolean getNewData() {
        if (isInitialized || startup()) {

            // if sim is not active, then no new data
            // TODO: 31 Jul 2020 Revert header parameter to sharedMemory to avoid creating each time Header object, sharedMemory will keep data updated all time
            if (header.getStatus() != StatusField.IRSDK_STCONNECTED.getValue()) {
                lastTickCount = Integer.MAX_VALUE;
                return false;
            }

            int latest = 0;
            for (int i = 1; i < header.getNumBuf(); i++) {
                if (header.getVarBuf_TickCount(latest) < header.getVarBuf_TickCount(i)) {
                    latest = i;
                }
            }

            int curTickCount = header.getVarBuf_TickCount(latest);

            // header.getLatestVarByteBuffer() = header.getVarByteBuffer(latest);
            // header.getLatestVarByteBuffer().order(ByteOrder.LITTLE_ENDIAN);

            if (curTickCount == header.getVarBuf_TickCount(latest)) {
                lastTickCount = curTickCount;
                return true;
            }

            // if here, the data changed out from under us
            return false;
        }
        return false;
    }

    public boolean isConnected() {
        if (isInitialized || init()) {
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
                header = new Header(sharedMemory);
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
        return false;
    }

    // ==================== Get Values ====================
    public List<TvDriverEntry> getDriverEntries() {
        IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
        List<TvDriverEntry> tvDriverEntryList = new ArrayList<>();

        if (irsdkYamlFileDto != null) {
            int maxCar = irsdkYamlFileDto.getDriverInfo().getDrivers().size();
            List<DriversDto> driverEntryList = irsdkYamlFileDto.getDriverInfo().getDrivers();
            List<ResultsPositionsDto> resultsPositionsList = irsdkYamlFileDto.getSessionInfo()
                                                                             .getSessions()
                                                                             .get(0)
                                                                             .getResultsPositions();

            for (int idx = 0; idx < maxCar; idx++) {
                TvDriverEntry tvDriverEntry = new TvDriverEntry();
                tvDriverEntry.setDriverName(driverEntryList.get(idx).getUserName());
                tvDriverEntry.setDriverNumber(Integer.parseInt(driverEntryList.get(idx).getCarNumber()));
                tvDriverEntry.setDriverposition(getVarInt("CarIdxPosition", idx));

                tvDriverEntryList.add(tvDriverEntry);
            }
        }

        return tvDriverEntryList;
    }

    public List<TvCamera> getCameraEntries() {
        IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
        return irsdkYamlFileDto.getCameraInfo()
                               .getGroups()
                               .stream()
                               .map(groupsDto -> new TvCamera(Integer.parseInt(groupsDto.getGroupNum()),
                                                              groupsDto.getGroupName()))
                               .collect(Collectors.toList());

    }

    public List<LapTimingDriverEntrySmallDto> getLapTimingValuesSmall() {
        IrsdkYamlFileDto irsdkYamlFileDto = getIrsdkYamlFileBean();
        List<LapTimingDriverEntrySmallDto> lapTimingList = new ArrayList<>();

        if (irsdkYamlFileDto != null) {
            int maxCar = irsdkYamlFileDto.getDriverInfo().getDrivers().size();
            List<DriversDto> driverEntryList = irsdkYamlFileDto.getDriverInfo().getDrivers();
            List<ResultsPositionsDto> resultsPositionsList = irsdkYamlFileDto.getSessionInfo()
                                                                             .getSessions()
                                                                             .get(0)
                                                                             .getResultsPositions();

            for (int idx = 0; idx < maxCar; idx++) {
                LapTimingDriverEntrySmallDto entry = new LapTimingDriverEntrySmallDto();

                int pos = getVarInt("CarIdxPosition", idx);
                if (pos != 0) {
                    entry.setDriverPos(pos);
                    entry.setDriverNum(driverEntryList.get(idx).getCarNumber());
                    entry.setDriverName(driverEntryList.get(idx).getUserName());
                    entry.setDriverDelta(convertToLapTimingFormat(getVarFloat("CarIdxF2Time", idx)));
                    entry.setDriverLastLap(convertToLapTimingFormat(getVarFloat("CarIdxLastLapTime", idx)));
                    entry.setDriverBestLap(convertToLapTimingFormat(getVarFloat("CarIdxBestLapTime", idx)));
                    entry.setDriverIRating(driverEntryList.get(idx).getIRating());

                    lapTimingList.add(entry);
                }
            }
        }

        lapTimingList.sort(Comparator.comparingInt(LapTimingDriverEntrySmallDto::getDriverPos));

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
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (header.getLatestVarByteBuffer()
                                                              .getChar(vh.getOffset() + (entry
                                                                                         * VarTypeBytes.IRSDK_BOOL.getValue())))
                                                       != 0;

                        case irsdk_int, irsdk_bitField -> (header.getLatestVarByteBuffer()
                                                                 .getInt(vh.getOffset() + (entry
                                                                                           * VarTypeBytes.IRSDK_BOOL.getValue())))
                                                          != 0;

                        case irsdk_float -> (header.getLatestVarByteBuffer()
                                                   .getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                        case irsdk_double -> (header.getLatestVarByteBuffer()
                                                    .getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL.getValue())))
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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = com.joffrey.irsdkjava.model.defines.VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (int) header.getLatestVarByteBuffer()
                                                                   .getChar(vh.getOffset() + (entry
                                                                                              * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_int, irsdk_bitField -> (int) header.getLatestVarByteBuffer()
                                                                      .getInt(vh.getOffset() + (entry
                                                                                                * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_float -> (int) header.getLatestVarByteBuffer()
                                                        .getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_INT.getValue()));

                        case irsdk_double -> (int) header.getLatestVarByteBuffer()
                                                         .getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_INT.getValue()));

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
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (float) header.getLatestVarByteBuffer()
                                                                     .getChar(vh.getOffset() + (entry
                                                                                                * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_int, irsdk_bitField -> (float) header.getLatestVarByteBuffer()
                                                                        .getInt(vh.getOffset() + (entry * VarTypeBytes.IRSDK_FLOAT
                                                                                .getValue()));

                        case irsdk_float -> (float) header.getLatestVarByteBuffer()
                                                          .getFloat(vh.getOffset() + (entry
                                                                                      * VarTypeBytes.IRSDK_FLOAT.getValue()));

                        case irsdk_double -> (float) header.getLatestVarByteBuffer()
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
        if (isConnected()) {
            VarHeader vh = getVarHeaderEntry(idx);
            if (vh != null) {
                if (entry >= 0 && entry < vh.getCount()) {
                    com.joffrey.irsdkjava.model.defines.VarType vhType = VarType.get(vh.getType());
                    return switch (vhType) {
                        case irsdk_char, irsdk_bool -> (double) header.getLatestVarByteBuffer()
                                                                      .getChar(vh.getOffset() + (entry * VarTypeBytes.IRSDK_DOUBLE
                                                                              .getValue()));

                        case irsdk_int, irsdk_bitField -> (double) header.getLatestVarByteBuffer()
                                                                         .getInt(vh.getOffset() + (entry
                                                                                                   * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                        case irsdk_float -> (double) header.getLatestVarByteBuffer()
                                                           .getFloat(vh.getOffset() + (entry
                                                                                       * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                        case irsdk_double -> (double) header.getLatestVarByteBuffer()
                                                            .getDouble(vh.getOffset() + (entry
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
            return new String(header.getSessionInfoByteBuffer().array());
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

    // ==================== Utils ====================
    private String convertToLapTimingFormat(double seconds) {
        // If seconds == -1 || 0, return "-" for better UI
        if (seconds == -1 || seconds == 0) {
            return "-";
        }
        Date d = new Date((long) (seconds * 1000L));
        SimpleDateFormat df;
        if (seconds < 60) {
            df = new SimpleDateFormat("ss.SSS");
        } else {
            df = new SimpleDateFormat("mm:ss.SSS");
        }
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d).replaceAll(":", "'");
    }
}
