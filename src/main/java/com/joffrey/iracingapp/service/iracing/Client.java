package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.CVar;
import com.joffrey.iracingapp.model.iracing.VarHeader;
import com.joffrey.iracingapp.model.iracing.defines.VarType;
import com.joffrey.iracingapp.model.iracing.defines.VarTypeBytes;
import java.nio.ByteBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Client {

    private final Utils utils;

    private ByteBuffer data;
    private int        nData;
    private int        statusID      = 0;
    private int        lastSessionCt = -1;

    public boolean isConnected() {
        return data != null && utils.isConnected();
    }

    public boolean waitForData(int timeoutMS) throws InterruptedException {

        // wait for start of session or new data
        if (utils.waitForDataReady(timeoutMS, data) && utils.getHeader() != null) {

            // if new connection, or data changed lenght then init
            if (data != null || nData != utils.getHeader().getBufLen()) {

                // allocate memory to hold incoming data from sim
                if (data != null) {
                    data = null;
                }
                nData = utils.getHeader().getBufLen();
                data = ByteBuffer.allocate(nData);

                // indicate a new connection
                statusID++;

                // reset session info str status
                lastSessionCt = -1;

                // and try to fill in the data
                if (utils.getNewData(data)) {
                    data = utils.getDataBuffer();
                    return true;
                }

            } else if (data != null) {
                // else we are already initialized, and data is ready for processing
                return true;
            }

        } else if (!isConnected()) {
            if (data != null) {
                data = null;
                lastSessionCt = -1;
            }
        }
        return false;
    }

    public boolean wasSessionStrUpdated() {
        return lastSessionCt != utils.getSessionInfoStrUpdate();
    }

    public int getSessionStr() {
        if (isConnected()) {
            lastSessionCt = getSessionCt();
            return utils.getSessionInfoStr();
        }
        return -1;
    }

    private int getSessionCt() {
        return utils.getSessionInfoStrUpdate();
    }

    private boolean checkIdx(CVar cvar) {
        if (isConnected()) {
            if (cvar.getStatusID() != this.statusID) {
                cvar.setIdx(getVarIdx(cvar.getName()));
            }
            return true;
        }
        return false;
    }

    public int getVarIdx(String varName) {
        if (isConnected()) {
            return utils.varNameToIndex(varName);
        }
        return -1;
    }

    public int getVarType(int idx) {
        if (isConnected()) {
            VarHeader vh = utils.getVarHeaderEntry(idx);
            if (vh != null) {
                return vh.getType();
            } else {
                // invalid variable index
            }
        }
        return 0;
    }

    public int getVarCount(int idx) {
        if (isConnected()) {
            VarHeader vh = utils.getVarHeaderEntry(idx);
            if (vh != null) {
                return vh.getCount();
            } else {
                // invalid variable index
            }
        }
        return 0;
    }

    public boolean getVarBoolean(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            // 1 byte
                            case irsdk_char:
                            case irsdk_bool:
                                return (data.getChar(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry()))))
                                       != 0;

                            // 4 bytes
                            case irsdk_int:
                            case irsdk_bitField:
                                return (data.getInt(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry()))))
                                       != 0;

                            case irsdk_float:
                                return (data.getFloat(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry()))))
                                       != 0;

                            // 8 bytes
                            case irsdk_double:
                                return (data.getDouble(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry()))))
                                       != 0;

                        }
                    } else {
                        // invalid offset
                    }
                } else {
                    //invalid variable index
                }
            }
        }
        return false;

    }

    public float getVarFloat(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            // 1 byte
                            case irsdk_char:
                            case irsdk_bool:
                                return (float) data.getChar(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            // 4 bytes
                            case irsdk_int:
                            case irsdk_bitField:
                                return (float) data.getInt(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            case irsdk_float:
                                return (float) data.getFloat(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            // 8 bytes
                            case irsdk_double:
                                return (float) data.getDouble(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                        }
                    } else {
                        // invalid offset
                    }
                } else {
                    //invalid variable index
                }
            }
        }
        return 0.0F;

    }

    public double getVarDouble(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            // 1 byte
                            case irsdk_char:
                            case irsdk_bool:
                                return (double) data.getChar(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            // 4 bytes
                            case irsdk_int:
                            case irsdk_bitField:
                                return (double) data.getInt(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            case irsdk_float:
                                return (double) data.getFloat(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            // 8 bytes
                            case irsdk_double:
                                return (double) data.getDouble(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry())));

                        }
                    } else {
                        // invalid offset
                    }
                } else {
                    //invalid variable index
                }
            }
        }
        return 0.0;

    }

    public int getVarInt(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            // 1 byte
                            case irsdk_char:
                            case irsdk_bool:
                                return (int) data.getChar(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry())));

                            // 4 bytes
                            case irsdk_int:
                            case irsdk_bitField:
                                return (int) data.getInt(
                                        VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (cVar.getIdx()
                                                                                                            + cVar.getEntry())));

                            case irsdk_float:
                                return (int) data.getFloat(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                            // 8 bytes
                            case irsdk_double:
                                return (int) data.getDouble(VarHeader.SIZEOF_VAR_HEADER + (VarTypeBytes.IRSDK_INT.getValue() * (
                                        cVar.getIdx()
                                        + cVar.getEntry())));

                        }
                    } else {
                        // invalid offset
                    }
                } else {
                    //invalid variable index
                }
            }
        }
        return 0;
    }

}
