package com.joffrey.iracingapp.model.iracing;

import com.joffrey.iracingapp.model.iracing.defines.Constant;
import com.sun.jna.Pointer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {

    public static final int HEADER_SIZE             = (12 * 4) + ((4 * 4) * 4); // 112
    public static final int NUMBER_OF_VARBUF_FIELDS = 4;
    public static final int ALIGNMENT               = 4;
    public static final int SIZEOF_VARBUF           = NUMBER_OF_VARBUF_FIELDS * ALIGNMENT;

    private Pointer sharedMemory;
    
    private int ver;                                                                      // this api header version, see IRSDK_VER
    private int status;                                                                   // bitfield using irsdk_StatusField
    private int tickRate;                                                                 // ticks per second (60 or 360 etc)

    // session information, updated periodicaly
    private int sessionInfoUpdate;                                                        // Incremented when session info changes
    private int sessionInfoLen;                                                           // Length in bytes of session info string
    private int sessionInfoOffset;                                                        // Session info, encoded in YAML format

    // State data, output at tickRate
    private int numVars;                                                                  // length of arra pointed to by varHeaderOffset
    private int varHeaderOffset;                                                          // offset to irsdk_varHeader[numVars] array, Describes the variables received in varBuf

    private int      numBuf;                                                              // <= IRSDK_MAX_BUFS (3 for now)
    private int      bufLen;                                                              // length in bytes for one line
    private int[]    pad1   = new int[2];                                                 // (16 byte align)
    private VarBuf[] varBuf = new VarBuf[Constant.IRSDK_MAX_BUFS];                        // buffers of data being written to

    public Header(Pointer sharedMemory) {
        this.sharedMemory = sharedMemory;
    }

    private ByteBuffer getHeaderByteBuffer() {
        ByteBuffer headerByteBuffer = ByteBuffer.allocate(HEADER_SIZE);
        headerByteBuffer.position(0);
        headerByteBuffer.put(ByteBuffer.wrap(sharedMemory.getByteArray(0, Header.HEADER_SIZE)));
        headerByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return headerByteBuffer;
    }

    public int getVarBufTickCount(int varBuf) {
        return getHeaderByteBuffer().getInt((varBuf * SIZEOF_VARBUF) + 48);
    }

    public int getVarBufOffset(int varBuf) {
        return getHeaderByteBuffer().getInt((varBuf * SIZEOF_VARBUF) + 52);
    }


    public int getVer() {
        return getHeaderByteBuffer().getInt(0);
    }

    public int getStatus() {
        return getHeaderByteBuffer().getInt(4);
    }

    public int getTickRate() {
        return getHeaderByteBuffer().getInt(8);
    }

    public int getSessionInfoUpdate() {
        return getHeaderByteBuffer().getInt(12);
    }

    public int getSessionInfoLen() {
        return getHeaderByteBuffer().getInt(16);
    }

    public int getSessionInfoOffset() {
        return getHeaderByteBuffer().getInt(20);
    }

    public int getNumVars() {
        return getHeaderByteBuffer().getInt(24);
    }

    public int getVarHeaderOffset() {
        return getHeaderByteBuffer().getInt(28);
    }

    public int getNumBuf() {
        return getHeaderByteBuffer().getInt(32);
    }

    public int getBufLen() {
        return getHeaderByteBuffer().getInt(36);
    }

}
