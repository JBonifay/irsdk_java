package com.joffrey.iracingapp;

import com.joffrey.iracingapp.service.iracing.Client;
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
