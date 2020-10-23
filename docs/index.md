![iRacing image](https://www.jayski.com/wp-content/uploads/sites/31/2020/03/14/iRacing.png)

# iRacing java SDK  
Unofficial [iRacing](https://www.iracing.com/) SDK implementation for Java

The sdk provide API for fetching simulator data through [reactor Flux](https://projectreactor.io/) 

* [Github repository](https://github.com/JBonifay/irsdk_java)  
* [Documentation](https://jbonifay.github.io/irsdk_java/)  
* [Forum thread](https://members.iracing.com/jforum/posts/list/3749393.page#12148089)  

# How does it work ?
#### Sdk Dependencies
The irsdk_java deliver fast data from simulator thanks to:
- **[Spring boot](https://spring.io/)** for DI
- **[Reactor](reactor.io)** for Reactive flux
- **[JNA](https://github.com/java-native-access/jna)** for communication with windows (Memory mapped files, Broadcast messages, etc)  
_have a look at [WindowsService](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/windows/WindowsService.java) for details_
- **[Jackson](https://github.com/FasterXML/jackson)** for yaml parsing

#### Sdk initialization
The Sdk initialize itself after the first API call.
The init is donne in [SdkStarter](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/model/SdkStarter.java)  

Each call of an API check first if Sdk is initialized with memorymapped files, irsdk_header with sharedMemory,...  
then vars from header are populated in `private final Map<String, VarHeader> vars = new HashMap<>();`
when a value from shared memory is needed (see `public int getVarInt(String varName, int entry)` for example), the var info like position in memory is taken from this hashmap instead of fetching again the memory.

Yaml file object is updated each seconds in [YamlService](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/yaml/YamlService.java),
like that when a value is need from yaml, yaml file is already filled with data.

# Install
### Maven
You can find active release [here](https://github.com/JBonifay/irsdk_java/packages/449562)
Just follow the instructions and you'll be ready

Add this to pom.xml:
```xml
<dependency>
  <groupId>com.joffrey.iracing</groupId>
  <artifactId>irsdkjava-spring-boot-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Run via command line:
```
$ mvn install
```

### In your MainApplication
The java Sdk use spring autoconfiguration, you just need to import IRacingLibrary object in your project 

```java
@Autowired
private IRacingLibrary iRacingLibrary;
``` 


### Dependencies  
- Java 11  
- Spring Boot  
- Maven  


# Available API  
Available data Flux can be find under [IRacingLibrary.java](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/IRacingLibrary.java)

### CameraPacket
Simple object containing data from Yaml file only:  
[CamerasGroupsYaml](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/yaml/irsdkyaml/CamerasGroupsYaml.java)  
[DriverInfoYaml](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/yaml/irsdkyaml/DriverInfoYaml.java)  

```java
public Flux<CameraPacket> getCameraPacket() {
    return cameraService.getCameraPacketFlux();
}
```


### LapTimingPacket
List of [LapTimingData](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/laptiming/model/LapTimingData.java) objects representing en entry used for La Timing,  
LapTimingData content are a mix between live data (CarPosition, CarLivePosition, etc, etc)  
with data from yaml file used to fetch info of the driver 
```java
public Flux<List<LapTimingData>> getLapTimingDataList() {
    return lapTimingService.getLapTimingDataListFlux();
}
```


### RaceInfo Packet
[RaceInfo](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/raceinfo/model/RaceInfo.java) contain all the data of actual session  
like track name, weather, actual time, fuel of actual player.

```java
public Flux<RaceInfo> getRaceInfo() {
    return raceInfoService.getRaceInfoFlux();
}
```


### TelemetryData Packet
the [TelemetryData](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/telemetry/model/TelemetryData.java) contain all data from telemetry,  vars are organized as the following:  
- Pedals && Speed  
- Fuel && Angles  
- Weather  
- Session  
```java
public Flux<TelemetryData> getTelemetryDataFlux() {
    return telemetryDataFlux.autoConnect();
}
```

### TrackmapTracker Packet
the [TrackmapTracker](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/trackmaptracker/model/TrackmapTrackerDriver.java) contain all data from needed fro a race tracker, data returned contain all drivers, with drivers info, and drivers distance on track (pct %).
```java
public Flux<List<TrackmapTrackerDriver>> getTrackmapTrackerList() {
    return trackmapTrackerService.getTrackmapTrackerListFlux();
}
```

### Broadcasting messages to simulator
you'll can find two methods in [IRacingLibrary](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/IRacingLibrary.java) used to send messages to the simulator.
```java
public void broadcastMsg(BroadcastMsg msg, int var1, int var2, int var3) {
    broadcastMsg(msg, var1, windowsService.MAKELONG(var2, var3));
}

public void broadcastMsg(BroadcastMsg msg, int var1, float var2) {
    // multiply by 2^16-1 to move fractional part to the integer part
    int real = (int) (var2 * 65536.0f);

    broadcastMsg(msg, var1, real);
}
```

#### Usage
Parameters can be found in [BroadcastMsg](https://github.com/JBonifay/irsdk_java/blob/master/src/main/java/com/joffrey/irsdkjava/model/defines/BroadcastMsg.java).  

Method is working as following:
|                               |       First parameter     |       Second parameter      |     Third parameter     |
|-------------------------------|---------------------------|-----------------------------|-------------------------|
| CamSwitchPos                  | CarPosition         	    | Group      	              | Camera       	        |
| CamSwitchNum                  | Driver#           	    | Group      	              | Camera       	        |
| CamSetState                   | irsdk_CameraState 	    | unused     	              | unused       	        |
| ReplaySetPlaySpeed            | Speed             	    | Slowmotion 	              | unused       	        |
| ReplaySetPlayPosition         | irsdk_RpyPosMode    	    | Frame Number (high, low)    | unused       	        |
| ReplaySearch                  | irsdk_RpySrchMode    	    | unused                      | unused       	        |
| ReplaySetState                | irsdk_RpyStateMode   	    | unused                      | unused       	        |
| ReloadTextures                | irsdk_ReloadTexturesMode  | carIdx                      | unused       	        |
| ChatComand                    | irsdk_ChatCommandMode     | subCommand                  | unused       	        |
| PitCommand                    | irsdk_PitCommandMode      | parameter                   | unused       	        |
| TelemCommand                  | irsdk_TelemCommandMode    | unused                      | unused       	        |
| FFBCommand                    | irsdk_FFBCommandMode      | value (float, high, low)    | unused       	        |
| ReplaySearchSessionTime       | sessionNum                | sessionTimeMS (high, low)   | unused       	        |

```java
iRacingLibrary.broadcastMsg( COMMAND , FIRST_PARAM , SECOND_PARAM, THIRD_PARAM);
```

##### Example
- **I want to set** the active camera focus on player with car number 63, cockpit camera (For example group is 2), camera is 0 
```java
iRacingLibrary.broadcastMsg(BroadcastMsg.irsdk_BroadcastCamSwitchNum, 63, 2, 0);
```
- **I want to set** all tire change at next pit stop 
```java
iRacingLibrary.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand, irsdk_PitCommand_ClearTires, 0, 0);
```

- **I want to set** telemetry on 
```java
iRacingLibrary.broadcastMsg(BroadcastMsg.irsdk_BroadcastTelemCommand, irsdk_TelemCommand_Start, 0, 0);
```

#### Flux configuration
     
In your spring project you can modify the settings of the different Flux Api
If you need to change these values you can do as the following:
```properties
irsdkjava.config.flux.interval.camera=1000
irsdkjava.config.flux.interval.lap-timing=1000
irsdkjava.config.flux.interval.race-info=1000
irsdkjava.config.flux.interval.telemetry=500
irsdkjava.config.flux.interval.trackmap-tracker=100
irsdkjava.config.flux.interval.yaml=100
```  

## Contributing / Reporting issues
It can be interresting to add more API with more/less content, facilitate the broadcastMsg API  
Any help is welcome, it can be fix a bug, code improvement ...   
[How to contribute / report an issue](CONTRIBUTING.md)

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
