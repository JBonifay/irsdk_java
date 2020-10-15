![iRacing image](https://www.jayski.com/wp-content/uploads/sites/31/2020/03/14/iRacing.png)

# iRacing java SDK  
Unofficial [iRacing](https://www.iracing.com/) SDK implementation for Java

The sdk provide API for fetching simulator data through [reactor Flux](https://projectreactor.io/) 

* [Github repository](https://github.com/JBonifay/irsdk_java)  
* [Documentation](https://jbonifay.github.io/irsdk_java/)  
* [Forum thread](https://members.iracing.com/jforum/posts/list/3749393.page#12148089)  

# Install
##### Maven
```xml
    <dependency>
      <groupId>com.joffrey</groupId>
      <artifactId>irsdk_java</artifactId>
      <version>{version}</version>
    </dependency>
```
##### In your app
```java
@SpringBootApplication
@Import(IRacingLibraryConfiguration.class)
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

}
```


##### Dependencies  
- Java 11  
- Spring Boot  
- Maven  


# Available API  
Available data Flux can be find under [IRacingLibrary.java](src/main/java/com/joffrey/irsdkjava/IRacingLibrary.java)
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

## Contributing / Reporting issues
It can be interresting to add more API with more/less content, facilitate the broadcastMsg API  
Any help is welcome, it can be fix a bug, code improvement ...   
[How to contribute / report an issue](CONTRIBUTING.md)

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
