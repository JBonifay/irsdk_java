package com.joffrey.iracingapp.model.defines;

public enum BroadcastMsg {

    irsdk_BroadcastCamSwitchPos(0),                     // car position, group, camera
    irsdk_BroadcastCamSwitchNum(1),                     // driver #, group, camera
    irsdk_BroadcastCamSetState(2),                      // irsdk_CameraState, unused, unused
    irsdk_BroadcastReplaySetPlaySpeed(3),               // speed, slowMotion, unused
    irskd_BroadcastReplaySetPlayPosition(4),            // irsdk_RpyPosMode, Frame Number (high, low)
    irsdk_BroadcastReplaySearch(5),                     // irsdk_RpySrchMode, unused, unused
    irsdk_BroadcastReplaySetState(6),                   // irsdk_RpyStateMode, unused, unused
    irsdk_BroadcastReloadTextures(7),                   // irsdk_ReloadTexturesMode, carIdx, unused
    irsdk_BroadcastChatComand(8),                       // irsdk_ChatCommandMode, subCommand, unused
    irsdk_BroadcastPitCommand(9),                       // irsdk_PitCommandMode, parameter
    irsdk_BroadcastTelemCommand(10),                     // irsdk_TelemCommandMode, unused, unused
    irsdk_BroadcastFFBCommand(11),                       // irsdk_FFBCommandMode, value (float, high, low)
    irsdk_BroadcastReplaySearchSessionTime(12),          // sessionNum, sessionTimeMS (high, low)
    irsdk_BroadcastLast(13),                              // unused placeholder
    ;

    private final int value;

    BroadcastMsg(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
