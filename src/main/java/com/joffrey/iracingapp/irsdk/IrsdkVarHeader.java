package com.joffrey.iracingapp.irsdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrsdkVarHeader {

    private int type;                                                 // irsdk_VarType
    private int offset;                                               // offset fron start of buffer row
    private int count;                                                // number of entrys (array)

    // so length in bytes would be irsdk_VarTypeBytes[type] * count
    private boolean countAsTime;
    private char[]  pad = new char[3];                                // (16 byte align)

    private String name;
    private String desc;
    private String unit;                                              // something like "kg/m^2"

}
