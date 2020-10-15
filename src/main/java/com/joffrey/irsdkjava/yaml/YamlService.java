/*
 *
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.joffrey.irsdkjava.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.yaml.irsdkyaml.YamlFile;
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
