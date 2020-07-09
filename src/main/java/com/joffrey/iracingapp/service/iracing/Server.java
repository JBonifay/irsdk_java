package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.model.defines.VarType;

public class Server {

    // log in realtime to clients
    public static final int IRSDK_LOG_LIVE = 1;
    // log to disk
    public static final int IRSDK_LOG_DISK = 2;
    // log both to realtime and to disk
    public static final int IRSDK_LOG_ALL = (IRSDK_LOG_LIVE | IRSDK_LOG_DISK);

    public int getRegVar(String dynamicVar, int dynamicVar1, VarType irsdk_int, int i, String s, String m, int irsdkLogAll) {
        return 0;
    }

    public boolean isHeaderFinalized() {
        return false;
    }

    public void finalizeHeader() {

    }

    public boolean isInitialized() {
        return false;
    }

    public void resetSampleVars() {

    }
}
