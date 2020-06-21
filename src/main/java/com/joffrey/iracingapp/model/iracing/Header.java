package com.joffrey.iracingapp.model.iracing;

import com.joffrey.iracingapp.model.iracing.defines.Constant;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;

public class Header {

    public static final int HEADER_SIZE             = (12 * 4) + ((4 * 4) * 4); // 112
    public static final int NUMBER_OF_VARBUF_FIELDS = 4;
    public static final int ALIGNMENT               = 4;
    public static final int SIZEOF_VARBUF           = NUMBER_OF_VARBUF_FIELDS * ALIGNMENT;

    private ByteBuffer headerByteBuffer;

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

    public Header(ByteBuffer byteBuffer) {
        headerByteBuffer = ByteBuffer.allocate(HEADER_SIZE);
        headerByteBuffer.position(0);
        byteBuffer.position(0);
        headerByteBuffer.put(byteBuffer);
        headerByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int getVarBuf_TickCount(int varBuf) {
        return headerByteBuffer.getInt((varBuf * SIZEOF_VARBUF) + 48);
    }

    public int getVer() {
        return headerByteBuffer.getInt(0);
    }

    public int getStatus() {
        return headerByteBuffer.getInt(4);
    }

    public int getTickRate() {
        return headerByteBuffer.getInt(8);
    }

    public int getSessionInfoUpdate() {
        return headerByteBuffer.getInt(12);
    }

    public int getSessionInfoLen() {
        return headerByteBuffer.getInt(16);
    }

    public int getSessionInfoOffset() {
        return headerByteBuffer.getInt(20);
    }

    public int getNumVars() {
        return headerByteBuffer.getInt(24);
    }

    public int getVarHeaderOffset() {
        return headerByteBuffer.getInt(28);
    }

    public int getNumBuf() {
        return headerByteBuffer.getInt(32);
    }

    public int getBufLen() {
        return headerByteBuffer.getInt(36);
    }


}
