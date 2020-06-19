package com.joffrey.iracingapp;

import com.joffrey.iracingapp.service.iracing.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IRacingService {

    private final Client client;

    private static final int TIMEOUT = 3000;
    private int lastSetupCount = -1;

    public void main() throws InterruptedException {
        log.info("Starting IRacing connector.");
        log.info("Initialization ...");

        if (init()) {

            log.info("Waiting game initialization...");
            while (true) {
                run();
            }

        } else {
            log.error("init failed.");
            System.exit(1);
        }

        log.error("Shutting down...");
        System.exit(0);
    }

    private boolean init() {

        lastSetupCount = -1;

        return true;
    }

    private void run() throws InterruptedException {

        // wait up to 16 ms for start of session or new data
        if (client.waitForData(TIMEOUT)) {

        }

        // your normal process loop would go here
        monitorConnectionStatus();

    }

    private void monitorConnectionStatus() {

        boolean isConnected = client.isConnected();
        boolean wasConnected = false;

        if (wasConnected != isConnected) {
            log.info("Welcome to iRacing !");
        } else {
            log.info("Lost connection to iRacing ...");
        }

        wasConnected = isConnected;

        lastSetupCount = -1;

    }

}
