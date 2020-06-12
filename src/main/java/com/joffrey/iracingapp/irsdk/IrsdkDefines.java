package com.joffrey.iracingapp.irsdk;

public class IrsdkDefines {

    public static final String IRSDK_DATAVALIDEVENTNAME = "Local\\IRSDKDataValidEvent";
    public static final String IRSDK_MEMMAPFILENAME     = "Local\\IRSDKMemMapFileName";
    public static final String IRSDK_BROADCASTMSGNAME   = "IRSDK_BROADCASTMSG";

    public static final int IRSDK_MAX_BUFS   = 4;
    public static final int IRSDK_MAX_STRING = 32;
    // descriptions can be longer than max_string!
    public static final int IRSDK_MAX_DESC   = 64;

    public static final int   IRSDK_UNLIMITED_LAPS = 32767;
    public static final float IRSDK_UNLIMITED_TIME = 604800.0f;

    public static final int IRSDK_VER = 2;

}
