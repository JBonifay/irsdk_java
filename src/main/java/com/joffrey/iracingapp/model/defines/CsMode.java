package com.joffrey.iracingapp.model.defines;

// irsdk_BroadcastCamSwitchPos or irsdk_BroadcastCamSwitchNum camera focus defines
// pass these in for the first parameter to select the 'focus at' types in the camera system.
public enum CsMode {

    irsdk_csFocusAtIncident(-3),
    irsdk_csFocusAtLeader(-2),
    irsdk_csFocusAtExiting(-1),

    // ctFocusAtDriver + car number...
    irsdk_csFocusAtDriver(0),
    ;

    CsMode(int value) {

    }
}
