package com.joffrey.irsdkjava.library.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.YamlFile;
import java.io.IOException;
import java.time.Duration;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Log
@Service
public class YamlService {

    private final SdkStarter   sdkStarter;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    private       YamlFile     yamlFile;

    public YamlService(SdkStarter sdkStarter) {
        this.sdkStarter = sdkStarter;
        this.yamlFile = new YamlFile();
        Flux<YamlFile> map = Flux.interval(Duration.ofSeconds(1)).filter(aLong -> sdkStarter.isRunning())
                                 .map(aLong -> yamlFile = loadYamlObject());

        map.subscribe();
    }

    public YamlFile getYamlFile() {
        return yamlFile;
    }

    private YamlFile loadYamlObject() {
        String yamlString = new String(sdkStarter.getHeader().getSessionInfoByteBuffer().array());
        if (!yamlString.isEmpty()) {
            // Remove 'null' ascii char after '...' yaml ending
            yamlString = yamlString.substring(0, yamlString.indexOf("...") + 3);
            try {
                return objectMapper.readValue(yamlString, YamlFile.class);
            } catch (IOException e) {
                log.warning(e.getMessage());
            }
        }
        return yamlFile;
    }

}
