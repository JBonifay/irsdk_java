package com.joffrey.irsdkjava.library.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.SdkStarter;
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
        if (sdkStarter.isRunning()) {
            return new String(sdkStarter.getHeader().getSessionInfoByteBuffer().array());
        }
        return "";
    }

    public YamlFile getIrsdkYamlFileBean() {
        if (sdkStarter.isRunning()) {
            return createYamlObject(getSessionInfoStr());
        }
        return null;
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
        return null;
    }

}
