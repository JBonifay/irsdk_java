package com.joffrey.irsdkjava.sdk.defines;/*
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


public enum TrkSurf {
    irsdk_SurfaceNotInWorld(-1),
    irsdk_UndefinedMaterial(0),

    irsdk_Asphalt1Material(1),
    irsdk_Asphalt2Material(2),
    irsdk_Asphalt3Material(3),
    irsdk_Asphalt4Material(4),
    irsdk_Concrete1Material(5),
    irsdk_Concrete2Material(6),
    irsdk_RacingDirt1Material(7),
    irsdk_RacingDirt2Material(8),
    irsdk_Paint1Material(9),
    irsdk_Paint2Material(10),
    irsdk_Rumble1Material(11),
    irsdk_Rumble2Material(12),
    irsdk_Rumble3Material(13),
    irsdk_Rumble4Material(14),

    irsdk_Grass1Material(15),
    irsdk_Grass2Material(16),
    irsdk_Grass3Material(17),
    irsdk_Grass4Material(18),
    irsdk_Dirt1Material(19),
    irsdk_Dirt2Material(20),
    irsdk_Dirt3Material(21),
    irsdk_Dirt4Material(22),
    irsdk_SandMaterial(23),
    irsdk_Gravel1Material(24),
    irsdk_Gravel2Material(25),
    irsdk_GrasscreteMaterial(26),
    irsdk_AstroturfMaterial(27),
    ;


    private final int value;

    TrkSurf(int value) {
        this.value = value;
    }

    public static String valueOf(int value) {
        for (TrkSurf t : TrkSurf.values()) {
            if (t.value == value) {
                return t.toString();
            }
        }
        return TrkSurf.irsdk_SurfaceNotInWorld.toString();

    }

}
