package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.model.iracing.Header;
import com.joffrey.iracingapp.model.iracing.VarHeader;
import com.joffrey.iracingapp.model.iracing.defines.BroadcastMsg;
import com.joffrey.iracingapp.model.iracing.defines.Constant;
import com.joffrey.iracingapp.model.iracing.defines.StatusField;
import com.joffrey.iracingapp.model.windows.Handle;
import com.joffrey.iracingapp.model.windows.Pointer;
import com.joffrey.iracingapp.service.windows.WindowsService;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Utils {

    private final WindowsService windowsService;

    private Handle  memMapFile     = null;
    private Handle  dataValidEvent = null;
    private Pointer sharedMemory   = null;

    private Header                 header        = null;
    private Map<String, VarHeader> varHeaderList = null;

    private int     lastTickCount = Integer.MAX_VALUE;
    private boolean isInitialized = false;

    private double    timeout       = 30.0; // timeout after 30 seconds with no communication
    private Timestamp lastValidTime = new Timestamp(System.currentTimeMillis());

    public boolean startup() {

        // Try to open Memory Mapped File
        if (memMapFile == null) {
            memMapFile = windowsService.openMemoryMapFile(Constant.IRSDK_MEMMAPFILENAME);
            lastTickCount = Integer.MAX_VALUE;
        }

        if (memMapFile != null) {
            if (sharedMemory == null) {
                sharedMemory = windowsService.mapViewOfFile(memMapFile);
                // header = new Header(ByteBuffer.wrap(sharedMemory.getByteArray(0, Header.HEADER_SIZE)));
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

            if (header.getStatus() != StatusField.IRSDK_STCONNECTED.getValue()) {
                lastTickCount = Integer.MAX_VALUE;
                return false;
            }

            int latest = 0;
            for (int i = 1; i < header.getNumBuf(); i++) {
                if (header.getVarBufTickCount(latest) < header.getVarBufTickCount(i)) {
                    latest = i;
                }
            }

            if (lastTickCount < header.getVarBufTickCount(latest)) {
                if (Objects.nonNull(data)) {
                    for (int count = 0; count < 2; count++) {

                        int curTickCount = header.getVarBufTickCount(latest);

                        // memcpy(data, pSharedMem + pHeader->varBuf[latest].bufOffset, pHeader->bufLen)
                        data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarHeaderOffset(),
                                                                         header.getNumVars() * VarHeader.SIZEOF_VAR_HEADER));

                        varHeaderList = VarHeader.getVarheaderList(header.getNumVars(), data);

                        if (curTickCount == header.getVarBufTickCount(latest)) {
                            lastTickCount = curTickCount;
                            lastValidTime = new Timestamp(System.currentTimeMillis());
                            return true;
                        }
                    }

                    return false;

                } else {
                    lastTickCount = header.getVarBufTickCount(latest);
                    lastValidTime = new Timestamp(System.currentTimeMillis());
                    return true;
                }

            } else if (lastTickCount > header.getVarBufTickCount(latest)) {
                lastTickCount = header.getVarBufTickCount(latest);
                return false;
            }
        }
        return false;
    }

    public boolean waitForDataReady(int timeOut, ByteBuffer data) throws InterruptedException {
        if (isInitialized || startup()) {

            // just to be sure, check before we sleep
            if (getNewData(data)) {
                return true;
            }

            // sleep till signaled
            windowsService.waitForSingleObject(dataValidEvent, timeOut);

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
            Timestamp elapsedTime = new Timestamp(System.currentTimeMillis() - lastValidTime.getTime());
            return (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0 && elapsedTime.getTime() < timeout;
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

    public int getSessionInfoStr() {
        if (isInitialized) {
            return header.getSessionInfoOffset();
        }
        return -1;
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
                // header.getVarHeaderOffset()
                return new VarHeader(ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarHeaderOffset() + index,
                                                                               header.getVarHeaderOffset()
                                                                               + index
                                                                               + VarHeader.SIZEOF_VAR_HEADER)));
            }
        }
        return null;
    }

    public int varNameToIndex(String name) throws NotImplementedException {
        throw new NotImplementedException("Not Impl");
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
