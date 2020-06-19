package com.joffrey.iracingapp.service.iracing;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Client {

    // TODO: 18 Jun 2020 transform to SINGELTON
    private final Utils utils;

    private String data;
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

                statudID++;
                lastSessionCt = -1;

                if (utils.getNewData(data)) {
                    return true;
                }

            } else if (Objects.nonNull(data)) {
                // else we are already initialized, and data is ready for processing
                return true;
            }

        } else if (!isConnected()) {
            if (Objects.nonNull(data)) {
                data = null;
                lastSessionCt = -1;
            }
        }

        return false;

    }
}
