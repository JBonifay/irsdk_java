package com.joffrey.iracingapp;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class IracingAppApplication {

    private final IRacingService iRacingService;

    public static void main(String[] args) {
        SpringApplication.run(IracingAppApplication.class, args);

    }

    @PostConstruct
    public void startIracingService() throws InterruptedException {
        new Thread(() -> {
            try {
                iRacingService.startup();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // Kernel32.INSTANCE.OpenFileMapping(WinNT.FILE_ALL_ACCESS, false, IrsdkDefines.IRSDK_MEMMAPFILENAME);
    }

}
