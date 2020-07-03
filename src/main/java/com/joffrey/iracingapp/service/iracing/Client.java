package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.CVar;
import com.joffrey.iracingapp.model.iracing.VarHeader;
import com.joffrey.iracingapp.model.iracing.defines.VarType;
import com.joffrey.iracingapp.model.iracing.defines.VarTypeBytes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
                    data.order(ByteOrder.LITTLE_ENDIAN);
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

    public boolean isConnected() {
        return data != null && utils.isConnected();
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
                        // 1 byte
                        // 4 bytes
                        // 8 bytes
                        return switch (vhType) {
                            case irsdk_char,
                                    irsdk_bool -> (data.getChar(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                            case irsdk_int,
                                    irsdk_bitField -> (data.getInt(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                            case irsdk_float -> (data.getFloat(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                            case irsdk_double -> (data.getDouble(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_BOOL.getValue()))) != 0;

                            default -> throw new IllegalStateException("Unexpected value: " + vhType);
                        };
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

    public int getVarInt(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        // 1 byte
                        // 4 bytes
                        // 8 bytes
                        return switch (vhType) {
                            case irsdk_char,
                                    irsdk_bool -> (int) data.getChar(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_INT.getValue()));

                            case irsdk_int,
                                    irsdk_bitField -> (int) data.getInt(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_INT.getValue()));

                            case irsdk_float -> (int) data.getFloat(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_INT.getValue()));

                            case irsdk_double -> (int) data.getDouble(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_INT.getValue()));

                            default -> throw new IllegalStateException("Unexpected value: " + vhType);
                        };
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

    public float getVarFloat(CVar cVar) {

        if (checkIdx(cVar)) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(cVar.getIdx());
                if (vh != null) {
                    if (cVar.getEntry() >= 0 && cVar.getEntry() < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        // 1 byte
                        // 4 bytes
                        // 8 bytes
                        return switch (vhType) {
                            case irsdk_char,
                                    irsdk_bool -> (float) data.getChar(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_FLOAT.getValue()));

                            case irsdk_int,
                                    irsdk_bitField -> (float) data.getInt(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_FLOAT.getValue()));

                            case irsdk_float -> (float) data.getFloat(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_FLOAT.getValue()));

                            case irsdk_double -> (float) data.getDouble(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_FLOAT.getValue()));

                            default -> throw new IllegalStateException("Unexpected value: " + vhType);
                        };
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
                        // 1 byte
                        // 4 bytes
                        // 8 bytes
                        return switch (vhType) {
                            case irsdk_char,
                                    irsdk_bool -> (double) data.getChar(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                            case irsdk_int,
                                    irsdk_bitField -> (double) data.getInt(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                            case irsdk_float -> (double) data.getFloat(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                            case irsdk_double -> (double) data.getDouble(vh.getOffset() + (cVar.getEntry() * VarTypeBytes.IRSDK_DOUBLE.getValue()));

                            default -> throw new IllegalStateException("Unexpected value: " + vhType);
                        };
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

    //path is in the form of "DriverInfo:Drivers:CarIdx:{%d}UserName:"
    public int getSessionStrVal() {
        return -1;
    }

    public int getSessionStr() {
        if (isConnected()) {
            lastSessionCt = getSessionCt();
            return utils.getSessionInfoStr();
        }
        return -1;
    }

    public boolean wasSessionStrUpdated() {
        return lastSessionCt != utils.getSessionInfoStrUpdate();
    }

    private int getSessionCt() {
        return utils.getSessionInfoStrUpdate();
    }

    // ---------------------------
    // CVar
    private boolean checkIdx(CVar cvar) {
        if (isConnected()) {
            if (cvar.getStatusID() != this.statusID) {
                cvar.setIdx(getVarIdx(cvar.getName()));
            }
            return true;
        }
        return false;
    }

    public boolean isCVarValid(CVar cVar) {
        checkIdx(cVar);
        return cVar.getIdx() > -1;
    }
}
