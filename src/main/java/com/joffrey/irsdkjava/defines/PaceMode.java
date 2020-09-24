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

package com.joffrey.irsdkjava.defines;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PaceMode {
    irsdk_PaceModeSingleFileStart(0),
    irsdk_PaceModeDoubleFileStart(1),
    irsdk_PaceModeSingleFileRestart(2),
    irsdk_PaceModeDoubleFileRestart(3),
    irsdk_PaceModeNotPacing(4),
    ;

    private static final Map<Integer, PaceMode> paceModeMap = new HashMap();
    private final        int                        value;

    PaceMode(int value) {
        this.value = value;
    }


    public static PaceMode get(int code) {
        return (PaceMode) paceModeMap.get(code);
    }

    static {
        for (PaceMode paceMode : EnumSet.allOf(PaceMode.class)) {
            paceModeMap.put(paceMode.value, paceMode);
        }
    }
}
