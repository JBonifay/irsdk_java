package com.joffrey.iracingapp.service.iracing;

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
    private int        statudID      = 0;
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
                statudID++;
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
        return false;
    }
}
