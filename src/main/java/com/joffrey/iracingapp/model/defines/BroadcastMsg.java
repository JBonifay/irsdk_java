package com.joffrey.iracingapp.model.defines;

public enum BroadcastMsg {

    irsdk_BroadcastCamSwitchPos,                     // car position, group, camera
    irsdk_BroadcastCamSwitchNum,                     // driver #, group, camera
    irsdk_BroadcastCamSetState,                      // irsdk_CameraState, unused, unused
    irsdk_BroadcastReplaySetPlaySpeed,               // speed, slowMotion, unused
    irskd_BroadcastReplaySetPlayPosition,            // irsdk_RpyPosMode, Frame Number (high, low)
    irsdk_BroadcastReplaySearch,                     // irsdk_RpySrchMode, unused, unused
    irsdk_BroadcastReplaySetState,                   // irsdk_RpyStateMode, unused, unused
    irsdk_BroadcastReloadTextures,                   // irsdk_ReloadTexturesMode, carIdx, unused
    irsdk_BroadcastChatComand,                       // irsdk_ChatCommandMode, subCommand, unused
    irsdk_BroadcastPitCommand,                       // irsdk_PitCommandMode, parameter
    irsdk_BroadcastTelemCommand,                     // irsdk_TelemCommandMode, unused, unused
    irsdk_BroadcastFFBCommand,                       // irsdk_FFBCommandMode, value (float, high, low)
    irsdk_BroadcastReplaySearchSessionTime,          // sessionNum, sessionTimeMS (high, low)
    irsdk_BroadcastLast                              // unused placeholder

}
