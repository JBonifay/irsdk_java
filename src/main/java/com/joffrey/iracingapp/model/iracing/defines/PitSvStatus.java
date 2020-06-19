package com.joffrey.iracingapp.model.iracing.defines;

public enum PitSvStatus {
    // status
    irsdk_PitSvNone,
    irsdk_PitSvInProgress,
    irsdk_PitSvComplete,

    // errors
    irsdk_PitSvTooFarLeft,
    irsdk_PitSvTooFarRight,
    irsdk_PitSvTooFarForward,
    irsdk_PitSvTooFarBack,
    irsdk_PitSvBadAngle,
    irsdk_PitSvCantFixThat,
}
