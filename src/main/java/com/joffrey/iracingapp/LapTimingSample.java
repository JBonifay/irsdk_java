package com.joffrey.iracingapp;

import com.joffrey.iracingapp.service.iracing.Client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class LapTimingSample implements CommandLineRunner {

    private final Client client;

    private static       double   lastTime     = -1;
    private static final int      maxCars      = 64;
    private static final float[]  lastDistPct  = new float[maxCars];
    private static final double[] lapStartTime = new double[maxCars];
    // lap time for last lap, or -1 if not yet completed a lap
    private static final float[]  lapTime      = new float[maxCars];

    private static final List<DriverEntry> driverEntryList = new ArrayList<>(maxCars);
    private boolean wasConnected = false;

    public static void main(String[] args) {
        SpringApplication.run(LapTimingSample.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (init()) {
            while (true) {
                lapTiming();
            }
        } else {
            log.error("Init failed..");
        }
    }

    private boolean init() {
        // bump priority up so we get time from the sim
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        //****Note, put your init logic here

        return true;
    }

    private void lapTiming() throws InterruptedException, IOException {
        // wait up to 16 ms for start of session or new data
        if (client.waitForData(16)) {

            // and grab the data here
            log.info("oui");

            // only process session string if it changed
            if (client.wasSessionStrUpdated()) {

            }

            // update the display as well
            updateDisplay();
        }
        // else we did not grab data, do nothing

        // pump our connection status
        monitorConnectionStatus();

        //****Note, add your own additional loop processing here
        // for anything not dependant on telemetry data (keeping a UI running, etc)

    }

    private void monitorConnectionStatus() {
        // keep track of connection status

        boolean isConnected = client.isConnected();
        if (wasConnected != isConnected) {
            if (isConnected) {
                log.info("Connected to iRacing.");
                resetState(true);
            } else {
                log.info("Lost connection to iRacing");
            }

            //****Note, put your connection handling here

            wasConnected = isConnected;
        }

    }

    private void resetState(boolean isNewConnection) {
        if (isNewConnection) {
            Collections.fill(driverEntryList, new DriverEntry());
        }

        for (int i = 0; i < maxCars; i++) {
            lastTime = -1;
            lastDistPct[i] = -1;
            lapStartTime[i] = -1;
            lapTime[i] = -1;
        }
    }

    private void updateDisplay() throws IOException {
        System.out.println("\33[H\033[2J");
        System.out.flush();


    }


}
