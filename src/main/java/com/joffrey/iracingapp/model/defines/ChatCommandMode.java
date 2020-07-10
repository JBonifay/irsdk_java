package com.joffrey.iracingapp.model.defines;

public enum ChatCommandMode {

    irsdk_ChatCommand_Macro(0),            // pass in a number from 1-15 representing the chat macro to launch
    irsdk_ChatCommand_BeginChat(1),        // Open up a new chat window
    irsdk_ChatCommand_Reply(2),            // Reply to last private chat
    irsdk_ChatCommand_Cancel (3)           // Close chat window
    ;

    private final int value;

    ChatCommandMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
