// package com.sample.laptiming;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// public class LapTiming {
//
//     // Live weather info, may change as session progresses
//     private CVar AirDensity                 = new CVar("AirDensity");
//     private CVar AirPressure                = new CVar("AirPressure");
//     private CVar AirTemp                    = new CVar("AirTemp");
//     private CVar FogLevel                   = new CVar("FogLevel");
//     private CVar RelativevHumidity          = new CVar("RelativevHumidity");
//     private CVar Skies                      = new CVar("Skies"); // (int) Skies (0=clear/1=p cloudy/2=m cloudy/3=overcast)
//     private CVar TrackTempCrew              = new CVar("TrackTempCrew"); // (float) C, Temperature of track measured by crew around track
//     private CVar WeatherType                = new CVar("WeatherType"); // (int) Weather type (0=constant 1=dynamic)
//     private CVar WindDir                    = new CVar("WindDir"); // (float) rad, Wind direction at start/finish line
//     private CVar WindVel                    = new CVar("WindVel"); // (float) m/s, Wind velocity at start/finish line
//     // session status
//     private CVar PitsOpen                   = new CVar("PitsOpen"); // (bool) True if pit stop is allowed, basically true if caution lights not out
//     private CVar RaceLaps                   = new CVar("RaceLaps"); // (int) Laps completed in race
//     private CVar SessionFlags               = new CVar("SessionFlags"); // (int) irsdk_Flags, bitfield
//     private CVar SessionLapsRemain          = new CVar("SessionLapsRemain"); // (int) Laps left till session ends
//     private CVar SessionLapsRemainEx        = new CVar("SessionLapsRemainEx"); // (int) New improved laps left till session ends
//     private CVar SessionNum                 = new CVar("SessionNum"); // (int) Session number
//     private CVar SessionState               = new CVar("SessionState"); // (int) irsdk_SessionState, Session state
//     private CVar SessionTick                = new CVar("SessionTick"); // (int) Current update number
//     private CVar SessionTime                = new CVar("SessionTime"); // (double), s, Seconds since session start
//     private CVar SessionTimeOfDay           = new CVar("SessionTimeOfDay"); // (float) s, Time of day in seconds
//     private CVar SessionTimeRemain          = new CVar("SessionTimeRemain"); // (double) s, Seconds left till session ends
//     private CVar SessionUniqueID            = new CVar("SessionUniqueID"); // (int) Session ID
//     // competitor information, array of up to 64 cars
//     private CVar CarIdxEstTime              = new CVar("CarIdxEstTime"); // (float) s, Estimated time to reach current location on track
//     private CVar CarIdxClassPosition        = new CVar("CarIdxClassPosition"); // (int) Cars class position in race by car index
//     private CVar CarIdxF2Time               = new CVar("CarIdxF2Time"); // (float) s, Race time behind leader or fastest lap time otherwise
//     private CVar CarIdxGear                 = new CVar("CarIdxGear"); // (int) -1=reverse 0=neutral 1..n=current gear by car index
//     private CVar CarIdxLap                  = new CVar("CarIdxLap"); // (int) Lap count by car index
//     private CVar CarIdxLapCompleted         = new CVar("CarIdxLapCompleted"); // (int) Laps completed by car index
//     private CVar CarIdxLapDistPct           = new CVar("CarIdxLapDistPct"); // (float) %, Percentage distance around lap by car index
//     private CVar CarIdxOnPitRoad            = new CVar("CarIdxOnPitRoad"); // (bool) On pit road between the cones by car index
//     private CVar CarIdxPosition             = new CVar("CarIdxPosition"); // (int) Cars position in race by car index
//     private CVar CarIdxRPM                  = new CVar("CarIdxRPM"); // (float) revs/min, Engine rpm by car index
//     private CVar CarIdxSteer                = new CVar("CarIdxSteer"); // (float) rad, Steering wheel angle by car index
//     private CVar CarIdxTrackSurface         = new CVar("CarIdxTrackSurface"); // (int) irsdk_TrkLoc, Track surface type by car index
//     private CVar CarIdxTrackSurfaceMaterial = new CVar("CarIdxTrackSurfaceMaterial"); // (int) irsdk_TrkSurf, Track surface material type by car index
//     // new variables
//     private CVar CarIdxLastLapTime          = new CVar("CarIdxLastLapTime"); // (float) s, Cars last lap time
//     private CVar CarIdxBestLapTime          = new CVar("CarIdxBestLapTime"); // (float) s, Cars best lap time
//     private CVar CarIdxBestLapNum           = new CVar("CarIdxBestLapNum"); // (int) Cars best lap number
//     private CVar CarIdxP2P_Status           = new CVar("CarIdxP2P_Status"); // (bool) Push2Pass active or not
//     private CVar CarIdxP2P_Count            = new CVar("CarIdxP2P_Count"); // (int) Push2Pass count of usage (or remaining in Race)
//     private CVar PaceMode                   = new CVar("PaceMode"); // (int) irsdk_PaceMode, Are we pacing or not
//     private CVar CarIdxPaceLine             = new CVar("CarIdxPaceLine"); // (int) What line cars are pacing in, or -1 if not pacing
//     private CVar CarIdxPaceRow              = new CVar("CarIdxPaceRow"); // (int) What row cars are pacing in, or -1 if not pacing
//     private CVar CarIdxPaceFlags            = new CVar("CarIdxPaceFlags"); // (int) irsdk_PaceFlags, Pacing status flags for each car
//
//     private static final int      maxCars      = 64;
//     private static final int      maxNameLen   = 64;
//     private static       double   lastTime     = -1;
//     private static final float[]  lastDistPct  = new float[maxCars];
//     private static final double[] lapStartTime = new double[maxCars];
//     // lap time for last lap, or -1 if not yet completed a lap
//     private static final float[]  lapTime      = new float[maxCars];
//
//     private DriverEntry[] driverEntries = new DriverEntry[maxCars];
//
//     public boolean parceYamlInt(String yamlStr, String path, int dest) {
//         if (dest != 0) {
//             dest = 0;
//
//             if (!yamlStr.isEmpty() && !path.isEmpty()) {
//
//                 int count = 0;
//                 char strPtr = 0;
//
//                 if (parseYaml(yamlStr, path, strPtr, count)) {
//                     return true;
//                 }
//             }
//         }
//         return false;
//     }
//
//     public boolean parceYamlStr(String yamlStr, String path, char dest, int maxCount) {
//
//         if (dest != 0 && maxCount > 0) {
//
//             // dest[0] = '\0';
//
//             if (!yamlStr.isEmpty() && !path.isEmpty()) {
//
//                 int count = 0;
//                 char strPtr = 0;
//
//                 if (parseYaml(yamlStr, path, strPtr, count)) {
//                     // strip leading quotes
//                     if (strPtr == '"') {
//                         strPtr++;
//                         count--;
//                     }
//
//                     int l = Math.min(count, maxCount);
//                     // strncpy(dest, strPtr, l);
//                     // dest[1] = '\0';
//
//                     // strip trailing quotes
//                     // if (l >= 1 && dest[l - 1] == '"') {
//                     //     dest[l - 1] = '\0';
//                     // }
//
//                     return true;
//                 }
//             }
//         }
//         return false;
//     }
//
//     public void resetState(boolean isNewConnection) {
//         if (isNewConnection)
//         // memset(g_driverTableTable, 0, sizeof(g_driverTableTable));
//
//         {
//             for (int i = 0; i < maxCars; i++) {
//                 lastTime = -1;
//                 lastDistPct[i] = -1;
//                 lapStartTime[i] = -1;
//                 lapTime[i] = -1;
//             }
//         }
//     }
//
//     public double interpolateTimeAcrossPoint(double t1, double t2, float p1, float p2, float check) {
//         // unwrap if crossing start/finish line
//         //****Note, assumes p1 is a percent from 0 to 1
//         // if that is not true then unwrap the numbers before calling this function
//         if (p1 > p2) {
//             p1 -= 1;
//         }
//
//         // calculate where line is between points
//         float pct = (check - p1) / (p2 - p1);
//
//         return t1 + (t2 - t1) * pct;
//     }
//
//     public void processLapInfo() {
//         // work out lap times for all cars
//         // double curTime = SessionTime.getDouble();
//
//         // if time moves backwards were in a new session!
//         if (lastTime > curTime) {
//             resetState(false);
//         }
//
//         for (int i = 0; i < maxCars; i++) {
//             float curDistPct = CarIdxLapDistPct.getFloat(i);
//             // reject if the car blinked out of the world
//             if (curDistPct != -1) {
//                 // did we cross the lap?
//                 if (lastDistPct[i] > 0.9f && curDistPct < 0.1f) {
//                     // calculate exact time of lap crossing
//                     double curLapStartTime = interpolateTimeAcrossPoint(lastTime, curTime, lastDistPct[i], curDistPct, 0);
//
//                     // calculate lap time, if already crossed start/finish
//                     if (lapStartTime[i] != -1) {
//                         lapTime[i] = (float) (curLapStartTime - lapStartTime[i]);
//                     }
//
//                     // and store start/finish crossing time for next lap
//                     lapStartTime[i] = curLapStartTime;
//                 }
//
//                 lastDistPct[i] = curDistPct;
//             }
//         }
//
//         lastTime = curTime;
//     }
//
//
//
// }
