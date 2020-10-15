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

package com.joffrey.irsdkjava.camera;

import com.joffrey.irsdkjava.camera.model.CameraPacket;
import com.joffrey.irsdkjava.model.SdkStarter;
import com.joffrey.irsdkjava.yaml.YamlService;
import java.time.Duration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CameraService {

    private final SdkStarter  sdkStarter;
    private final YamlService yamlService;

    private final Flux<CameraPacket> cameraPacketFlux;

    public CameraService(SdkStarter sdkStarter, YamlService yamlService) {
        this.sdkStarter = sdkStarter;
        this.yamlService = yamlService;
        this.cameraPacketFlux =
                Flux.interval(Duration.ofSeconds(1)).filter(aLong -> sdkStarter.isRunning()).flatMap(aLong -> loadCameraData());
    }

    public Flux<CameraPacket> getCameraPacketFlux() {
        return cameraPacketFlux;
    }

    private Flux<CameraPacket> loadCameraData() {
        return Flux.zip(Mono.just(yamlService.getYamlFile().getCameraInfo().getGroups()),
                        Mono.just(yamlService.getYamlFile().getDriverInfo().getDrivers()))
                   .map(objects -> new CameraPacket(objects.getT1(), objects.getT2()));
    }
}
