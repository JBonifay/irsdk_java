package com.joffrey.iracingapp.model.iracing;

import com.joffrey.iracingapp.model.iracing.defines.Constant;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VarHeader {

    public final static int NUMBER_OF_FIELDS  = 4;
    public final static int SIZEOF_FIELDS     = 4;
    public final static int SIZEOF_VAR_HEADER = (NUMBER_OF_FIELDS * SIZEOF_FIELDS)
                                                + Constant.IRSDK_MAX_STRING
                                                + Constant.IRSDK_MAX_DESC
                                                + Constant.IRSDK_MAX_STRING;

    private int type;                                                 // irsdk_VarType
    private int offset;                                               // offset fron start of buffer row
    private int count;                                                // number of entrys (array)

    // so length in bytes would be irsdk_VarTypeBytes[type] * count
    private boolean countAsTime;
    private char[]  pad = new char[3];                                // (16 byte align)

    private String name;
    private String desc;
    private String unit;                                              // something like "kg/m^2"
    private Object value;

    public VarHeader(ByteBuffer buffer) {
        createVarHeader(buffer, 0);
    }

    public static Map<String, VarHeader> getVarheaderList(int numberOfVar, ByteBuffer buffer) {

        Map<String, VarHeader> varHeaderMap = new TreeMap<>();

        for (int i = 0; i < numberOfVar; i++) {
            int varOffset = i * SIZEOF_VAR_HEADER;

            byte[] transferbuffer;

            VarHeader varHeader = createVarHeader(buffer, varOffset);

            //now put it in the cache
            varHeaderMap.put(varHeader.getName(), varHeader);
        }

        return varHeaderMap;

    }

    private static VarHeader createVarHeader(ByteBuffer buffer, int varOffset) {
        byte[] transferbuffer;
        VarHeader varHeader = new VarHeader();
        varHeader.setType(buffer.get(varOffset));
        varHeader.setOffset(buffer.get(varOffset + 4));
        varHeader.setCount(buffer.get(varOffset + 8));

        transferbuffer = new byte[Constant.IRSDK_MAX_STRING];
        buffer.position(varOffset + 16);
        buffer.get(transferbuffer, 0, transferbuffer.length);
        varHeader.setName(new String(transferbuffer).replaceAll("[\000]", ""));

        buffer.position(varOffset + 16 + Constant.IRSDK_MAX_STRING + Constant.IRSDK_MAX_DESC);
        buffer.get(transferbuffer, 0, transferbuffer.length);
        varHeader.setUnit(new String(transferbuffer).replaceAll("[\000]", ""));

        transferbuffer = new byte[Constant.IRSDK_MAX_DESC];
        buffer.position(varOffset + 16 + Constant.IRSDK_MAX_STRING);
        buffer.get(transferbuffer, 0, transferbuffer.length);
        varHeader.setDesc(new String(transferbuffer).replaceAll("[\000]", ""));

        varHeader.setValue(null); //to be filled in later
        return varHeader;
    }

    public void clear() {
        type = 0;
        offset = 0;
        count = 0;
        countAsTime = false;
        name = "";
        desc = "";
        unit = "";
    }

}
