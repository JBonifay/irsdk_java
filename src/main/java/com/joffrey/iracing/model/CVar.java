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

package com.joffrey.iracing.model;

import com.joffrey.iracing.service.iracing.Client;
import lombok.Data;

@Data
public class CVar {

    private final Client client;
    private final String name;
    private       int    idx      = -1;
    private       int    statusID = -1;

    public CVar(Client client, String name) {
        this.client = client;
        this.name = name;
    }

    private boolean checkIdx() {
        if (client.isConnected()) {
            if (this.statusID != client.getStatusID()) {
                this.statusID = client.getStatusID();
                this.idx = client.getVarIdx(getName());
            }
            return true;
        }
        return false;
    }

    public int getType() {
        if (checkIdx()) {
            return client.getVarType(this.idx);
        }
        return 0;
    }

    public int getCount() {
        if (checkIdx()) {
            return client.getVarCount(this.idx);
        }
        return 0;
    }

    public boolean isValid() {
        checkIdx();
        return this.idx > -1;
    }

    public boolean getBoolean() {
        return getBoolean(0);
    }

    public boolean getBoolean(int entry) {
        if (checkIdx()) {
            return client.getVarBoolean(this.idx, entry);
        }
        return false;
    }

    public int getInt() {
        return getInt(0);
    }

    public int getInt(int entry) {
        if (checkIdx()) {
            return client.getVarInt(this.idx, entry);
        }
        return 0;
    }

    public float getFloat() {
        return getFloat(0);
    }

    public float getFloat(int entry) {
        if (checkIdx()) {
            return client.getVarFloat(this.idx, entry);
        }
        return 0.0f;
    }

    public double getDouble() {
        return getDouble(0);
    }

    public double getDouble(int entry) {
        if (checkIdx()) {
            return client.getVarDouble(this.idx, entry);
        }
        return 0.0;
    }


}
