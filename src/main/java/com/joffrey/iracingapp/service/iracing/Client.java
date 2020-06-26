package com.joffrey.iracingapp.service.iracing;

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
    private int        test          = 1;

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

    private boolean checkIdx() {
        return true;
    }

    public double getVarDouble(int idx, int entry) {

        if (checkIdx()) {

            if (isConnected()) {
                final VarHeader vh = utils.getVarHeaderEntry(idx);
                if (vh != null) {
                    if (entry >= 0 && entry < vh.getCount()) {

                        byte buffer = data.get(vh.getOffset());

                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            case irsdk_double:
                                return data.getDouble(vh.getOffset() + (VarTypeBytes.IRSDK_DOUBLE.getValue() * idx));
                        }

                    }
                }
            }
        }
        return 0.0;

    }

    public int getVarInt(int idx, int entry) {

        if (checkIdx()) {

            if (isConnected()) {
                VarHeader vh = utils.getVarHeaderEntry(idx);
                if (vh != null) {
                    if (entry >= 0 && entry < vh.getCount()) {
                        VarType vhType = VarType.get(vh.getType());
                        switch (vhType) {
                            case irsdk_int :
                                return data.getInt(vh.getOffset() + (VarTypeBytes.IRSDK_INT.getValue() * idx));
                        }

                    }
                }

            }
        }
        return 0;
    }

}
