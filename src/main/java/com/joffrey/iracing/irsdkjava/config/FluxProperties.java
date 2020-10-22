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

package com.joffrey.iracing.irsdkjava.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "irsdkjava.config.flux.interval")
public class FluxProperties {

    /**
     * Set interval for camera flux in ms
     */
    private long cameraIntervalInMs = 1000L;

    /**
     * Set interval for lap timing flux in ms
     */
    private long lapTimingIntervalInMs = 1000L;

    /**
     * Set interval for race info flux in ms
     */
    private long raceInfoIntervalInMs = 1000L;

    /**
     * Set interval for telemetry flux in ms
     */
    private long telemetryIntervalInMs = 100L;

    /**
     * Set interval for trackmap tracker flux in ms
     */
    private long trackmapTrackerIntervalInMs = 50L;

    /**
     * Set interval for yaml flux in ms
     */
    private long yamlIntervalInMs = 1000L;

}
