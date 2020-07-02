package com.joffrey.iracingapp.model.iracing;

import com.joffrey.iracingapp.model.iracing.defines.Constant;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Getter;

@Getter
public class Header {

    public static final int HEADER_SIZE             = (12 * 4) + ((4 * 4) * 4); // 112
    public static final int NUMBER_OF_VARBUF_FIELDS = 4;
    public static final int ALIGNMENT               = 4;
    public static final int SIZEOF_VARBUF           = NUMBER_OF_VARBUF_FIELDS * ALIGNMENT;

    private final int ver;                                                                      // this api header version, see IRSDK_VER
    private final int status;                                                                   // bitfield using irsdk_StatusField
    private final int tickRate;                                                                 // ticks per second (60 or 360 etc)

    // session information, updated periodicaly
    private final int sessionInfoUpdate;                                                        // Incremented when session info changes
    private final int sessionInfoLen;                                                           // Length in bytes of session info string
    private final int sessionInfoOffset;                                                        // Session info, encoded in YAML format

    // State data, output at tickRate
    private final int numVars;                                                                  // length of arra pointed to by varHeaderOffset
    private final int varHeaderOffset;                                                          // offset to irsdk_varHeader[numVars] array, Describes the variables received in varBuf

    private final int      numBuf;                                                              // <= IRSDK_MAX_BUFS (3 for now)
    private final int      bufLen;                                                              // length in bytes for one line
    private final int[]    pad1   = new int[2];                                                 // (16 byte align)
    private final VarBuf[] varBuf = new VarBuf[]{new VarBuf(), new VarBuf(), new VarBuf(), new VarBuf()};
    // buffers of data being written to

    public Header(ByteBuffer headerByteBuffer) {
        headerByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.ver = headerByteBuffer.getInt(0);
        this.status = headerByteBuffer.getInt(4);
        this.tickRate = headerByteBuffer.getInt(8);
        this.sessionInfoUpdate = headerByteBuffer.getInt(12);
        this.sessionInfoLen = headerByteBuffer.getInt(16);
        this.sessionInfoOffset = headerByteBuffer.getInt(20);
        this.numVars = headerByteBuffer.getInt(24);
        this.varHeaderOffset = headerByteBuffer.getInt(28);
        this.numBuf = headerByteBuffer.getInt(32);
        this.bufLen = headerByteBuffer.getInt(36);
        this.pad1[0] = headerByteBuffer.getInt(40);
        this.pad1[1] = headerByteBuffer.getInt(44);
        this.varBuf[0].setTickCount(headerByteBuffer.getInt(48));
        this.varBuf[0].setBufOffset(headerByteBuffer.getInt(52));

        this.varBuf[1].setTickCount(headerByteBuffer.getInt(64));
        this.varBuf[1].setBufOffset(headerByteBuffer.getInt(68));

        this.varBuf[2].setTickCount(headerByteBuffer.getInt(80));
        this.varBuf[2].setBufOffset(headerByteBuffer.getInt(84));

        this.varBuf[3].setTickCount(headerByteBuffer.getInt(96));
        this.varBuf[3].setBufOffset(headerByteBuffer.getInt(100));
    }


}
