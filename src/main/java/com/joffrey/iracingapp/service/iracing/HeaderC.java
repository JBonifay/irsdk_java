package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.model.iracing.Header;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HeaderC {

    public final static int IRSDK_MAX_BUFS          = 4;
    public final static int NUMBER_OF_HEADER_FIELDS = 12;
    public final static int NUMBER_OF_VARBUF_FIELDS = 4;
    public final static int ALIGNMENT               = 4;
    public final static int SIZEOF_VARBUF           = NUMBER_OF_VARBUF_FIELDS * ALIGNMENT;
    public final static int SIZEOF_HEADER           = (NUMBER_OF_HEADER_FIELDS * ALIGNMENT) + (SIZEOF_VARBUF * IRSDK_MAX_BUFS);

    private ByteBuffer m_bytebuffer;

    public HeaderC(ByteBuffer buffer) {
        //make a copy of the buffer
        m_bytebuffer = ByteBuffer.allocate(SIZEOF_HEADER);
        m_bytebuffer.position(0);
        buffer.position(0);
        m_bytebuffer.put(buffer);
        m_bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int getVer() {
        return m_bytebuffer.getInt(0);
    }

    public int getStatus() {
        return m_bytebuffer.getInt(4);
    }

    public int getTickRate() {
        return m_bytebuffer.getInt(8);
    }

    public int getSessionInfoUpdate() {
        return m_bytebuffer.getInt(12);
    }

    public int getSessionInfoLen() {
        return m_bytebuffer.getInt(16);
    }

    public int getSessionInfoOffset() {
        return m_bytebuffer.getInt(20);
    }

    public int getNumVars() {
        return m_bytebuffer.getInt(24);
    }

    public int getVarHeaderOffset() {
        return m_bytebuffer.getInt(28);
    }

    public int getNumBuf() {
        return m_bytebuffer.getInt(32);
    }

    public int getBufLen() {
        return m_bytebuffer.getInt(36);
    }

    public int getVarBuf_TickCount(int varBuf) {
        return m_bytebuffer.getInt((varBuf * SIZEOF_VARBUF) + 48);
    }

    public int getVarBuf_BufOffset(int varBuf) {
        return m_bytebuffer.getInt((varBuf * SIZEOF_VARBUF) + 52);
    }

    public int getLatest_VarBuf() {
        int varBufTick = 0;
        int varBuf = 0;

        for (int i = 0; i < getNumBuf(); i++) {
            int x = getVarBuf_TickCount(i);
            if (x > varBufTick) {
                varBufTick = x;
                varBuf = i;
            }
        }
        return varBuf;
    }

    public int getLatest_VarBufTick() {
        int varBufTick = 0;

        for (int i = 0; i < getNumBuf(); i++) {
            int x = getVarBuf_TickCount(i);
            if (x > varBufTick) {
                varBufTick = x;
            }
        }
        return varBufTick;
    }

    public HeaderC getLatest_VarBufTick(int tick) {
        int varBuf = getLatest_VarBuf();
        m_bytebuffer.putInt((varBuf * SIZEOF_VARBUF) + 48, tick);
        return this;
    }

    public boolean isValid() {
        if (getVer() == 0
            || getTickRate() == 0
            || (getSessionInfoUpdate() > 0 && (getSessionInfoLen() == 0
                                               || getSessionInfoOffset() == 0))
            || getNumVars() == 0
            || getVarHeaderOffset() == 0
            || getNumBuf() == 0
            || getBufLen() == 0) {
            return false;
        }
        return true;
    }

    public boolean isValid(HeaderC header) {
        if (!isValid())
            return false;

        //	    #if the tickcount of the latest buffer in the 1st header is different in the second header,
        //	    #fail because shared memory changed while reading it
        //	    #iRacing gives us 16ms to read it from the time the timer lets us know it's there.

        if (header != null && (getVarBuf_TickCount(getLatest_VarBuf()) != header.getVarBuf_TickCount(getLatest_VarBuf())
                               || getSessionInfoUpdate() != header.getSessionInfoUpdate())) {
            return false;
        }

        return true;
    }

    public String toJSON() {
        StringBuffer json = new StringBuffer();
        json.append("{ \"header\": {\n");
        json.append(String.format("  \"size\": %d,\n", Header.HEADER_SIZE));
        json.append(String.format("  \"ver\": %d,\n", this.getVer()));
        json.append(String.format("  \"status\": \"0x%08x\",\n", this.getStatus()));
        json.append(String.format("  \"tickRate\": %d,\n", this.getTickRate()));
        json.append(String.format("  \"sessionInfoUpdate\": %d,\n", this.getSessionInfoUpdate()));
        json.append(String.format("  \"sessionInfoLen\": %d,\n", this.getSessionInfoLen()));
        json.append(String.format("  \"sessionInfoOffset\": %d,\n", this.getSessionInfoOffset()));
        json.append(String.format("  \"numVars\": %d,\n", this.getNumVars()));
        json.append(String.format("  \"varHeaderOffset\": %d,\n", this.getVarHeaderOffset()));
        json.append(String.format("  \"numBuf\": %d,\n", this.getNumBuf()));
        json.append(String.format("  \"bufLen\": %d,\n", this.getBufLen()));
        json.append("  \"varBuf\": [");
        for (int i = 0; i < this.getNumBuf(); i++) {
            json.append(String.format("    { \"tickcount\": %d, \"bufOffset\": %d }", this.getVarBuf_TickCount(i),
                                      this.getVarBuf_BufOffset(i)));
            if (i < (this.getNumBuf() - 1))
                json.append(",");
            json.append("\n");
        }
        json.append("   ],\n");
        json.append(String.format("  \"latest_varBufTick\": %d,\n", this.getLatest_VarBufTick()));
        json.append(String.format("  \"latest_varBuf\": %d\n", this.getLatest_VarBuf()));
            json.append("}\n");
        json.append("}");
        return json.toString();
    }
}
