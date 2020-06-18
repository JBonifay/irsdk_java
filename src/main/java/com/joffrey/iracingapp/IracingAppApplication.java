package com.joffrey.iracingapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class IracingAppApplication implements CommandLineRunner {

    private final IRacingService iRacingService;

    public static void main(String[] args) {
        SpringApplication.run(IracingAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        iRacingService.main();
    }
}
