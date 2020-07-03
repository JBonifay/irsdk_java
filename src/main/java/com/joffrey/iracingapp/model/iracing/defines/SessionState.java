package com.joffrey.iracingapp.model.iracing.defines;

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
