package com.joffrey.iracingapp;

import com.joffrey.iracingapp.service.iracing.Client;
import com.sun.tools.javac.Main;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class IracingAppApplication implements CommandLineRunner {

    private final Client client;

    public static void main(String[] args) {
        SpringApplication.run(IracingAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {

            startSdk();

        }


    }

    private void startSdk() throws InterruptedException {

        if (client.waitForData(16)) {

            log.info("oui");


        }


    }
}
