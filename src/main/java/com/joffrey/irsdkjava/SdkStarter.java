package com.joffrey.irsdkjava;

import com.joffrey.irsdkjava.defines.StatusField;
import com.joffrey.irsdkjava.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SdkStarter {

    private final WindowsService windowsService;

    private WinNT.HANDLE memMapFile     = null;
    private WinNT.HANDLE dataValidEvent = null;
    @Getter
    private Pointer      sharedMemory   = null;
    @Getter
    private Header       header         = null;

    private boolean isInitialized = false;
    private boolean wasConnected  = false;

    public boolean isReady() {
        if (!isInitialized) {
            // Try to open Memory Mapped File
            if (memMapFile == null) {
                memMapFile = windowsService.openMemoryMapFile(com.joffrey.irsdkjava.defines.Constant.IRSDK_MEMMAPFILENAME);
            }

            if (memMapFile != null) {
                if (sharedMemory == null) {
                    sharedMemory = windowsService.mapViewOfFile(memMapFile);
                    header = new Header(sharedMemory);

                    if (header.getByteBuffer() == null) {
                        return false;
                    }

                }

                if (sharedMemory != null) {
                    if (dataValidEvent == null) {
                        dataValidEvent = windowsService.openEvent(com.joffrey.irsdkjava.defines.Constant.IRSDK_DATAVALIDEVENTNAME);
                    }
                }

                if (dataValidEvent != null) {
                    isInitialized = true;
                    return true;
                }

                return false;
            }

        }
        return true;
    }

    public boolean isRunning() {
        boolean isConnected;

        if (isReady()) {
            isConnected = (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0;
        } else {
            isConnected = false;
        }

        // keep track of connection status
        if (wasConnected != isConnected) {
            if (isConnected) {
                log.info("Connected to iRacing.");
            } else {
                log.info("Lost connection to iRacing");
            }
            //****Note, put your connection handling here
            wasConnected = isConnected;
        }

        return isConnected;
    }


}
