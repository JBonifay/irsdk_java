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

package com.joffrey.irsdkjava.model;

import com.joffrey.irsdkjava.service.iracing.IrsdkClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CVarService {

    private final IrsdkClient irsdkClient;

    private boolean checkIdx(CVar cVar) {
        if (irsdkClient.isConnected()) {
            if (cVar.getStatusID() != irsdkClient.getStatusID()) {
                cVar.setStatusID(irsdkClient.getStatusID());
                cVar.setIdx(irsdkClient.getVarIdx(cVar.getName()));
            }
            return true;
        }
        return false;
    }

    public int getType(CVar cVar) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarType(cVar.getIdx());
        }
        return 0;
    }

    public int getCount(CVar cVar) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarCount(cVar.getIdx());
        }
        return 0;
    }

    public boolean isValid(CVar cVar) {
        checkIdx(cVar);
        return cVar.getIdx() > -1;
    }

    public boolean getBoolean(CVar cVar) {
        return getBoolean(cVar, 0);
    }

    public boolean getBoolean(CVar cVar, int entry) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarBoolean(cVar.getIdx(), entry);
        }
        return false;
    }

    public int getInt(CVar cVar) {
        return getInt(cVar,0);
    }

    public int getInt(CVar cVar, int entry) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarInt(cVar.getIdx(), entry);
        }
        return 0;
    }

    public float getFloat(CVar cVar) {
        return getFloat(cVar, 0);
    }

    public float getFloat(CVar cVar, int entry) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarFloat(cVar.getIdx(), entry);
        }
        return 0.0f;
    }

    public double getDouble(CVar cVar) {
        return getDouble(cVar, 0);
    }

    public double getDouble(CVar cVar, int entry) {
        if (checkIdx(cVar)) {
            return irsdkClient.getVarDouble(cVar.getIdx(), entry);
        }
        return 0.0;
    }


}
