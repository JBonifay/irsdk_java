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

public enum RpySrchMode {

    irsdk_RpySrch_ToStart(0),
    irsdk_RpySrch_ToEnd(1),
    irsdk_RpySrch_PrevSession(2),
    irsdk_RpySrch_NextSession(3),
    irsdk_RpySrch_PrevLap(4),
    irsdk_RpySrch_NextLap(5),
    irsdk_RpySrch_PrevFrame(6),
    irsdk_RpySrch_NextFrame(7),
    irsdk_RpySrch_PrevIncident(8),
    irsdk_RpySrch_NextIncident(9),
    irsdk_RpySrch_Last(10)            // unused placeholder
    ;

    private final int value;

    RpySrchMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
