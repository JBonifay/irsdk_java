package com.joffrey.iracingapp.model.defines;

public enum ChatCommandMode {

    irsdk_ChatCommand_Macro,            // pass in a number from 1-15 representing the chat macro to launch
    irsdk_ChatCommand_BeginChat,        // Open up a new chat window
    irsdk_ChatCommand_Reply,            // Reply to last private chat
    irsdk_ChatCommand_Cancel            // Close chat window

}
