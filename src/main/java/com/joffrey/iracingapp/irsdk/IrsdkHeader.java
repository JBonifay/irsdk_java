package com.joffrey.iracingapp.irsdk;

import java.nio.ByteBuffer;
import lombok.Data;

@Data
public class IrsdkHeader {

    public static final int HEADER_SIZE = (12 * 4) + ((4 * 4) * 4); // 112

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

    private int           numBuf;                                                         // <= IRSDK_MAX_BUFS (3 for now)
    private int           bufLen;                                                         // length in bytes for one line
    private int[]         pad1   = new int[2];                                            // (16 byte align)
    private IrsdkVarBuf[] varBuf = new IrsdkVarBuf[IrsdkDefines.IRSDK_MAX_BUFS];          // buffers of data being written to

    public IrsdkHeader(ByteBuffer byteBuffer) {
        this.headerByteBuffer = byteBuffer;
        this.ver = byteBuffer.getInt(0);
        this.status = byteBuffer.getInt(4);
        this.tickRate = byteBuffer.getInt(8);
        this.sessionInfoUpdate = byteBuffer.getInt(12);
        this.sessionInfoLen = byteBuffer.getInt(16);
        this.sessionInfoOffset = byteBuffer.getInt(20);
        this.numVars = byteBuffer.getInt(24);
        this.varHeaderOffset = byteBuffer.getInt(28);
        this.numBuf = byteBuffer.getInt(32);
        this.bufLen = byteBuffer.getInt(36);
        this.pad1 = pad1;
        this.varBuf = varBuf;
    }


}
