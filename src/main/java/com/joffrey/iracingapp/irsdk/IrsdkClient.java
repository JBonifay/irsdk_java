package com.joffrey.iracingapp.irsdk;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IrsdkClient {

    private final IrsdkUtils irsdkUtils;

    private StringBuilder data = new StringBuilder();
    private int           nData;
    private int           statudID      = 0;
    private int           lastSessionCt = -1;


    public boolean isConnected() {
        return data != null && irsdkUtils.isConnected();
    }


    public boolean waitForData(int timeoutMS) throws InterruptedException {

        // wait for start of session or new data
        if (irsdkUtils.waitForDataReady(timeoutMS, data.toString()) && Objects.nonNull(irsdkUtils.getHeader())) {

            // if new connection, or data changed lenght then init
            if (Objects.nonNull(data) || nData != irsdkUtils.getHeader().getBufLen()) {

                // allocate memory to hold incoming data from sim
                if (Objects.nonNull(data)) {
                    data = null;
                }

                nData = irsdkUtils.getHeader().getBufLen();
                data = new StringBuilder(nData);

                statudID++;
                lastSessionCt = -1;

                if (irsdkUtils.getNewData(data.toString())) {
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
