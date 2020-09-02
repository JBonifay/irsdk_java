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

package com.joffrey.irsdkjava;

import com.joffrey.irsdkjava.sdk.Header;
import com.joffrey.irsdkjava.sdk.defines.Constant;
import com.joffrey.irsdkjava.sdk.defines.StatusField;
import com.joffrey.irsdkjava.sdk.windows.WindowsService;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IRacingSDK {

    private final WindowsService windowsService;

    private WinNT.HANDLE memMapFile     = null;
    private WinNT.HANDLE dataValidEvent = null;
    @Getter
    private Pointer sharedMemory = null;
    @Getter
    private Header  header       = null;

    private boolean isInitialized = false;


    // ==================== Init ====================
    private boolean getNewData() {
        if (isInitialized || startup()) {

            // if sim is not active, then no new data
            if (header.getStatus() != StatusField.IRSDK_STCONNECTED.getValue()) {
                return false;
            }

            int latest = 0;
            for (int i = 1; i < header.getNumBuf(); i++) {
                if (header.getVarBuf_TickCount(latest) < header.getVarBuf_TickCount(i)) {
                    latest = i;
                }
            }

            int curTickCount = header.getVarBuf_TickCount(latest);
            return curTickCount == header.getVarBuf_TickCount(latest);

            // if here, the data changed out from under us
        }
        return false;
    }

    public boolean isConnected() {
        if (isInitialized || startup()) {
            return (header.getStatus() & StatusField.IRSDK_STCONNECTED.getValue()) > 0;
        }
        return false;
    }

    private boolean startup() {
        // Try to open Memory Mapped File
        if (memMapFile == null) {
            memMapFile = windowsService.openMemoryMapFile(Constant.IRSDK_MEMMAPFILENAME);
        }

        if (memMapFile != null) {
            if (sharedMemory == null) {
                sharedMemory = windowsService.mapViewOfFile(memMapFile);
                header = new Header(sharedMemory);

                if (header.getByteBuffer() == null) {
                    return false;
                }

            }

            if (sharedMemory != null) {
                if (dataValidEvent == null) {
                    dataValidEvent = windowsService.openEvent(Constant.IRSDK_DATAVALIDEVENTNAME);
                }
            }

            if (dataValidEvent != null) {
                isInitialized = true;
                return true;
            }

        }

        isInitialized = false;
        return false;
    }


}
