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

public enum PitCommandMode {

    irsdk_PitCommand_Clear(0),                   // Clear all pit checkboxes
    irsdk_PitCommand_WS(1),                      // Clean the winshield, using one tear off
    irsdk_PitCommand_Fuel(2),                    // Add fuel, optionally specify the amount to add in liters or pass '0' to use existing amount
    irsdk_PitCommand_LF(3),                      // Change the left front tire, optionally specifying the pressure in KPa or pass '0' to use existing pressure
    irsdk_PitCommand_RF(4),                      // right front
    irsdk_PitCommand_LR(5),                      // left rear
    irsdk_PitCommand_RR(6),                      // right rear
    irsdk_PitCommand_ClearTires(7),              // Clear tire pit checkboxes
    irsdk_PitCommand_FR(8),                      // Request a fast repair
    irsdk_PitCommand_ClearWS(9),                 // Uncheck Clean the winshield checkbox
    irsdk_PitCommand_ClearFR(10),                 // Uncheck request a fast repair
    irsdk_PitCommand_ClearFuel(11),
    ;               // Uncheck add fuel

    private final int value;

    PitCommandMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
