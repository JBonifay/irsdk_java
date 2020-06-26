package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.CVar;
import com.joffrey.iracingapp.model.iracing.VarHeader;
import java.nio.ByteBuffer;
import java.util.Objects;
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
        if (utils.waitForDataReady(timeoutMS, data) && Objects.nonNull(utils.getHeader())) {

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

    public double getVarHeaderDouble(CVar cVar) {

            VarHeader vh = utils.getVarHeaderEntry(0);
        // if (checkIdx(cVar)) {
        //
        //     if (vh != null) {
        //
        //     }
        //
        //
        // }

        return 0.0;

    }

    // private boolean checkIdx(CVar cVar) {
    //     if (isConnected()) {
    //         if (statusID != cVar.getStatusID()) {
    //             statusID = getStatusID();
    //             idx = getVarIdx(name);
    //         }
    //     }
    //
    // }

}
