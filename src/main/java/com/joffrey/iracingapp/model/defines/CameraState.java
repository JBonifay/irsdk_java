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
