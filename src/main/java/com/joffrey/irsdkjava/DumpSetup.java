/*
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.joffrey.irsdkjava;

import com.joffrey.irsdkjava.service.iracing.Client;
import com.joffrey.irsdkjava.service.iracing.DiskClient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class DumpSetup implements CommandLineRunner {

    private final Client     client;
    private final DiskClient diskClient;

    private boolean wasConnected = false;

    //------------------------
    // process live setup info
    private static int lastSetupCount = -1;

    public static void main(String[] args) {
        SpringApplication.run(DumpSetup.class, args);
    }

    //-----------------------
    // entry point
    @Override
    public void run(String... args) throws Exception {
        if (init()) {
            while (true) {
                dumpSetup();
            }
        } else {
            log.error("Init failed..");
        }
    }

    //------------------------
    // process live setup info
    public boolean init() {
        // bump priority up so we get time from the sim
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        lastSetupCount = -1;

        return true;
    }

    private void monitorConnectionStatus() {
        // keep track of connection status

        boolean isConnected = client.isConnected();
        if (wasConnected != isConnected) {
            if (isConnected) {
                log.info("Connected to iRacing.");
            } else {
                log.info("Lost connection to iRacing");
            }

            //****Note, put your connection handling here

            wasConnected = isConnected;
        }

    }

    private void dumpSetup() throws InterruptedException {
        // wait up to 16 ms for start of session or new data
        if (client.waitForData(16)) {

            // and grab the data
            final int MAX_STR = 1024;
            List<String> tstr = new ArrayList<>();
            if (1 == client.getSessionStrVal("CarSetup:UpdateCount:", tstr, MAX_STR)) {
                int count = Integer.parseInt(tstr.get(0));
                if (lastSetupCount != count) {
                    lastSetupCount = count;

                    // get info on the car path
                    if (1 == client.getSessionStrVal("DriverInfo:DriverCarIdx:", tstr, MAX_STR)) {
                        int driverCarIdx = Integer.parseInt(tstr.get(1));
                        String pathStr = "";

                        pathStr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarScreenName:", driverCarIdx);
                        if (1 == client.getSessionStrVal(pathStr, tstr, MAX_STR)) {
                            log.info(String.format("CarName: %s%n", tstr.get(2)));
                        }

                        pathStr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarPath:", driverCarIdx);
                        if (1 == client.getSessionStrVal(pathStr, tstr, MAX_STR)) {
                            log.info(String.format("CarPath: %s%n", tstr.get(3)));
                        }
                    }

                    // get info on what setup is loaded
                    if (1 == client.getSessionStrVal("DriverInfo:DriverSetupName:", tstr, MAX_STR)) {
                        log.info(String.format("CarSetupName: %s%n", tstr.get(4)));

                        if (1 == client.getSessionStrVal("DriverInfo:DriverSetupLoadTypeName:", tstr, MAX_STR)) {
                            log.info(String.format(", %s%n", tstr.get(5)));

                        }

                        if (1 == client.getSessionStrVal("DriverInfo:DriverSetupIsModified:", tstr, MAX_STR)) {
                            if (Integer.parseInt(tstr.get(6)) == 1) {
                                log.info(", modified");
                            }
                        }

                        if (1 == client.getSessionStrVal("DriverInfo:DriverSetupPassedTech:", tstr, MAX_STR)) {
                            if (Integer.parseInt(tstr.get(7)) == 0) {
                                log.info(", failed tech");
                            }
                        }

                        log.info("\n");
                    }

                    // get the current setup and dump it
                    String sesStr = client.getSessionStr();
                    if (!sesStr.isEmpty()) {
                        //****Note, this assumes CarSetup is the last section of the session string.
                        String res = sesStr.substring(strStr(sesStr, "CarSetup:"));
                        if (!res.isEmpty()) {
                            log.info(res);
                            log.info("\n");
                        }
                    }
                }
            }
        }

        // your normal process loop would go here
        monitorConnectionStatus();
    }

    public int strStr(String haystack, String needle) {
        if (haystack == null || needle == null) {
            return 0;
        }

        if (needle.length() == 0) {
            return 0;
        }

        for (int i = 0; i < haystack.length(); i++) {
            if (i + needle.length() > haystack.length()) {
                return -1;
            }

            int m = i;
            for (int j = 0; j < needle.length(); j++) {
                if (needle.charAt(j) == haystack.charAt(m)) {
                    if (j == needle.length() - 1) {
                        return i;
                    }
                    m++;
                } else {
                    break;
                }

            }
        }

        return -1;
    }

    //------------------------
    // process ibt files
    // TODO: 10 Jul 2020 TEST this method
    public void processFile(String path) {
        final int MAX_STR = 1024;
        String tstr = "";

        File file = new File(path);

        log.info("\n\nFileName: %s\n", file);

        if (diskClient.openFile(path)) {
            // get info on the car path
            if (1 == diskClient.getSessionStrVal("DriverInfo:DriverCarIdx:", tstr, MAX_STR)) {
                int driverCarIdx = tstr.charAt(0);
                String pathStr = "";

                pathStr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarScreenName:", driverCarIdx);
                if (1 == diskClient.getSessionStrVal(pathStr, tstr, MAX_STR)) {
                    log.info("CarName: %s\n", tstr);
                }

                pathStr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarPath:", driverCarIdx);
                if (1 == diskClient.getSessionStrVal(pathStr, tstr, MAX_STR)) {
                    log.info("CarPath: %s\n", tstr);
                }
            }

            // get info on what setup is loaded
            if (1 == diskClient.getSessionStrVal("DriverInfo:DriverSetupName:", tstr, MAX_STR)) {
                log.info("CarSetupName: %s", tstr);

                if (1 == diskClient.getSessionStrVal("DriverInfo:DriverSetupLoadTypeName:", tstr, MAX_STR)) {
                    log.info(", %s", tstr);
                }

                if (1 == diskClient.getSessionStrVal("DriverInfo:DriverSetupIsModified:", tstr, MAX_STR)) {
                    if (tstr.charAt(0) == 1) {
                        log.info(", modified");
                    }
                }

                if (1 == diskClient.getSessionStrVal("DriverInfo:DriverSetupPassedTech:", tstr, MAX_STR)) {
                    if (tstr.charAt(0) == 0) {
                        log.info(", failed tech");
                    }
                }

                log.info("\n");
            }

            // get the current setup and dump it
            String sesStr = diskClient.getSessionStr();
            if (!sesStr.isEmpty()) {
                //****Note, this assumes CarSetup is the last section of the session string.
                String res = sesStr.substring(strStr(sesStr, "CarSetup:"));
                if (!res.isEmpty()) {
                    log.info(res);
                    log.info("\n");
                }
            }

            diskClient.closeFile();
        } else {
            log.info("failed!\n");
        }
    }
}

