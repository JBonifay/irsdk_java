package com.joffrey.irsdkjava.library.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.SdkStarter;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.YamlFile;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log
@Service
public class YamlService {

    private final SdkStarter sdkStarter;

    public String getSessionInfoStr() {
        return new String(sdkStarter.getHeader().getSessionInfoByteBuffer().array());
    }

    public YamlFile getIrsdkYamlFileBean() {
        return createYamlObject(getSessionInfoStr());
    }

    private YamlFile createYamlObject(String yamlString) {
        if (!yamlString.isEmpty()) {
            // Remove 'null' ascii char after '...' yaml ending
            yamlString = yamlString.substring(0, yamlString.indexOf("...") + 3);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            try {
                return objectMapper.readValue(yamlString, YamlFile.class);
            } catch (IOException e) {
                log.warning(e.getMessage());
            }
        }
        return new YamlFile();
    }

}
