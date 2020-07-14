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

package com.joffrey.iracingapp;

import com.joffrey.iracingapp.model.CVar;
import com.joffrey.iracingapp.model.DriverEntry;
import com.joffrey.iracingapp.model.defines.Flags;
import com.joffrey.iracingapp.model.defines.PaceMode;
import com.joffrey.iracingapp.model.defines.SessionState;
import com.joffrey.iracingapp.service.iracing.Client;
import com.joffrey.iracingapp.service.iracing.YamlParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class LapTiming implements CommandLineRunner {

    private final Client     client;
    private final YamlParser yamlParser;

    private static       double   lastTime     = -1;
    private static final int      MAX_CARS     = 64;
    private static final float[]  lastDistPct  = new float[MAX_CARS];
    private static final double[] lapStartTime = new double[MAX_CARS];
    // lap time for last lap, or -1 if not yet completed a lap
    private static final float[]  lapTime      = new float[MAX_CARS];

    private static       List<DriverEntry> driverTableTable = new ArrayList<>(MAX_CARS);
    private              boolean           wasConnected     = false;
    private static final boolean           UPDATE_DISPLAY   = true;

    private CVar airDensity;
    private CVar airPressure;
    private CVar airTemp;
    private CVar fogLevel;
    private CVar relativeHumidity;
    private CVar skies;
    private CVar trackTempCrew;
    private CVar weatherType;
    private CVar windDir;
    private CVar windVel;
    private CVar pitsOpen;
    private CVar raceLaps;
    private CVar sessionFlags;
    private CVar sessionLapsRemain;
    private CVar sessionLapsRemainEx;
    private CVar sessionNum;
    private CVar sessionState;
    private CVar sessionTick;
    private CVar sessionTime;
    private CVar sessionTimeOfDay;
    private CVar sessionTimeRemain;
    private CVar sessionUniqueID;
    private CVar carIdxEstTime;
    private CVar carIdxClassPosition;
    private CVar carIdxF2Time;
    private CVar carIdxGear;
    private CVar carIdxLap;
    private CVar carIdxLapCompleted;
    private CVar carIdxLapDistPct;
    private CVar carIdxOnPitRoad;
    private CVar carIdxPosition;
    private CVar carIdxRPM;
    private CVar carIdxSteer;
    private CVar carIdxTrackSurface;
    private CVar carIdxTrackSurfaceMaterial;
    private CVar carIdxLastLapTime;
    private CVar carIdxBestLapTime;
    private CVar carIdxBestLapNum;
    private CVar carIdxP2P_status;
    private CVar carIdxP2P_count;
    private CVar paceMode;
    private CVar carIdxPaceLine;
    private CVar carIdxPaceRow;
    private CVar carIdxPaceFlags;


    private void initVar() {

        // 'live' session info
        // Live weather info, may change as session progresses
        // (float) kg/m^3, Density of air at start/finish line
        airDensity = new CVar(client, "AirDensity");
        // (float) Hg, Pressure of air at start/finish line
        airPressure = new CVar(client, "AirPressure");
        // (float) C, Temperature of air at start/finish line
        airTemp = new CVar(client, "AirTemp");
        // (float) %, Fog level
        fogLevel = new CVar(client, "FogLevel");
        // (float) %, Relative Humidity
        relativeHumidity = new CVar(client, "RelativeHumidity");
        // (int) Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
        skies = new CVar(client, "Skies");
        // (float) C, Temperature of track measured by crew around track
        trackTempCrew = new CVar(client, "TrackTempCrew");
        // (int) Weather type (0=constant 1=dynamic)
        weatherType = new CVar(client, "WeatherType");
        // (float) rad, Wind direction at start/finish line
        windDir = new CVar(client, "WindDir");
        // (float) m/s, Wind velocity at start/finish line
        windVel = new CVar(client, "WindVel");
        // session status
        // (bool) True if pit stop is allowed, basically true if caution lights not out
        pitsOpen = new CVar(client, "PitsOpen");
        // (int) Laps completed in race
        raceLaps = new CVar(client, "RaceLaps");
        // (int) irsdk_Flags, bitfield
        sessionFlags = new CVar(client, "SessionFlags");
        // (int) Laps left till session ends
        sessionLapsRemain = new CVar(client, "SessionLapsRemain");
        // (int) New improved laps left till session ends
        sessionLapsRemainEx = new CVar(client, "SessionLapsRemainEx");
        // (int) Session number
        sessionNum = new CVar(client, "SessionNum");
        // (int) irsdk_SessionState, Session state
        sessionState = new CVar(client, "SessionState");
        // (int) Current update number
        sessionTick = new CVar(client, "SessionTick");
        // (double), s, Seconds since session start
        sessionTime = new CVar(client, "SessionTime");
        // (float) s, Time of day in seconds
        sessionTimeOfDay = new CVar(client, "SessionTimeOfDay");
        // (double) s, Seconds left till session ends
        sessionTimeRemain = new CVar(client, "SessionTimeRemain");
        // (int) Session ID
        sessionUniqueID = new CVar(client, "SessionUniqueID");
        // competitor information, array of up to 64 cars
        // (float) s, Estimated time to reach current location on track
        carIdxEstTime = new CVar(client, "CarIdxEstTime");
        // (int) Cars class position in race by car index
        carIdxClassPosition = new CVar(client, "CarIdxClassPosition");
        // (float) s, Race time behind leader or fastest lap time otherwise
        carIdxF2Time = new CVar(client, "CarIdxF2Time");
        // (int) -1=reverse 0=neutral 1..n=current gear by car index
        carIdxGear = new CVar(client, "CarIdxGear");
        // (int) Lap count by car index
        carIdxLap = new CVar(client, "CarIdxLap");
        // (int) Laps completed by car index
        carIdxLapCompleted = new CVar(client, "CarIdxLapCompleted");
        // (float) %, Percentage distance around lap by car index
        carIdxLapDistPct = new CVar(client, "CarIdxLapDistPct");
        // (bool) On pit road between the cones by car index
        carIdxOnPitRoad = new CVar(client, "CarIdxOnPitRoad");
        // (int) Cars position in race by car index
        carIdxPosition = new CVar(client, "CarIdxPosition");
        // (float) revs/min, Engine rpm by car index
        carIdxRPM = new CVar(client, "CarIdxRPM");
        // (float) rad, Steering wheel angle by car index
        carIdxSteer = new CVar(client, "CarIdxSteer");
        // (int) irsdk_TrkLoc, Track surface type by car index
        carIdxTrackSurface = new CVar(client, "CarIdxTrackSurface");
        // (int) irsdk_TrkSurf, Track surface material type by car index
        carIdxTrackSurfaceMaterial = new CVar(client, "CarIdxTrackSurfaceMaterial");
        // new variables
        // (float) s, Cars last lap time
        carIdxLastLapTime = new CVar(client, "CarIdxLastLapTime");
        // (float) s, Cars best lap time
        carIdxBestLapTime = new CVar(client, "CarIdxBestLapTime");
        // (int) Cars best lap number
        carIdxBestLapNum = new CVar(client, "CarIdxBestLapNum");
        // (bool) Push2Pass active or not
        carIdxP2P_status = new CVar(client, "CarIdxP2P_Status");
        // (int) Push2Pass count of usage (or remaining in Race)
        carIdxP2P_count = new CVar(client, "CarIdxP2P_Count");
        // (int) irsdk_PaceMode, Are we pacing or not
        paceMode = new CVar(client, "PaceMode");
        // (int) What line cars are pacing in, or -1 if not pacing
        carIdxPaceLine = new CVar(client, "CarIdxPaceLine");
        // (int) What row cars are pacing in, or -1 if not pacing
        carIdxPaceRow = new CVar(client, "CarIdxPaceRow");
        // (int) irsdk_PaceFlags, Pacing status flags for each car
        carIdxPaceFlags = new CVar(client, "CarIdxPaceFlags");
    }

    public static void main(String[] args) {
        SpringApplication.run(LapTiming.class, args);
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

        initVar();

        //****Note, put your init logic here

        return true;
    }

    private void lapTiming() throws InterruptedException {
        // wait up to 16 ms for start of session or new data
        if (client.waitForData(16)) {

            // and grab the data here
            processLapInfo();
            // processYAMLSessionString(generateLiveYAMLString());

            // only process session string if it changed
            if (client.wasSessionStrUpdated()) {
                processYAMLSessionString(client.getSessionStr());
            }

            // update the display as well
            if (UPDATE_DISPLAY) {
                updateDisplay();
            }
        }
        // else we did not grab data, do nothing

        // pump our connection status
        monitorConnectionStatus();

        //****Note, add your own additional loop processing here
        // for anything not dependant on telemetry data (keeping a UI running, etc)

    }

    private void processLapInfo() {
        // work out lap times for all cars

        double currentTime = sessionTime.getDouble();

        // if time moves backwards were in a new session!
        if (lastTime > currentTime) {
            resetState(false);
        }

        for (int i = 0; i < MAX_CARS; i++) {
            float curDistPct = carIdxLapDistPct.getFloat(i);
            // reject if the car blinked out of the world
            if (curDistPct != -1) {

                // did we cross the lap?
                if (lastDistPct[i] > 0.9f && curDistPct < 0.1f) {
                    // calculate exact time of lap crossing
                    double curLapStartTime = interpolateTimeAcressPoint(lastTime, currentTime, lastDistPct[i], curDistPct, 0);

                    // calculate lap time, if already crossed start/finish
                    if (lapStartTime[i] != -1) {
                        lapTime[i] = (float) (curLapStartTime - lapStartTime[i]);
                    }

                    // and store start/finish crossing time for next lap
                    lapStartTime[i] = curLapStartTime;
                }

                lastDistPct[i] = curDistPct;
            }

        }
        lastTime = currentTime;
    }

    /**
     * helper function to handle interpolation across a checkpoint
     *
     * @param p1    position and time before checkpoint
     * @param p2    position and time after checkpoint
     * @param t1    position and time before checkpoint
     * @param t2    position and time after checkpoint
     * @param check position of checkpoint
     */
    private double interpolateTimeAcressPoint(double t1, double t2, float p1, float p2, int check) {
        // unwrap if crossing start/finish line
        //****Note, assumes p1 is a percent from 0 to 1
        // if that is not true then unwrap the numbers before calling this function
        if (p1 > p2) {
            p1 -= 1;
        }

        // calculate where line is between points
        float pct = (check - p1) / (p2 - p1);

        return t1 + (t2 - t1) * pct;
    }

    private String generateLiveYAMLString() {

        //****Warning, shared static memory!
        final int yamlLen = 50000;
        final StringBuilder tstr = new StringBuilder(yamlLen);

        // Start of YAML file
        tstr.append("---\n");

        // Live weather info, may change as session progresses
        tstr.append("WeatherStatus:\n");
        tstr.append(" AirDensity: "
                    + airDensity.getFloat());                                       // kg/m^3, Density of air at start/finish line
        tstr.append(" AirPressure: "
                    + airPressure.getFloat());                                     // Hg, Pressure of air at start/finish line
        tstr.append(" AirTemp: "
                    + airTemp.getFloat());                                             // C, Temperature of air at start/finish line
        tstr.append(" FogLevel: " + fogLevel.getFloat());                                           // %, Fog level
        tstr.append(" RelativeHumidity: " + relativeHumidity.getFloat());                           // %, Relative Humidity
        tstr.append(" Skies: "
                    + skies.getInt());                                                   // Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
        tstr.append(" TrackTempCrew: "
                    + trackTempCrew.getFloat());                                 // C, Temperature of track measured by crew around track
        tstr.append(" WeatherType: "
                    + weatherType.getInt());                                       // Weather type (0=constant 1=dynamic)
        tstr.append(" WindDir: "
                    + windDir.getFloat());                                             // rad, Wind direction at start/finish line
        tstr.append(" WindVel: "
                    + windVel.getFloat());                                             // m/s, Wind velocity at start/finish line
        tstr.append("\n");

        // session status
        tstr.append("SessionStatus:\n");
        tstr.append(" PitsOpen: "
                    + pitsOpen.getBoolean()
                    + "\n");                                  // True if pit stop is allowed, basically true if caution lights not out
        tstr.append(" RaceLaps: " + raceLaps.getInt() + "\n");                                      // Laps completed in race
        tstr.append(" SessionFlags: " + sessionFlags.getInt() + "\n");                               // irsdk_Flags, bitfield
        tstr.append(" SessionLapsRemain: " + sessionLapsRemain.getInt() + "\n");                    // Laps left till session ends
        tstr.append(" SessionLapsRemainEx: "
                    + sessionLapsRemainEx.getInt()
                    + "\n");                // New improved laps left till session ends
        tstr.append(" SessionNum: " + sessionNum.getInt() + "\n");                                  // Session number
        tstr.append(" SessionState: "
                    + sessionState.getInt()
                    + "\n");                              // irsdk_SessionState, Session state
        tstr.append(" SessionTick: " + sessionTick.getInt() + "\n");                                // Current update number
        tstr.append(" SessionTime: "
                    + sessionTime.getDouble()
                    + "\n");                             // s, Seconds since session start
        tstr.append(" SessionTimeOfDay: " + sessionTimeOfDay.getFloat() + "\n");                    // s, Time of day in seconds
        tstr.append(" SessionTimeRemain: "
                    + sessionTimeRemain.getDouble()
                    + "\n");                 // s, Seconds left till session ends
        tstr.append(" SessionUniqueID: " + sessionUniqueID.getInt() + "\n");                        // Session ID
        tstr.append("\n");

        // competitor information, array of up to 64 cars
        tstr.append("CarStatus:\n");
        if (paceMode.isValid()) {
            // irsdk_PaceMode, Are we pacing or not
            tstr.append(" PaceMode: " + paceMode.getInt() + "\n");
        }

        tstr.append(" Cars:\n");
        for (int entry = 0; entry < MAX_CARS; entry++) {
            tstr.append(" - CarIdx: " + entry + "\n"); // for convenience, the index into the array is the carIdx
            tstr.append("   CarIdxEstTime: "
                        + carIdxEstTime.getFloat(entry)
                        + "\n"); // s, Estimated time to reach current location on track
            tstr.append("   CarIdxClassPosition: "
                        + carIdxClassPosition.getInt(entry)
                        + "\n"); // Cars class position in race by car index
            tstr.append("   CarIdxF2Time: "
                        + carIdxF2Time.getFloat(entry)
                        + "\n"); // s, Race time behind leader or fastest lap time otherwise
            tstr.append("   CarIdxGear: "
                        + carIdxGear.getInt(entry)
                        + "\n"); // -1=reverse 0=neutral 1..n=current gear by car index
            tstr.append("   CarIdxLap: " + carIdxLap.getInt(entry) + "\n"); // Lap count by car index
            tstr.append("   CarIdxLapCompleted: " + carIdxLapCompleted.getInt(entry) + "\n"); // Laps completed by car index
            tstr.append("   CarIdxLapDistPct: "
                        + carIdxLapDistPct.getFloat(entry)
                        + "\n"); // %, Percentage distance around lap by car index
            tstr.append("   CarIdxOnPitRoad: "
                        + carIdxOnPitRoad.getBoolean(entry)
                        + "\n"); // On pit road between the cones by car index
            tstr.append("   CarIdxPosition: " + carIdxPosition.getInt(entry) + "\n"); // Cars position in race by car index
            tstr.append("   CarIdxRPM: " + carIdxRPM.getFloat(entry) + "\n"); // revs/min, Engine rpm by car index
            tstr.append("   CarIdxSteer: " + carIdxSteer.getFloat(entry) + "\n"); // rad, Steering wheel angle by car index
            tstr.append("   CarIdxTrackSurface: "
                        + carIdxTrackSurface.getInt(entry)
                        + "\n"); // irsdk_TrkLoc, Track surface type by car index
            tstr.append("   CarIdxTrackSurfaceMaterial: "
                        + carIdxTrackSurfaceMaterial.getInt(entry)
                        + "\n"); // irsdk_TrkSurf, Track surface material type by car index
            //****Note, don't use this one any more, it is replaced by CarIdxLastLapTime
            tstr.append("   CarIdxLapTime: " + lapTime[entry] + "\n"); // s, last lap time or -1 if not yet crossed s/f
            // new variables, check if they exist on members
            if (carIdxLastLapTime.isValid()) {
                tstr.append("   CarIdxLastLapTime: " + carIdxLastLapTime.getFloat(entry) + "\n"); // s, Cars last lap time
            }
            if (carIdxBestLapTime.isValid()) {
                tstr.append("   CarIdxBestLapTime: " + carIdxBestLapTime.getFloat(entry) + "\n"); // s, Cars best lap time
            }
            if (carIdxBestLapNum.isValid()) {
                tstr.append("   CarIdxBestLapNum: " + carIdxBestLapNum.getInt(entry) + "\n"); // Cars best lap number}
            }
            if (this.carIdxP2P_status.isValid()) {
                tstr.append("   CarIdxP2P_Status: " + this.carIdxP2P_status.getBoolean(entry) + "\n"); // Push2Pass active or not}
            }

            if (carIdxP2P_count.isValid()) {
                tstr.append("   CarIdxP2P_Count: "
                            + carIdxP2P_count.getInt(entry)
                            + "\n"); // Push2Pass count of usage (or remaining in Race)
            }
            if (carIdxPaceLine.isValid()) {
                tstr.append("   CarIdxPaceLine: "
                            + carIdxPaceLine.getInt(entry)
                            + "\n"); // What line cars are pacing in, or -1 if not pacing}
            }
            if (carIdxPaceRow.isValid()) {
                tstr.append("   CarIdxPaceRow: "
                            + carIdxPaceRow.getInt(entry)
                            + "\n"); // What row cars are pacing in, or -1 if not pacing}
            }
            if (carIdxPaceFlags.isValid()) {
                tstr.append("   CarIdxPaceFlags: "
                            + carIdxPaceFlags.getInt(entry)
                            + "\n"); // irsdk_PaceFlags, Pacing status flags for each car}
            }

        }

        tstr.append("\n");

        // End of YAML file
        tstr.append("...\n");

        // terminate string in case we blew off the end of the array.
        tstr.append('\0');

        // make sure we are not close to running out of room
        // if this triggers then double m_len
        // assert (len < (m_len - 256));

        return tstr.toString();
    }

    private void processYAMLSessionString(String yamlString) {

        long lastTime = 0;

        // validate string
        if (!yamlString.isEmpty() && yamlString.charAt(0) != ' ') {
            //****Note, your code goes here
            // can write to disk, parse, etc

            // output file once every 5 seconds
            long minTime = (long) 5.0f * 1000;
            long curTime = System.currentTimeMillis();

            if (Math.abs((curTime - lastTime)) > minTime) {

                lastTime = curTime;

                File file = new File("liveStr.txt");
                try {
                    file.createNewFile();
                    if (file.exists()) {
                        FileWriter fileWriter = new FileWriter(file);
                        PrintWriter printWriter = new PrintWriter(fileWriter);
                        printWriter.print(yamlString);
                        printWriter.flush();
                        printWriter.close();

                        file = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //---

            // Pull some driver info into a local array
            String tstr;
            for (int i = 0; i < MAX_CARS; i++) {

                DriverEntry newDriverEntry = new DriverEntry();

                // skip the rest if carIdx not found
                Pattern p = Pattern.compile("CarIdx: " + i + "\n");   // the pattern to search for
                Matcher m = p.matcher(yamlString);
                if (m.find()) {

                    tstr = String.format("DriverInfo:Drivers:CarIdx:{%d}", i);
                    newDriverEntry.setCarIdx(parceYamlInt(yamlString, tstr));

                    tstr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarClassID:", i);
                    newDriverEntry.setCarClassId(parceYamlInt(yamlString, tstr));

                    tstr = String.format("DriverInfo:Drivers:CarIdx:{%d}UserName:", i);
                    newDriverEntry.setDriverName(parceYamlStr(yamlString, tstr));

                    tstr = String.format("DriverInfo:Drivers:CarIdx:{%d}TeamName:", i);
                    newDriverEntry.setTeamName(parceYamlStr(yamlString, tstr));

                    tstr = String.format("DriverInfo:Drivers:CarIdx:{%d}CarNumber:", i);
                    newDriverEntry.setCarNumStr(parceYamlStr(yamlString, tstr));

                    // TeamID

                }

                driverTableTable.add(newDriverEntry);
            }
        }

        //---

        //****Note, your code goes here
        // can write to disk, parse, etc

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

    private int parceYamlInt(String yamlStr, String path) {
        if (!(yamlStr.isEmpty() && !path.isEmpty())) {
            int count = path.length();
            String result = yamlParser.parseYaml(yamlStr, path, count);
            if (!result.isEmpty()) {
                return Integer.parseInt(result);
            } else {
                return 0;
            }
        }
        return 0;
    }

    private String parceYamlStr(String yamlString, String path) {
        int count = path.length();
        return yamlParser.parseYaml(yamlString, path, count);
    }

    private void resetState(boolean isNewConnection) {
        if (isNewConnection) {
            Collections.fill(driverTableTable, new DriverEntry());
        }

        for (int i = 0; i < MAX_CARS; i++) {
            lastTime = -1;
            lastDistPct[i] = -1;
            lapStartTime[i] = -1;
            lapTime[i] = -1;
        }
    }

    public void printTime(double time_s) {
        int minutes = (int) (time_s / 60);
        float seconds = (float) (time_s - (60 * minutes));
        log.info(String.format("%03d:%05.2f", minutes, seconds));
    }

    private void printFlags(int flags) {

        // global flags
        if ((flags & Flags.irsdk_checkered.getValue()) != 0) {
            log.info("checkered ");
        }
        if ((flags & Flags.irsdk_white.getValue()) != 0) {
            log.info("white ");
        }
        if ((flags & Flags.irsdk_green.getValue()) != 0) {
            log.info("green ");
        }
        if ((flags & Flags.irsdk_yellow.getValue()) != 0) {
            log.info("yellow ");
        }
        if ((flags & Flags.irsdk_red.getValue()) != 0) {
            log.info("red ");
        }
        if ((flags & Flags.irsdk_blue.getValue()) != 0) {
            log.info("blue ");
        }
        if ((flags & Flags.irsdk_debris.getValue()) != 0) {
            log.info("debris ");
        }
        if ((flags & Flags.irsdk_crossed.getValue()) != 0) {
            log.info("crossed ");
        }
        if ((flags & Flags.irsdk_yellowWaving.getValue()) != 0) {
            log.info("yellowWaving ");
        }
        if ((flags & Flags.irsdk_oneLapToGreen.getValue()) != 0) {
            log.info("oneLapToGreen ");
        }
        if ((flags & Flags.irsdk_greenHeld.getValue()) != 0) {
            log.info("greenHeld ");
        }
        if ((flags & Flags.irsdk_tenToGo.getValue()) != 0) {
            log.info("tenToGo ");
        }
        if ((flags & Flags.irsdk_fiveToGo.getValue()) != 0) {
            log.info("fiveToGo ");
        }
        if ((flags & Flags.irsdk_randomWaving.getValue()) != 0) {
            log.info("randomWaving ");
        }
        if ((flags & Flags.irsdk_caution.getValue()) != 0) {
            log.info("caution ");
        }
        if ((flags & Flags.irsdk_cautionWaving.getValue()) != 0) {
            log.info("cautionWaving ");
        }

        // drivers black flags
        if ((flags & Flags.irsdk_black.getValue()) != 0) {
            log.info("black ");
        }
        if ((flags & Flags.irsdk_disqualify.getValue()) != 0) {
            log.info("disqualify ");
        }
        if ((flags & Flags.irsdk_servicible.getValue()) != 0) {
            log.info("servicible ");
        }
        if ((flags & Flags.irsdk_furled.getValue()) != 0) {
            log.info("furled ");
        }
        if ((flags & Flags.irsdk_repair.getValue()) != 0) {
            log.info("repair ");
        }

        // start lights
        if ((flags & Flags.irsdk_startHidden.getValue()) != 0) {
            log.info("startHidden ");
        }
        if ((flags & Flags.irsdk_startReady.getValue()) != 0) {
            log.info("startReady ");
        }
        if ((flags & Flags.irsdk_startSet.getValue()) != 0) {
            log.info("startSet ");
        }
        if ((flags & Flags.irsdk_startGo.getValue()) != 0) {
            log.info("startGo ");
        }
    }

    private void printSessionState(int state) {
        switch (SessionState.get(state)) {
            case irsdk_StateInvalid -> log.info("Invalid");
            case irsdk_StateGetInCar -> log.info("GetInCar");
            case irsdk_StateWarmup -> log.info("Warmup");
            case irsdk_StateParadeLaps -> log.info("ParadeLap");
            case irsdk_StateRacing -> log.info("Racing");
            case irsdk_StateCheckered -> log.info("Checkered");
            case irsdk_StateCoolDown -> log.info("CoolDown");
            default -> log.error(("Unexpected value: " + SessionState.get(state)));
        }
    }

    private void printPaceMode(int mode) {
        switch (PaceMode.get(mode)) {
            case irsdk_PaceModeSingleFileStart -> log.info("SingleFileStart");
            case irsdk_PaceModeDoubleFileStart -> log.info("DoubleFileStart");
            case irsdk_PaceModeSingleFileRestart -> log.info("SingleFileRestart");
            case irsdk_PaceModeDoubleFileRestart -> log.info("DoubleFileRestart:");
            case irsdk_PaceModeNotPacing -> log.info("NotPacing");
            default -> log.error("Unexpected value: " + PaceMode.get(mode));
        }
    }

    private void updateDisplay() {
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // int height = (int) screenSize.getHeight();
        // int width = (int) screenSize.getWidth();

        int height = 1000;
        int width = 100;

        int statusOffset = 3;
        int carsOffset = 6;
        int maxCarLines = height - carsOffset;

        // print race status line
        System.out.println("\33[H\033[2J");
        System.out.flush();

        log.info("Time: ");
        printTime(sessionTime.getDouble());

        log.info(String.format(" Session: %d", sessionNum.getInt()));

        log.info(String.format(" LapsComplete: %03d", raceLaps.getInt()));

        if (sessionLapsRemainEx.getInt() < 32767) {
            log.info(String.format(" LapsRemain: %03d", sessionLapsRemainEx.getInt()));
        } else {
            log.info(" LapsRemain: Unlimited");
        }

        log.info(" TimeRemain: ");
        if (sessionTimeRemain.getDouble() < 604800.0f) {
            printTime(sessionTimeRemain.getDouble());
        } else {
            log.info("Unlimited");
        }

        // print flag status
        log.info(" flags: ");
        printFlags(sessionFlags.getInt());

        log.info(String.format(" PitsOpen: %s", pitsOpen.getBoolean()));

        log.info(" State: ");
        printSessionState(sessionState.getInt());

        // new variables check if on members
        if (paceMode.isValid()) {
            log.info(" PaceMode: ");
            printPaceMode(paceMode.getInt());
        }

        // print car info
        // don't scroll off the end of the buffer
        int linesUsed = 0;
        int maxLines = Math.min(MAX_CARS, maxCarLines);
        for (int i = 0; i < MAX_CARS; i++) {
            if (linesUsed < maxLines) {
                // is the car in the world, or did we at least collect data on it when it was?
                if (carIdxTrackSurface.getInt() != -1 || carIdxLap.getInt(i) != -1 || carIdxPosition.getInt(i) != 0) {
                    log.info(String.format(
                            " %2d %3s %7.3f %2d %2d %2d %6.3f %s %8.2f %5.2f %2d %2d %2d %2d %7.3f %7.3f %7.3f %7.3f %2d %s %2d %2d %2d 0x%02x %n",
                            i,
                            driverTableTable.get(i).getCarNumStr(),
                            carIdxEstTime.getFloat(i),
                            carIdxGear.getInt(i),
                            carIdxLap.getInt(i),
                            carIdxLapCompleted.getInt(i),
                            carIdxLapDistPct.getFloat(i),
                            carIdxOnPitRoad.getBoolean(i),
                            carIdxRPM.getFloat(i),
                            carIdxSteer.getFloat(i),
                            carIdxTrackSurface.getInt(i),
                            carIdxTrackSurfaceMaterial.getInt(i),
                            carIdxTrackSurfaceMaterial.getInt(i),
                            carIdxClassPosition.getInt(i),
                            carIdxF2Time.getFloat(i),
                            //****Note, don't use this one any more, it is replaced by CarIdxLastLapTime
                            lapTime[i],
                            // new variables, check if they exist on members
                            (carIdxLastLapTime.isValid()) ? carIdxLastLapTime.getFloat(i) : -1,
                            (carIdxBestLapTime.isValid()) ? carIdxBestLapTime.getFloat(i) : -1,
                            (carIdxBestLapNum.isValid()) ? carIdxBestLapNum.getInt(i) : -1,
                            (carIdxP2P_status.isValid()) ? carIdxP2P_status.getBoolean(i) : -1,
                            (carIdxP2P_count.isValid()) ? carIdxP2P_count.getInt(i) : -1,
                            (carIdxPaceLine.isValid()) ? carIdxPaceLine.getInt(i) : -1,
                            (carIdxPaceRow.isValid()) ? carIdxPaceRow.getInt(i) : -1,
                            (carIdxPaceFlags.isValid()) ? carIdxPaceFlags.getInt(i) : -1));
                    linesUsed++;
                }
            }
        }

        // clear remaining lines
        for (int i = linesUsed; i < maxLines; i++) {
            log.info(" ");
        }
    }

}
