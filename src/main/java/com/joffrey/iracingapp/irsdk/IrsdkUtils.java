package com.joffrey.iracingapp.irsdk;


import com.joffrey.iracingapp.windows.Handle;
import com.joffrey.iracingapp.windows.Pointer;
import com.joffrey.iracingapp.windows.WindowsHelper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IrsdkUtils {

    private final WindowsHelper windowsHelper;

    private int     lastTickCount  = Integer.MAX_VALUE;
    private boolean isInitialized  = false;
    private Handle  memMapFile     = null;
    private Handle  dataValidEvent = null;
    private String  sharedMemory   = null;
    private Pointer header         = null;


    public boolean waitForDataReady(int timeout, String data) {
        if (isInitialized || startup()) {

            if (getNewData(data)) {
                return true;
            }


        }


    }

    private boolean startup() {

        // Try to open Memory Mapped File
        if (Objects.isNull(memMapFile)) {
            memMapFile = windowsHelper.openMemoryMapFile();
            lastTickCount = Integer.MAX_VALUE;
        }

        if (Objects.nonNull(memMapFile)) {
            if (Objects.isNull(sharedMemory)) {
                header = windowsHelper.mapViewOfFile(memMapFile);
                lastTickCount = Integer.MAX_VALUE;
            }
        }

        if (Objects.isNull(sharedMemory)) {
            if (Objects.isNull(dataValidEvent)) {
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

    private boolean getNewData(String data) {
        if (isInitialized || startup()) {



        }



    }

}
