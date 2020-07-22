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

package com.joffrey.iracing.model.defines;

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
