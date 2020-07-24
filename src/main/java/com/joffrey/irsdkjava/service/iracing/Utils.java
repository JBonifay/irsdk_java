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

package com.joffrey.irsdkjava.service.iracing;

import com.joffrey.irsdkjava.model.Header;
import com.joffrey.irsdkjava.model.VarHeader;
import com.joffrey.irsdkjava.model.defines.BroadcastMsg;
import com.joffrey.irsdkjava.model.defines.Constant;
import com.joffrey.irsdkjava.model.defines.StatusField;
import com.joffrey.irsdkjava.service.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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
                        data = ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarBuf(latest).getBufOffset(),
                                                                         header.getBufLen()));

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

    public String getData(int index) {
        if (isInitialized) {
            return new String(sharedMemory.getByteArray(header.getVarBuf(index).getBufOffset(), header.getBufLen()));
        }
        return "";
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

    public VarHeader getVarHeaderPtr() {
        if (isInitialized) {
            return new VarHeader(ByteBuffer.wrap(sharedMemory.getByteArray(header.getHeaderOffset(),
                                                                           VarHeader.SIZEOF_VAR_HEADER)));
        }
        return null;
    }

    public VarHeader getVarHeaderEntry(int index) {
        if (isInitialized) {
            if (index >= 0 && index < header.getNumVars()) {
                return new VarHeader(ByteBuffer.wrap(sharedMemory.getByteArray(header.getHeaderOffset()
                                                                               + (VarHeader.SIZEOF_VAR_HEADER * index),
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

    public int varNameToOffset(String name) {
        VarHeader vh;

        if (!name.isEmpty()) {
            for (int index = 0; index < header.getNumVars(); index++) {
                vh = getVarHeaderEntry(index);
                if (vh != null && vh.getName().equalsIgnoreCase(name)) {
                    return vh.getOffset();
                }
            }
        }
        return -1;
    }

    public int getBroadcastMsgID() {
        return windowsService.registerWindowMessage(Constant.IRSDK_BROADCASTMSGNAME);
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2, int var3) {
        broadcastMsg(msg, var1, (long) (var2 + var3));
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, float var2) {
        // multiply by 2^16-1 to move fractional part to the integer part
        int real = (int) (var2 * 65536.0f);

        broadcastMsg(msg, var1, real);
    }

    public void broadcastMsg(BroadcastMsg msg, int var1, int var2) {
        int msgId = getBroadcastMsgID();

        if (msgId != 0 && msg.getValue() >= 0 && msg.getValue() < BroadcastMsg.irsdk_BroadcastLast.getValue()) {
            boolean sent = windowsService.sendNotifyMessage(msgId, MAKELONG(msg.getValue(), var1), var2);
        }
    }

    /**
     * add a leading zero (or zeros) to a car number
     * to encode car #001 call padCarNum(1,2)
     * @param num the car number
     * @param zero the leading zero
     * @return the new CarNumber
     */
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

    /**
     * C++ MAKELONG for Java
     * @param var1 the first int
     * @param var2 the second int
     * @return
     */
    private int MAKELONG(int var1, int var2) {
        return (var2 << 16) | ((var1) & 0xFFFF);
    }

}
