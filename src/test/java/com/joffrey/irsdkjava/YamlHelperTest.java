package com.joffrey.irsdkjava;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.yaml.irsdkyaml.YamlFile;
import java.io.File;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class YamlHelperTest {

    @SneakyThrows
    static ByteBuffer createByteBufferYamlFile(String file) {
        File yamlFile = new ClassPathResource(file).getFile();
        return ByteBuffer.wrap(FileCopyUtils.copyToByteArray(yamlFile));
    }

    @SneakyThrows
    static YamlFile loadYamlObject(ByteBuffer byteBufferYamlFile) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        String yamlString = new String(byteBufferYamlFile.array());
        yamlString = yamlString.substring(0, yamlString.indexOf("...") + 3);
        return objectMapper.readValue(yamlString, YamlFile.class);
    }


}
