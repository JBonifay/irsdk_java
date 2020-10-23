![iRacing image](https://www.jayski.com/wp-content/uploads/sites/31/2020/03/14/iRacing.png)

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/JBonifay/irsdk_java)
![Maven CI/CD](https://github.com/JBonifay/irsdk_java/workflows/Maven%20CI/CD/badge.svg)
![GitHub](https://img.shields.io/github/license/JBonifay/irsdk_java)
![GitHub Repo stars](https://img.shields.io/github/stars/JBonifay/irsdk_java?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/JBonifay/irsdk_java?style=social)

# iRacing java SDK  
Unofficial [iRacing](https://www.iracing.com/) SDK implementation for Java :checkered_flag:

The sdk provide API for fetching simulator data through [reactor Flux](https://projectreactor.io/) 

* [Github repository](https://github.com/JBonifay/irsdk_java)  
* [Documentation](https://jbonifay.github.io/irsdk_java/)  
* [Forum thread](https://members.iracing.com/jforum/posts/list/3749393.page#12148089)  

# Install
##### Maven
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

##### In your MainApplication
The java Sdk use spring autoconfiguration, you just need to import IRacingLibrary object in your project 

```java
@Autowired
private IRacingLibrary iRacingLibrary;
``` 


##### Dependencies  
- Java 11  
- Spring Boot  
- Maven  


# Available API  
Available data Flux can be find under [IRacingLibrary.java](src/main/java/com/joffrey/iracing/irsdkjava/IRacingLibrary.java)
```
- Flux<CameraPacket> : Packet containing camera info and drivers info, this flux can be used for a TV editor  
- Flux<List<LapTimingData>> : Packet containing a list of LapTimingData Objects, list is sort by drivers live position  
- Flux<RaceInfo> : Packet containing info about the current race, player info (Fuel/Laps/time remaining, ...)
- Flux<TelemetryData> : Packet containing Telemetry Live data
- Flux<List<TrackmapTrackerDriver>> : Packet containing usefull info for display in a Race Tracker 
```  

```
- broadcastMsg used to send a message to the sim (Change camera, control some settings,...)
```

# Flux configuration

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
