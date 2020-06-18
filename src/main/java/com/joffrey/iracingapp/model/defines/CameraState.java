package com.joffrey.iracingapp.model.defines;

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

    private int value;

    CameraState(int value) {
        this.value = value;
    }
}
