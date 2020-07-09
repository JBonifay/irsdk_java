package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.model.Header;
import com.joffrey.iracingapp.model.VarHeader;
import com.joffrey.iracingapp.model.defines.BroadcastMsg;
import com.joffrey.iracingapp.model.defines.Constant;
import com.joffrey.iracingapp.model.defines.StatusField;
import com.joffrey.iracingapp.service.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Utils {

    private final WindowsService windowsService;

    private WinNT.HANDLE memMapFile     = null;
    private WinNT.HANDLE dataValidEvent = null;
    private Pointer      sharedMemory   = null;
    private Header       header;

    @Getter
    private ByteBuffer dataBuffer;

    private int     lastTickCount = Integer.MAX_VALUE;
    private boolean isInitialized = false;

    private final double    timeout       = 30.0;                // timeout after 30 seconds with no communication
    private       Timestamp lastValidTime = new Timestamp(System.currentTimeMillis());

    public boolean startup() {

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
        return isInitialized;
    }

    public void shutdown() throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public boolean getNewData(ByteBuffer data) {
        if (isInitialized || startup()) {

            // if sim is not active, then no new data
            if (header.getStatus() != StatusField.IRSDK_STCONNECTED.getValue()) {
                lastTickCount = Integer.MAX_VALUE;
                return false;
            }

            int latest = 0;
            for (int i = 1; i < header.getNumBuf(); i++) {
                if (header.getVarBuf(latest).getTickCount() < header.getVarBuf(i).getTickCount()) {
                    latest = i;
                }
            }

            // if newer than last recieved, than report new data
            if (lastTickCount < header.getVarBuf(latest).getTickCount()) {

                // if asked to retrieve the data
                if (data != null) {

                    // try twice to get the data out
                    for (int count = 0; count < 2; count++) {

                        int curTickCount = header.getVarBuf(latest).getTickCount();

                        // memcpy(data, pSharedMem + pHeader -> varBuf[latest].bufOffset, pHeader -> bufLen);
                        data = ByteBuffer.wrap(
                                sharedMemory.getByteArray(header.getVarBuf(latest).getBufOffset(), header.getBufLen()));

                        dataBuffer = data;

                        if (curTickCount == header.getVarBuf(latest).getTickCount()) {
                            lastTickCount = curTickCount;
                            lastValidTime = new Timestamp(System.currentTimeMillis());
                            return true;
                        }
                    }

                    // if here, the data changed out from under us
                    return false;

                } else {
                    lastTickCount = header.getVarBuf(latest).getTickCount();
                    lastValidTime = new Timestamp(System.currentTimeMillis());
                    return true;
                }

                // if older than last recieved, than reset, we probably disconnected
            } else if (lastTickCount > header.getVarBuf(latest).getTickCount()) {
                lastTickCount = header.getVarBuf(latest).getTickCount();
                return false;
            }
            // else the same, and nothing changed this tick
        }

        return false;
    }

    private Map<Integer, VarHeader> getVarheaderList(int numberOfVar, ByteBuffer buffer) {
        Map<Integer, VarHeader> varHeaderMap = new TreeMap<>();

        for (int i = 0; i < numberOfVar; i++) {
            int varOffset = i * VarHeader.SIZEOF_VAR_HEADER;
            VarHeader varHeader = new VarHeader(buffer, varOffset);
            //now put it in the cache
            varHeaderMap.put(varHeader.getOffset(), varHeader);
        }
        return varHeaderMap;
    }

    public boolean waitForDataReady(int timeOut, ByteBuffer data) throws InterruptedException {
        if (isInitialized || startup()) {

            // just to be sure, check before we sleep
            if (getNewData(data)) {
                return true;
            }

            // sleep till signaled
            windowsService.waitForSingleObject(dataValidEvent, timeOut);

            // sleep if error
            if (timeOut > 0) {
                Thread.sleep(timeOut);
            }

            // we woke up, so check for data
            if (getNewData(data)) {
                return true;
            } else {
                return false;
            }
        }

        // sleep if error
        if (timeOut > 0) {
            Thread.sleep(timeOut);
        }

        return false;
    }

    public boolean isConnected() {
        if (isInitialized) {
            long elapsedTime = Duration.between(lastValidTime.toInstant(), Instant.now()).getSeconds();
            return (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0 && elapsedTime < timeout;
        }
        return false;
    }

    public Header getHeader() {
        if (isInitialized) {
            return header;
        }
        return null;
    }

    public String getData(int index) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public String getSessionInfoStr() {
        if (isInitialized) {
            return new String(sharedMemory.getByteArray(header.getSessionInfoOffset(), header.getSessionInfoLen()));
        }
        return "";
    }

    public int getSessionInfoStrUpdate() {
        if (isInitialized) {
            return header.getSessionInfoUpdate();
        }
        return -1;
    }

    public VarHeader getVarHeaderPtr() throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public VarHeader getVarHeaderEntry(int index) {
        if (isInitialized) {
            if (index >= 0 && index < header.getNumVars()) {
                return new VarHeader(ByteBuffer.wrap(
                        sharedMemory.getByteArray(header.getHeaderOffset() + (VarHeader.SIZEOF_VAR_HEADER * index),
                                                  VarHeader.SIZEOF_VAR_HEADER)));
            }
        }
        return null;
    }

    public int varNameToIndex(String name) {
        VarHeader vh;

        if (!name.isEmpty()) {
            for (int index = 0; index < header.getNumVars(); index++) {
                vh = getVarHeaderEntry(index);
                if (vh != null && vh.getName().equals(name)) {
                    return index;
                }
            }
        }

        return -1;
    }

    public int varNameToOffset(String name) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public int getBroadcastMsgID() {
        return windowsService.registerWindowMessage(Constant.IRSDK_BROADCASTMSGNAME);
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2, int var3) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, float var2) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
    }

    public int padCarNum(int num, int zero) {
        int retVal = num;
        int numPlace = 1;

        if (num > 99) {
            numPlace = 3;
        } else if (num > 9) {
            numPlace = 2;
        }

        if (zero != 0) {
            numPlace += zero;
            retVal = num + 1000 * numPlace;
        }

        return retVal;
    }

}
