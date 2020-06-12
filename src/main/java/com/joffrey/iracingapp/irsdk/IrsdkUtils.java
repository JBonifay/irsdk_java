package com.joffrey.iracingapp.irsdk;


import com.joffrey.iracingapp.windows.Handle;
import com.joffrey.iracingapp.windows.Pointer;
import com.joffrey.iracingapp.windows.WindowsService;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IrsdkUtils {

    private final WindowsService windowsService;

    private int         lastTickCount  = Integer.MAX_VALUE;
    private boolean     isInitialized  = false;
    private Handle      memMapFile     = null;
    private Handle      dataValidEvent = null;
    private Pointer     sharedMemory   = null;
    private IrsdkHeader irsdkHeader    = null;
    private Timestamp   lastValidTime  = new Timestamp(System.currentTimeMillis());
    private double      timeout        = 30.0; // timeout after 30 seconds with no communication

    public boolean waitForDataReady(int timeOut, String data) throws InterruptedException {
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

    public boolean startup() {

        // Try to open Memory Mapped File
        if (Objects.isNull(memMapFile)) {
            memMapFile = windowsService.openMemoryMapFile(IrsdkDefines.IRSDK_MEMMAPFILENAME);
            lastTickCount = Integer.MAX_VALUE;
        }

        if (Objects.nonNull(memMapFile)) {
            if (Objects.isNull(sharedMemory)) {
                sharedMemory = windowsService.mapViewOfFile(memMapFile);
                irsdkHeader = new IrsdkHeader(ByteBuffer.wrap(sharedMemory.getByteArray(0, IrsdkHeader.HEADER_SIZE)));
                lastTickCount = Integer.MAX_VALUE;
            }
        }

        if (Objects.isNull(sharedMemory)) {
            if (Objects.isNull(dataValidEvent)) {
                dataValidEvent = windowsService.openEvent(IrsdkDefines.IRSDK_DATAVALIDEVENTNAME);
                lastTickCount = Integer.MAX_VALUE;
            }
        }

        if (Objects.nonNull(dataValidEvent)) {
            isInitialized = true;
            return isInitialized;
        }

        isInitialized = false;
        return isInitialized;
    }

    public boolean getNewData(String data) {
        if (isInitialized || startup()) {

            if (irsdkHeader.getStatus() != IrsdkStatusField.IRSDK_STCONNECTED.getValue()) {
                lastTickCount = Integer.MAX_VALUE;
                return false;
            }

            int latest = 0;
            for (int i = 1; i < irsdkHeader.getNumBuf(); i++) {
                if (irsdkHeader.getVarBuf()[latest].getTickCount() < irsdkHeader.getVarBuf()[i].getTickCount()) {
                    latest = i;
                }
            }

            if (lastTickCount < irsdkHeader.getVarBuf()[latest].getTickCount()) {
                if (Objects.nonNull(data)) {
                    for (int count = 0; count < 2; count++) {

                        int curTickCount = irsdkHeader.getVarBuf()[latest].getTickCount();
                        // memcpy(data, pSharedMem + pHeader->varBuf[latest].bufOffset, pHeader->bufLen)
                        if (curTickCount == irsdkHeader.getVarBuf()[latest].getTickCount()) {
                            lastTickCount = curTickCount;
                            lastValidTime = null;
                            return true;
                        }
                    }

                    return false;

                } else {
                    lastTickCount = irsdkHeader.getVarBuf()[latest].getTickCount();
                    lastValidTime = null;
                    return true;
                }

            } else if (lastTickCount > irsdkHeader.getVarBuf()[latest].getTickCount()) {
                lastTickCount = irsdkHeader.getVarBuf()[latest].getTickCount();
                return false;
            }
        }
        return false;
    }

    boolean isConnected() {
        if (isInitialized) {
            Timestamp elapsedTime = new Timestamp(System.currentTimeMillis() - lastValidTime.getTime());
            return (irsdkHeader.getStatus() & IrsdkStatusField.IRSDK_STCONNECTED.getValue()) > 0
                    && elapsedTime.getTime() < timeout;
        }
        return false;
    }

    public IrsdkHeader getHeader() {
        if (isInitialized) {
            return irsdkHeader;
        }
        return null;
    }
}
