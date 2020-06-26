package com.joffrey.iracingapp;

import com.joffrey.iracingapp.service.iracing.Client;
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
    private              boolean           wasConnected    = false;

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
                processYAMLSessionString("null");
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

    private void processLapInfo() {
        // work out lap times for all cars

        double currentTime = client.getVarDouble(sessionTime);

        // if time moves backwards were in a new session!
        if (lastTime > currentTime) {
            resetState(false);
        }

        for (int i = 0; i < maxCars; i++) {
            carIdxLapDistPct.setEntry(i);
            float curDistPct = client.getVarFloat(carIdxLapDistPct);
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
        int len = 0;

        // Start of YAML file
        tstr.append("---\n");

        // Live weather info, may change as session progresses
        tstr.append("WeatherStatus:\n");
        tstr.append(" AirDensity: " + client.getVarFloat(airDensity));                          // kg/m^3, Density of air at start/finish line
        tstr.append(" AirPressure: " + client.getVarFloat(airPressure));                        // Hg, Pressure of air at start/finish line
        tstr.append(" AirTemp: " + client.getVarFloat(airTemp));                                // C, Temperature of air at start/finish line
        tstr.append(" FogLevel: " + client.getVarFloat(fogLevel));                              // %, Fog level
        tstr.append(" RelativeHumidity: " + client.getVarFloat(relativeHumidity));              // %, Relative Humidity
        tstr.append(" Skies: " + client.getVarInt(skies));                                      // Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
        tstr.append(" TrackTempCrew: " + client.getVarFloat(trackTempCrew));                    // C, Temperature of track measured by crew around track
        tstr.append(" WeatherType: " + client.getVarInt(weatherType));                          // Weather type (0=constant 1=dynamic)
        tstr.append(" WindDir: " + client.getVarFloat(windDir));                                // rad, Wind direction at start/finish line
        tstr.append(" WindVel: " + client.getVarFloat(windVel));                                // m/s, Wind velocity at start/finish line
        tstr.append("\n");

        // session status
        tstr.append("SessionStatus:\n");
        tstr.append(" PitsOpen: "+client.getVarBoolean(pitsOpen)+"\n");                         // True if pit stop is allowed, basically true if caution lights not out
        tstr.append(" RaceLaps: " + client.getVarInt(raceLaps)+ "\n");                          // Laps completed in race
        tstr.append(" SessionFlags: "+client.getVarInt(sessionTime)+"\n");                      // irsdk_Flags, bitfield
        tstr.append(" SessionLapsRemain: "+ client.getVarInt(sessionLapsRemain) + "\n");        // Laps left till session ends
        tstr.append(" SessionLapsRemainEx: "+ client.getVarInt(sessionLapsRemainEx)+"\n");      // New improved laps left till session ends
        tstr.append(" SessionNum: "+client.getVarInt(sessionNum)+"\n");                         // Session number
        tstr.append(" SessionState: "+client.getVarInt(sessionState) + "\n");                   // irsdk_SessionState, Session state
        tstr.append(" SessionTick: "+client.getVarInt(sessionTick)+"\n");                       // Current update number
        tstr.append(" SessionTime: "+client.getVarDouble(sessionTime)+"\n");                    // s, Seconds since session start
        tstr.append(" SessionTimeOfDay: " + client.getVarFloat(sessionTimeOfDay) +"\n");       // s, Time of day in seconds
        tstr.append(" SessionTimeRemain: "+ client.getVarDouble(sessionTimeRemain)+"\n");       // s, Seconds left till session ends
        tstr.append(" SessionUniqueID: "+client.getVarInt(sessionUniqueID)+"\n");               // Session ID
        tstr.append("\n");


        return tstr.toString();
    }

    private void processYAMLSessionString(String yamlString) {

        log.info(yamlString);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    private void updateDisplay() {
        System.out.println("\33[H\033[2J");
        System.out.flush();


    }


}
