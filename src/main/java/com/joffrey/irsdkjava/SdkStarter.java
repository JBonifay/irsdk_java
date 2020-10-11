package com.joffrey.irsdkjava;

import com.joffrey.irsdkjava.defines.StatusField;
import com.joffrey.irsdkjava.defines.VarTypeBytes;
import com.joffrey.irsdkjava.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
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

    private final Map<String, VarHeader> vars = new HashMap<>();

    private boolean isReady() {
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
            return false;
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
                fetchVars();
            } else {
                log.info("Lost connection to iRacing");
            }
            //****Note, put your connection handling here
            wasConnected = isConnected;
        }

        return isConnected;
    }

    public void fetchVars() {
        for (int index = 0; index < header.getNumVars(); index++) {
            VarHeader vh = getVarHeaderEntry(index);
            vars.put(vh.getName(), vh);
        }
    }

    private VarHeader getVarHeaderEntry(int index) {
        return new VarHeader(ByteBuffer.wrap(sharedMemory.getByteArray(header.getVarHeaderOffset() + (VarHeader.VAR_HEADER_SIZE
                                                                                                      * index),
                                                                       VarHeader.VAR_HEADER_SIZE)));
    }

    public boolean getVarBoolean(String varName) {
        return getVarBoolean(varName, 0);
    }

    public boolean getVarBoolean(String varName, int entry) {
        VarHeader varHeader = vars.get(varName);
        if (varHeader != null) {
            if (entry >= 0 && entry < varHeader.getCount()) {
                return (header.getLatestVarByteBuffer()
                              .getChar(varHeader.getOffset() + (entry * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;
            }
        }
        return false;
    }

    public int getVarInt(String varName) {
        return getVarInt(varName, 0);
    }

    public int getVarInt(String varName, int entry) {
        VarHeader vh = vars.get(varName);
        if (vh != null) {
            if (entry >= 0 && entry < vh.getCount()) {
                return header.getLatestVarByteBuffer().getInt(vh.getOffset() + (entry * VarTypeBytes.IRSDK_INT.getValue()));
            }
        }
        return 0;
    }

    public float getVarFloat(String varName) {
        return getVarFloat(varName, 0);
    }

    public float getVarFloat(String varName, int entry) {

        VarHeader vh = vars.get(varName);
        if (vh != null) {
            if (entry >= 0 && entry < vh.getCount()) {
                return header.getLatestVarByteBuffer().getFloat(vh.getOffset() + (entry * VarTypeBytes.IRSDK_FLOAT.getValue()));
            }
        }
        return 0.0F;
    }

    public double getVarDouble(String varName) {
        return getVarDouble(varName, 0);
    }

    public double getVarDouble(String varName, int entry) {
        VarHeader vh = vars.get(varName);
        if (vh != null) {
            if (entry >= 0 && entry < vh.getCount()) {
                return header.getLatestVarByteBuffer().getDouble(vh.getOffset() + (entry * VarTypeBytes.IRSDK_DOUBLE.getValue()));

            }
        }
        return 0.0;

    }


}
