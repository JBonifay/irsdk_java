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

import com.joffrey.irsdkjava.model.defines.BroadcastMsg;
import com.joffrey.irsdkjava.model.defines.ChatCommandMode;
import com.joffrey.irsdkjava.model.defines.FFBCommandMode;
import com.joffrey.irsdkjava.model.defines.PitCommandMode;
import com.joffrey.irsdkjava.model.defines.ReloadTexturesMode;
import com.joffrey.irsdkjava.model.defines.RpyPosMode;
import com.joffrey.irsdkjava.model.defines.RpySrchMode;
import com.joffrey.irsdkjava.model.defines.RpyStateMode;
import com.joffrey.irsdkjava.model.defines.TelemCommandMode;
import com.joffrey.irsdkjava.service.iracing.Utils;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Simple demo that sends messages to the iRacing sim
 */
@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class MsgTest implements CommandLineRunner {

    private final Utils utils;

    public static void main(String[] args) {
        SpringApplication.run(MsgTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //trap ctrl-c
        // signal(SIGINT, ex_program);
        log.info("iRacing remote control demo:\n");
        log.info(" - press 'a' to swithc cameras by position.\n");
        log.info(" - press 'b' to swithc cameras by driver #.\n");
        log.info(" - press 'c' to swithc cameras but not driver.\n");
        log.info(" - press 'd' to cycle replay playback speed.\n");
        log.info(" - press 'e' to cycle replay search mode.\n");
        log.info(" - press 'f' to cycle replay set playback position.\n");
        log.info(" - press 'g' to cycle camera state.\n");
        log.info(" - press 'h' to clear the replay tape.\n");
        log.info("\n");
        log.info(" - press 'i' to clear the chat window.\n");
        log.info(" - press 'j' to reply to a private chat.\n");
        log.info(" - press 'k' to activate the chat window.\n");
        log.info(" - press 'l' to activate a chat macro.\n");
        log.info("\n");
        log.info(" - press 'm' to clear all pitstop commands.\n");
        log.info(" - press 'n' to add fuel.\n");
        log.info(" - press 'o' to change tires.\n");
        log.info(" - press 'p' to clean windows.\n");
        log.info(" - press 'q' to clear tire pitstop commands.\n");
        log.info("\n");
        log.info(" - press 'r' to reload the custom car textures for all cars.\n");
        log.info(" - press 's' to reload the custom car textures for a specific carIdx.\n");
        log.info("\n");
        log.info(" - press 't' to play at normal speed.\n");
        log.info(" - press 'u' to play at 1/16th speed.\n");
        log.info(" - press 'v' to pause the replay.\n");
        log.info("\n");
        log.info(" - press 'w' to stop recording telemetry to disk.\n");
        log.info(" - press 'x' to start recording telemetry to disk.\n");
        log.info(" - press 'y' to save out the old telemetry file and start a new one.\n");
        log.info(" - press 'z' to toggle 'true' FFB mode.\n");
        log.info(" - press 'A' to go to session 1, time 100 seconds.\n");
        log.info("\n");
        log.info(" - press 'B' to request a fast repair.\n");
        log.info(" - press 'C' to uncheck clear winshield.\n");
        log.info(" - press 'D' to uncheck a fast repair.\n");
        log.info(" - press 'E' to uncheck add fuel.\n");
        log.info(" press any other key to exit\n\n");

        boolean done = false;
        int playSpeed = 16;
        boolean slowMotion = false;
        int replaySearch = 0;
        int replayOffset = 0;
        int replayFrame = 600; // 10 seconds
        int cameraState = 0;
        int carIdx = 0;
        int chatMacro = 0;
        boolean trueFFBState = false;

        while (!done) {
            String c = getUserInput();
            switch (c.charAt(0)) {
                case 'a':
                    log.info("Switching camera to Pos 1, Group 1\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastCamSwitchPos, 1, 1, 0);
                    break;

                case 'b':
                    log.info("Switching camera to Driver #64, Group 2\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastCamSwitchNum, 64, 2, 0);
                    break;

                case 'c':
                    log.info("Switching camera to Group 3\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastCamSwitchPos, 0, 3, 0);
                    break;

                case 'd':
                    log.info(String.format("Set playback speed to %d %s%n", playSpeed, slowMotion));
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySetPlaySpeed, playSpeed, slowMotion ? 1 : 0, 0);
                    playSpeed--;
                    if (playSpeed < -16) {
                        playSpeed = 16;
                        slowMotion = !slowMotion;
                    }
                    break;

                case 'e':
                    log.info(String.format("Set replay search to %d%n", replaySearch));
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySearch, replaySearch, 0, 0);

                    replaySearch++;
                    if (replaySearch >= RpySrchMode.irsdk_RpySrch_Last.getValue()) {
                        replaySearch = 0;
                    }
                    break;

                case 'f':
                    log.info(String.format("Set replay offset to %d %d%n", replayOffset, replayFrame));
                    utils.broadcastMsg(BroadcastMsg.irskd_BroadcastReplaySetPlayPosition, replayOffset, replayFrame);

                    replayOffset++;
                    if (replayOffset >= RpyPosMode.irsdk_RpyPos_Last.getValue()) {
                        replayOffset = 0;
                    }

                    break;

                case 'g':
                    if (cameraState != 0) {
                        cameraState = 0;
                    } else {
                        cameraState = 508;
                    }

                    log.info(String.format("Set camera state %d%n", cameraState));
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastCamSetState, cameraState, 0);
                    break;

                case 'h':
                    log.info("Clear replay tape\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySetState,
                                       RpyStateMode.irsdk_RpyState_EraseTape.getValue(),
                                       0);
                    break;

                // chat commands
                case 'i':
                    log.info("Clear chat window\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastChatComand,
                                       ChatCommandMode.irsdk_ChatCommand_Cancel.getValue(),
                                       0);
                    break;

                case 'j':
                    log.info("Begin reply chat window\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastChatComand,
                                       ChatCommandMode.irsdk_ChatCommand_Reply.getValue(),
                                       0);
                    break;

                case 'k':
                    log.info("Activate chat window\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastChatComand,
                                       ChatCommandMode.irsdk_ChatCommand_BeginChat.getValue(),
                                       0);
                    break;

                case 'l':
                    if (chatMacro > 14) {
                        chatMacro = 0;
                    }

                    log.info(String.format("Sending chat macro %d%n", chatMacro));
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastChatComand,
                                       ChatCommandMode.irsdk_ChatCommand_Macro.getValue(),
                                       chatMacro++,
                                       0);
                    break;

                // pit stop commands
                case 'm':
                    log.info("Clear all pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_Clear.getValue(),
                                       0);
                    break;
                case 'n':
                    log.info("Add fuel\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_Fuel.getValue(),
                                       10); // add 10 liters (2.7 gallon), or pass '0' to leave level at previous value
                    break;
                case 'o':
                    log.info("Change all tires\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_LF.getValue(),
                                       0); // leave pressure alone
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_RF.getValue(),
                                       70); // fill tire to 70 KPa (10 psi)
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_LR.getValue(),
                                       200); // 200 KPa (30 psi)
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_RR.getValue(),
                                       1000); // Max, whatever that is
                    break;
                case 'p':
                    log.info("Clean window\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand, PitCommandMode.irsdk_PitCommand_WS.getValue(), 0);
                    break;
                case 'q':
                    log.info("Clear tire pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_ClearTires.getValue(),
                                       0);
                    break;

                // etc
                case 'r':
                    log.info("Reload custom car textures for all cars\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReloadTextures,
                                       ReloadTexturesMode.irsdk_ReloadTextures_All.getValue(),
                                       0,
                                       0);
                    break;

                case 's':
                    log.info(String.format("Reload custom car textures for carIdx %d%n", carIdx));
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReloadTextures,
                                       ReloadTexturesMode.irsdk_ReloadTextures_CarIdx.getValue(),
                                       carIdx++,
                                       // carIdx
                                       0); // not currently used
                    // loop for fun
                    if (carIdx > 20) {
                        carIdx = 0;
                    }
                    break;

                case 't':
                    log.info("Set playback speed to normal speed\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySetPlaySpeed, 1, 0, 0);
                    break;

                case 'u':
                    log.info("Set playback speed to 1/16th speed\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySetPlaySpeed, 16, 1, 0);
                    break;

                case 'v':
                    log.info("Pause playback\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySetPlaySpeed, 0, 0, 0);
                    break;

                case 'w':
                    log.info("Stop recording telemetry\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastTelemCommand,
                                       TelemCommandMode.irsdk_TelemCommand_Stop.getValue(),
                                       0,
                                       0);
                    break;
                case 'x':
                    log.info("Start recording telemetry\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastTelemCommand,
                                       TelemCommandMode.irsdk_TelemCommand_Start.getValue(),
                                       0,
                                       0);
                    break;
                case 'y':
                    log.info("Start new telemetry file\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastTelemCommand,
                                       TelemCommandMode.irsdk_TelemCommand_Restart.getValue(),
                                       0,
                                       0);
                    break;
                case 'z': {
                    float force = (trueFFBState) ? -1.0f : 20.9998f;
                    trueFFBState = !trueFFBState;

                    if (force < 0.0f) {
                        log.info("Set wheel to user controlled FFB\n");
                    } else {
                        log.info(String.format("Set wheel to %f Nm%n", force));
                    }

                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastFFBCommand,
                                       FFBCommandMode.irsdk_FFBCommand_MaxForce.getValue(),
                                       force);
                }

                break;

                case 'A':
                    log.info("Set replay search to session 1 at 100 seconds\n");
                    // this does a search and not a direct jump, may take a while!
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastReplaySearchSessionTime, 1, 100 * 1000);

                    break;

                case 'B':
                    log.info("Fast repair pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand, PitCommandMode.irsdk_PitCommand_FR.getValue(), 0);
                    break;

                case 'C':
                    log.info("Clear (uncheck) winshield pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_ClearWS.getValue(),
                                       0);
                    break;

                case 'D':
                    log.info("Clear Fast Repair pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_ClearFR.getValue(),
                                       0);
                    break;

                case 'E':
                    log.info("Clear Add Fuel pit commands\n");
                    utils.broadcastMsg(BroadcastMsg.irsdk_BroadcastPitCommand,
                                       PitCommandMode.irsdk_PitCommand_ClearFuel.getValue(),
                                       0);
                    break;

                default:
                    done = true;
            }
        }

    }

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
