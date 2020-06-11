package com.joffrey.iracingapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class IracingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(IracingAppApplication.class, args);

        new IracingAppApplication();
    }

    public IracingAppApplication() {
        log.info("Starting iracing application.");

        log.info("Waiting game initialization...");

        if (init()) {
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

    private void run() {

    }

    private boolean init() {
        log.info("Setup");

        return true;
    }


}
