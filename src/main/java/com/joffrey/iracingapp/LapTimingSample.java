package com.joffrey.iracingapp;

import com.joffrey.iracingapp.model.iracing.defines.Flags;
import com.joffrey.iracingapp.model.iracing.defines.PaceMode;
import com.joffrey.iracingapp.model.iracing.defines.SessionState;
import com.joffrey.iracingapp.service.iracing.Client;
import com.joffrey.iracingapp.service.iracing.YamlParser;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    private final Client     client;
    private final YamlParser yamlParser;

    private static       double   lastTime     = -1;
    private static final int      maxCars      = 64;
    private static final float[]  lastDistPct  = new float[maxCars];
    private static final double[] lapStartTime = new double[maxCars];
    // lap time for last lap, or -1 if not yet completed a lap
    private static final float[]  lapTime      = new float[maxCars];

    private static final List<DriverEntry> driverTableTable = new ArrayList<>(maxCars);
    private              boolean           wasConnected     = false;
    private static final boolean           updateDisplay    = true;


    // 'live' session info
    // Live weather info, may change as session progresses
    CVar airDensity                 = new CVar("AirDensity"); // (float) kg/m^3, Density of air at start/finish line
    CVar airPressure                = new CVar("AirPressure"); // (float) Hg, Pressure of air at start/finish line
    CVar airTemp                    = new CVar("AirTemp"); // (float) C, Temperature of air at start/finish line
    CVar fogLevel                   = new CVar("FogLevel"); // (float) %, Fog level
    CVar relativeHumidity           = new CVar("RelativeHumidity"); // (float) %, Relative Humidity
    CVar skies                      = new CVar("Skies"); // (int) Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
    CVar trackTempCrew              = new CVar("TrackTempCrew"); // (float) C, Temperature of track measured by crew around track
    CVar weatherType                = new CVar("WeatherType"); // (int) Weather type (0=constant 1=dynamic)
    CVar windDir                    = new CVar("WindDir"); // (float) rad, Wind direction at start/finish line
    CVar windVel                    = new CVar("WindVel"); // (float) m/s, Wind velocity at start/finish line
    // session status
    CVar pitsOpen                   = new CVar(
            "PitsOpen"); // (bool) True if pit stop is allowed, basically true if caution lights not out
    CVar raceLaps                   = new CVar("RaceLaps"); // (int) Laps completed in race
    CVar sessionFlags               = new CVar("SessionFlags"); // (int) irsdk_Flags, bitfield
    CVar sessionLapsRemain          = new CVar("SessionLapsRemain"); // (int) Laps left till session ends
    CVar sessionLapsRemainEx        = new CVar("SessionLapsRemainEx"); // (int) New improved laps left till session ends
    CVar sessionNum                 = new CVar("SessionNum"); // (int) Session number
    CVar sessionState               = new CVar("SessionState"); // (int) irsdk_SessionState, Session state
    CVar sessionTick                = new CVar("SessionTick"); // (int) Current update number
    CVar sessionTime                = new CVar("SessionTime"); // (double), s, Seconds since session start
    CVar sessionTimeOfDay           = new CVar("SessionTimeOfDay"); // (float) s, Time of day in seconds
    CVar sessionTimeRemain          = new CVar("SessionTimeRemain"); // (double) s, Seconds left till session ends
    CVar sessionUniqueID            = new CVar("SessionUniqueID"); // (int) Session ID
    // competitor information, array of up to 64 cars
    CVar carIdxEstTime              = new CVar("CarIdxEstTime"); // (float) s, Estimated time to reach current location on track
    CVar carIdxClassPosition        = new CVar("CarIdxClassPosition"); // (int) Cars class position in race by car index
    CVar carIdxF2Time               = new CVar(
            "CarIdxF2Time"); // (float) s, Race time behind leader or fastest lap time otherwise
    CVar carIdxGear                 = new CVar("CarIdxGear"); // (int) -1=reverse 0=neutral 1..n=current gear by car index
    CVar carIdxLap                  = new CVar("CarIdxLap"); // (int) Lap count by car index
    CVar carIdxLapCompleted         = new CVar("CarIdxLapCompleted"); // (int) Laps completed by car index
    CVar carIdxLapDistPct           = new CVar("CarIdxLapDistPct"); // (float) %, Percentage distance around lap by car index
    CVar carIdxOnPitRoad            = new CVar("CarIdxOnPitRoad"); // (bool) On pit road between the cones by car index
    CVar carIdxPosition             = new CVar("CarIdxPosition"); // (int) Cars position in race by car index
    CVar carIdxRPM                  = new CVar("CarIdxRPM"); // (float) revs/min, Engine rpm by car index
    CVar carIdxSteer                = new CVar("CarIdxSteer"); // (float) rad, Steering wheel angle by car index
    CVar carIdxTrackSurface         = new CVar("CarIdxTrackSurface"); // (int) irsdk_TrkLoc, Track surface type by car index
    CVar carIdxTrackSurfaceMaterial = new CVar(
            "CarIdxTrackSurfaceMaterial"); // (int) irsdk_TrkSurf, Track surface material type by car index
    // new variables
    CVar carIdxLastLapTime          = new CVar("CarIdxLastLapTime"); // (float) s, Cars last lap time
    CVar carIdxBestLapTime          = new CVar("CarIdxBestLapTime"); // (float) s, Cars best lap time
    CVar carIdxBestLapNum           = new CVar("CarIdxBestLapNum"); // (int) Cars best lap number
    CVar carIdxP2P_Status           = new CVar("CarIdxP2P_Status"); // (bool) Push2Pass active or not
    CVar carIdxP2P_Count            = new CVar("CarIdxP2P_Count"); // (int) Push2Pass count of usage (or remaining in Race)
    CVar paceMode                   = new CVar("PaceMode"); // (int) irsdk_PaceMode, Are we pacing or not
    CVar carIdxPaceLine             = new CVar("CarIdxPaceLine"); // (int) What line cars are pacing in, or -1 if not pacing
    CVar carIdxPaceRow              = new CVar("CarIdxPaceRow"); // (int) What row cars are pacing in, or -1 if not pacing
    CVar carIdxPaceFlags            = new CVar("CarIdxPaceFlags"); // (int) irsdk_PaceFlags, Pacing status flags for each car


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

    private void lapTiming() throws InterruptedException {
        // wait up to 16 ms for start of session or new data
        if (client.waitForData(16)) {

            // and grab the data here
            processLapInfo();
            processYAMLSessionString(generateLiveYAMLString());

            // only process session string if it changed
            if (client.wasSessionStrUpdated()) {
                processYAMLSessionString(client.getSessionStr());
            }

            // update the display as well
            if (updateDisplay) {
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

        for (int i = 0; i < maxCars; i++) {
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
        tstr.append(" AirDensity: " + airDensity.getFloat());                                       // kg/m^3, Density of air at start/finish line
        tstr.append(" AirPressure: " + airPressure.getFloat());                                     // Hg, Pressure of air at start/finish line
        tstr.append(" AirTemp: " + airTemp.getFloat());                                             // C, Temperature of air at start/finish line
        tstr.append(" FogLevel: " + fogLevel.getFloat());                                           // %, Fog level
        tstr.append(" RelativeHumidity: " + relativeHumidity.getFloat());                           // %, Relative Humidity
        tstr.append(" Skies: " + skies.getInt());                                                   // Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
        tstr.append(" TrackTempCrew: " + trackTempCrew.getFloat());                                 // C, Temperature of track measured by crew around track
        tstr.append(" WeatherType: " + weatherType.getInt());                                       // Weather type (0=constant 1=dynamic)
        tstr.append(" WindDir: " + windDir.getFloat());                                             // rad, Wind direction at start/finish line
        tstr.append(" WindVel: " + windVel.getFloat());                                             // m/s, Wind velocity at start/finish line
        tstr.append("\n");

        // session status
        tstr.append("SessionStatus:\n");
        tstr.append(" PitsOpen: " + pitsOpen.getBoolean() + "\n");                                  // True if pit stop is allowed, basically true if caution lights not out
        tstr.append(" RaceLaps: " + raceLaps.getInt() + "\n");                                      // Laps completed in race
        tstr.append(" SessionFlags: " + sessionTime.getInt() + "\n");                               // irsdk_Flags, bitfield
        tstr.append(" SessionLapsRemain: " + sessionLapsRemain.getInt() + "\n");                    // Laps left till session ends
        tstr.append(" SessionLapsRemainEx: " + sessionLapsRemainEx.getInt() + "\n");                // New improved laps left till session ends
        tstr.append(" SessionNum: " + sessionNum.getInt() + "\n");                                  // Session number
        tstr.append(" SessionState: " + sessionState.getInt() + "\n");                              // irsdk_SessionState, Session state
        tstr.append(" SessionTick: " + sessionTick.getInt() + "\n");                                // Current update number
        tstr.append(" SessionTime: " + sessionTime.getDouble() + "\n");                             // s, Seconds since session start
        tstr.append(" SessionTimeOfDay: " + sessionTimeOfDay.getFloat() + "\n");                    // s, Time of day in seconds
        tstr.append(" SessionTimeRemain: " + sessionTimeRemain.getDouble() + "\n");                 // s, Seconds left till session ends
        tstr.append(" SessionUniqueID: " + sessionUniqueID.getInt() + "\n");                        // Session ID
        tstr.append("\n");

        // competitor information, array of up to 64 cars
        tstr.append("CarStatus:\n");
        if (paceMode.isValid()) {
            // irsdk_PaceMode, Are we pacing or not
            tstr.append(" PaceMode: " + paceMode.getInt() + "\n");
        }

        tstr.append(" Cars:\n");
        for (int entry = 0; entry < maxCars; entry++) {
            tstr.append(" - CarIdx: " + entry + "\n"); // for convenience, the index into the array is the carIdx
            tstr.append("   CarIdxEstTime: " + carIdxEstTime.getFloat(entry) + "\n"); // s, Estimated time to reach current location on track
            tstr.append("   CarIdxClassPosition: " + carIdxClassPosition.getInt(entry) + "\n"); // Cars class position in race by car index
            tstr.append("   CarIdxF2Time: " + carIdxF2Time.getFloat(entry) + "\n"); // s, Race time behind leader or fastest lap time otherwise
            tstr.append("   CarIdxGear: " + carIdxGear.getInt(entry) + "\n"); // -1=reverse 0=neutral 1..n=current gear by car index
            tstr.append("   CarIdxLap: " + carIdxLap.getInt(entry) + "\n"); // Lap count by car index
            tstr.append("   CarIdxLapCompleted: " + carIdxLapCompleted.getInt(entry) + "\n"); // Laps completed by car index
            tstr.append("   CarIdxLapDistPct: " + carIdxLapDistPct.getFloat(entry) + "\n"); // %, Percentage distance around lap by car index
            tstr.append("   CarIdxOnPitRoad: " + carIdxOnPitRoad.getBoolean(entry) + "\n"); // On pit road between the cones by car index
            tstr.append("   CarIdxPosition: " + carIdxPosition.getInt(entry) + "\n"); // Cars position in race by car index
            tstr.append("   CarIdxRPM: " + carIdxRPM.getFloat(entry) + "\n"); // revs/min, Engine rpm by car index
            tstr.append("   CarIdxSteer: " + carIdxSteer.getFloat(entry) + "\n"); // rad, Steering wheel angle by car index
            tstr.append("   CarIdxTrackSurface: " + carIdxTrackSurface.getInt(entry) + "\n"); // irsdk_TrkLoc, Track surface type by car index
            tstr.append("   CarIdxTrackSurfaceMaterial: " + carIdxTrackSurfaceMaterial.getInt(entry) + "\n"); // irsdk_TrkSurf, Track surface material type by car index
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
            if (carIdxP2P_Status.isValid()) {
                tstr.append("   CarIdxP2P_Status: " + carIdxP2P_Status.getBoolean(entry) + "\n"); // Push2Pass active or not}
            }
            if (carIdxP2P_Count.isValid()) {
                tstr.append("   CarIdxP2P_Count: " + carIdxP2P_Count.getInt(entry) + "\n"); // Push2Pass count of usage (or remaining in Race)
            }
            if (carIdxPaceLine.isValid()) {
                tstr.append("   CarIdxPaceLine: " + carIdxPaceLine.getInt(entry) + "\n"); // What line cars are pacing in, or -1 if not pacing}
            }
            if (carIdxPaceRow.isValid()) {
                tstr.append("   CarIdxPaceRow: " + carIdxPaceRow.getInt(entry) + "\n"); // What row cars are pacing in, or -1 if not pacing}
            }
            if (carIdxPaceFlags.isValid()) {
                tstr.append("   CarIdxPaceFlags: " + carIdxPaceFlags.getInt(entry) + "\n"); // irsdk_PaceFlags, Pacing status flags for each car}
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

            // TODO: 3 Jul 2020 YamlParser is not currently working
            // Pull some driver info into a local array
            String[] tstr = new String[256];
            // for (int i = 0; i < maxCars; i++) {
            //     // skip the rest if carIdx not found
            //     log.info("DriverInfo:Drivers:CarIdx: " + i);
            //     if (parceYamlInt(yamlString, tstr, (driverTableTable.get(i).getCarIdx()))) {
            //         log.info("DriverInfo:Drivers:CarIdx:" + i + " CarClassID:");
            //         parceYamlInt(yamlString, tstr, (driverTableTable.get(i).getCarClassId()));
            //
            //         log.info("DriverInfo:Drivers:CarIdx:" + i + "UserName:");
            //         parceYamlStr(yamlString, tstr, driverTableTable.get(i).getDriverName(),
            //                      driverTableTable.get(i).getDriverName().length() - 1);
            //
            //         log.info("DriverInfo:Drivers:CarIdx:" + i + "TeamName:");
            //         parceYamlStr(yamlString, tstr, driverTableTable.get(i).getTeamName(),
            //                      driverTableTable.get(i).getTeamName().length() - 1);
            //
            //         log.info("DriverInfo:Drivers:CarIdx:" + i + "CarNumber:");
            //         parceYamlStr(yamlString, tstr, driverTableTable.get(i).getCarNumStr(),
            //                      driverTableTable.get(i).getCarNumStr().length() - 1);
            //
            //         // TeamID
            //     }
            // }

            //---

            //****Note, your code goes here
            // can write to disk, parse, etc

        }
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

    private boolean parceYamlInt(String yamlStr, String[] path, int dest) {
        if (dest != 0) {
            dest = 0;

            if (!(yamlStr.isEmpty() && path != null)) {
                int count = 0;
                String strPtr = "";
                // TODO: 3 Jul 2020 YamlParser is not currently working
                if (yamlParser.parseYaml(yamlStr, path, strPtr, count)) {
                    // dest = atoi(strPtr);
                    return true;
                }
            }
        }

        return false;
    }

    private void parceYamlStr(String yamlString, String[] tstr, String driverName, int i) {

    }

    private void resetState(boolean isNewConnection) {
        if (isNewConnection) {
            Collections.fill(driverTableTable, new DriverEntry());
        }

        for (int i = 0; i < maxCars; i++) {
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
        if (flags == Flags.irsdk_checkered.getValue()) {
            log.info("checkered ");
        }
        if (flags == Flags.irsdk_white.getValue()) {
            log.info("white ");
        }
        if (flags == Flags.irsdk_green.getValue()) {
            log.info("green ");
        }
        if (flags == Flags.irsdk_yellow.getValue()) {
            log.info("yellow ");
        }
        if (flags == Flags.irsdk_red.getValue()) {
            log.info("red ");
        }
        if (flags == Flags.irsdk_blue.getValue()) {
            log.info("blue ");
        }
        if (flags == Flags.irsdk_debris.getValue()) {
            log.info("debris ");
        }
        if (flags == Flags.irsdk_crossed.getValue()) {
            log.info("crossed ");
        }
        if (flags == Flags.irsdk_yellowWaving.getValue()) {
            log.info("yellowWaving ");
        }
        if (flags == Flags.irsdk_oneLapToGreen.getValue()) {
            log.info("oneLapToGreen ");
        }
        if (flags == Flags.irsdk_greenHeld.getValue()) {
            log.info("greenHeld ");
        }
        if (flags == Flags.irsdk_tenToGo.getValue()) {
            log.info("tenToGo ");
        }
        if (flags == Flags.irsdk_fiveToGo.getValue()) {
            log.info("fiveToGo ");
        }
        if (flags == Flags.irsdk_randomWaving.getValue()) {
            log.info("randomWaving ");
        }
        if (flags == Flags.irsdk_caution.getValue()) {
            log.info("caution ");
        }
        if (flags == Flags.irsdk_cautionWaving.getValue()) {
            log.info("cautionWaving ");
        }

        // drivers black flags
        if (flags == Flags.irsdk_black.getValue()) {
            log.info("black ");
        }
        if (flags == Flags.irsdk_disqualify.getValue()) {
            log.info("disqualify ");
        }
        if (flags == Flags.irsdk_servicible.getValue()) {
            log.info("servicible ");
        }
        if (flags == Flags.irsdk_furled.getValue()) {
            log.info("furled ");
        }
        if (flags == Flags.irsdk_repair.getValue()) {
            log.info("repair ");
        }

        // start lights
        if (flags == Flags.irsdk_startHidden.getValue()) {
            log.info("startHidden ");
        }
        if (flags == Flags.irsdk_startReady.getValue()) {
            log.info("startReady ");
        }
        if (flags == Flags.irsdk_startSet.getValue()) {
            log.info("startSet ");
        }
        if (flags == Flags.irsdk_startGo.getValue()) {
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
        }
    }

    private void printPaceMode(int mode) {
        switch (PaceMode.get(mode)) {
            case irsdk_PaceModeSingleFileStart:
                log.info("SingleFileStart");
                break;
            case irsdk_PaceModeDoubleFileStart:
                log.info("DoubleFileStart");
                break;
            case irsdk_PaceModeSingleFileRestart:
                log.info("SingleFileRestart");
                break;
            case irsdk_PaceModeDoubleFileRestart:
                log.info("DoubleFileRestart:");
                break;
            case irsdk_PaceModeNotPacing:
                log.info("NotPacing");
                break;
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
        int maxLines = Math.min(maxCars, maxCarLines);
        for (int i = 0; i < maxCars; i++) {
            if (linesUsed < maxLines) {
                // is the car in the world, or did we at least collect data on it when it was?
                if (carIdxTrackSurface.getInt() != -1 || carIdxLap.getInt(i) != -1 || carIdxPosition.getInt(i) != 0) {
                    log.info(String.format(
                            " %2d %3s %7.3f %2d %2d %2d %6.3f %s %8.2f %5.2f %2d %2d %2d %2d %7.3f %7.3f %7.3f %7.3f %2d %s %2d %2d %2d 0x%02x \n",
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
                            (carIdxP2P_Status.isValid()) ? carIdxP2P_Status.getBoolean(i) : -1,
                            (carIdxP2P_Count.isValid()) ? carIdxP2P_Count.getInt(i) : -1,
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
