/*
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
 */

package com.joffrey.irsdkjava.sdk.defines;

import lombok.Getter;

@Getter
public class Constant {

    public static final String IRSDK_DATAVALIDEVENTNAME = "Local\\IRSDKDataValidEvent";
    public static final String IRSDK_MEMMAPFILENAME     = "Local\\IRSDKMemMapFileName";
    public static final String IRSDK_BROADCASTMSGNAME   = "IRSDK_BROADCASTMSG";

    public static final int IRSDK_MAX_BUFS   = 4;
    public static final int IRSDK_MAX_STRING = 32;
    // descriptions can be longer than max_string!
    public static final int IRSDK_MAX_DESC   = 64;

    public static final int   IRSDK_UNLIMITED_LAPS = 32767;
    public static final float IRSDK_UNLIMITED_TIME = 604800.0f;

    // latest version of our telemetry headers
    public static final int IRSDK_VER = 2;


}
