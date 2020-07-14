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

public enum Flags {
    // global flags
    irsdk_checkered(0x00000001),
    irsdk_white(0x00000002),
    irsdk_green(0x00000004),
    irsdk_yellow(0x00000008),
    irsdk_red(0x00000010),
    irsdk_blue(0x00000020),
    irsdk_debris(0x00000040),
    irsdk_crossed(0x00000080),
    irsdk_yellowWaving(0x00000100),
    irsdk_oneLapToGreen(0x00000200),
    irsdk_greenHeld(0x00000400),
    irsdk_tenToGo(0x00000800),
    irsdk_fiveToGo(0x00001000),
    irsdk_randomWaving(0x00002000),
    irsdk_caution(0x00004000),
    irsdk_cautionWaving(0x00008000),

    // drivers black flags
    irsdk_black(0x00010000),
    irsdk_disqualify(0x00020000),
    irsdk_servicible(0x00040000), // car is allowed service (not a flag)
    irsdk_furled(0x00080000),
    irsdk_repair(0x00100000),

    // start lights
    irsdk_startHidden(0x10000000),
    irsdk_startReady(0x20000000),
    irsdk_startSet(0x40000000),
    irsdk_startGo(0x80000000),
    ;

    private int value;

    Flags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
