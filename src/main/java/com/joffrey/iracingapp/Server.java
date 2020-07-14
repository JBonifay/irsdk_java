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

package com.joffrey.iracingapp;

import com.joffrey.iracingapp.model.defines.VarType;

public class Server {

    // log in realtime to clients
    public static final int IRSDK_LOG_LIVE = 1;
    // log to disk
    public static final int IRSDK_LOG_DISK = 2;
    // log both to realtime and to disk
    public static final int IRSDK_LOG_ALL = (IRSDK_LOG_LIVE | IRSDK_LOG_DISK);

    public int getRegVar(String dynamicVar, int dynamicVar1, VarType irsdk_int, int i, String s, String m, int irsdkLogAll) {
        return 0;
    }

    public boolean isHeaderFinalized() {
        return false;
    }

    public void finalizeHeader() {

    }

    public boolean isInitialized() {
        return false;
    }

    public void resetSampleVars() {

    }
}
