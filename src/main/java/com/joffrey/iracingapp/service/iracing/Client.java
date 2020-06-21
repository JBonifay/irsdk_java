package com.joffrey.iracingapp.service.iracing;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Client {

    private final Utils utils;

    private String data          = "";
    private int    nData;
    private int    statudID      = 0;
    private int    lastSessionCt = -1;

    public boolean isConnected() {
        return data != null && utils.isConnected();
    }

    public boolean waitForData(int timeoutMS) throws InterruptedException {

        // wait for start of session or new data
        if (utils.waitForDataReady(timeoutMS, data) && Objects.nonNull(utils.getHeader())) {

            // if new connection, or data changed lenght then init
            if (Objects.nonNull(data) || nData != utils.getHeader().getBufLen()) {

                // allocate memory to hold incoming data from sim
                if (Objects.nonNull(data)) {
                    data = null;
                }

                nData = utils.getHeader().getBufLen();
                // data = new String(nData);

                // indicate a new connection
                statudID++;
                // reset session info str status
                lastSessionCt = -1;

                // and try to fill in the data
                if (utils.getNewData(data)) {
                    return true;
                }

            } else if (!data.isEmpty()) {
                // else we are already initialized, and data is ready for processing
                return true;
            }

        } else if (!isConnected()) {
            if (!data.isEmpty()) {
                data = null;
                lastSessionCt = -1;
            }
        }
        return false;
    }
}
