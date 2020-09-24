package com.joffrey.irsdkjava.sdk;

import com.joffrey.irsdkjava.library.livedata.service.LiveDataService;
import com.joffrey.irsdkjava.sdk.defines.StatusField;
import com.joffrey.irsdkjava.sdk.model.Header;
import com.joffrey.irsdkjava.sdk.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SdkStarter {

    private final WindowsService windowsService;

    @Autowired
    private LiveDataService liveDataService;

    private WinNT.HANDLE memMapFile     = null;
    private WinNT.HANDLE dataValidEvent = null;
    @Getter
    private Pointer      sharedMemory   = null;
    @Getter
    private Header       header         = null;

    private boolean isInitialized = false;
    private boolean wasConnected  = false;


    public boolean isConnected() {
        // keep track of connection status
        boolean isConnected;

        if (isInitialized || startup()) {
            isConnected = (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0;
        } else {
            isConnected = false;
        }

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

    private boolean startup() {
        // Try to open Memory Mapped File
        if (memMapFile == null) {
            memMapFile = windowsService.openMemoryMapFile(com.joffrey.irsdkjava.sdk.defines.Constant.IRSDK_MEMMAPFILENAME);
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
                    dataValidEvent = windowsService.openEvent(com.joffrey.irsdkjava.sdk.defines.Constant.IRSDK_DATAVALIDEVENTNAME);
                }
            }

            if (dataValidEvent != null) {
                isInitialized = true;
                return true;
            }

        }

        isInitialized = false;
        return false;
    }


}
