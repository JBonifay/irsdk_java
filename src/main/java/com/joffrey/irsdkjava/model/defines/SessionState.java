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

package com.joffrey.irsdkjava.model.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SessionState {

    irsdk_StateInvalid(0),
    irsdk_StateGetInCar(1),
    irsdk_StateWarmup(2),
    irsdk_StateParadeLaps(3),
    irsdk_StateRacing(4),
    irsdk_StateCheckered(5),
    irsdk_StateCoolDown(6);

    private static final Map<Integer, SessionState> sessionStateMap = new HashMap();
    private final        int                        value;

    SessionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SessionState get(int code) {
        return (SessionState) sessionStateMap.get(code);
    }

    static {
        for (SessionState sessionState : EnumSet.allOf(SessionState.class)) {
            sessionStateMap.put(sessionState.value, sessionState);
        }
    }

}
