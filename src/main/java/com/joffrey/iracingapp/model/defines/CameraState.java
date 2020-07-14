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

package com.joffrey.iracingapp.model.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public enum CameraState {
    irsdk_IsSessionScreen(0x0001), // the camera tool can only be activated if viewing the session screen (out of car)
    irsdk_IsScenicActive(0x0002), // the scenic camera is active (no focus car)

    //these can be changed with a broadcast message
    irsdk_CamToolActive(0x0004),
    irsdk_UIHidden(0x0008),
    irsdk_UseAutoShotSelection(0x0010),
    irsdk_UseTemporaryEdits(0x0020),
    irsdk_UseKeyAcceleration(0x0040),
    irsdk_UseKey10xAcceleration(0x0080),
    irsdk_UseMouseAimMode(0x0100);

    private static final Map<Integer, CameraState> cameraStateMap = new HashMap();
    private              int                       value;

    CameraState(int value) {
        this.value = value;
    }

    public static int getNextCameraState(int cameraState) {
        Iterator it = cameraStateMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey().equals(cameraState)) {
                return (int) ((Entry) it.next()).getValue();
            }
        }
        return 1;
    }

    static {
        for (CameraState cameraState : EnumSet.allOf(CameraState.class)) {
            cameraStateMap.put(cameraState.value, cameraState);
        }
    }
}
