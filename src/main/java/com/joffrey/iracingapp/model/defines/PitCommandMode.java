package com.joffrey.iracingapp.model.defines;

public enum PitCommandMode {

    irsdk_PitCommand_Clear,                   // Clear all pit checkboxes
    irsdk_PitCommand_WS,                      // Clean the winshield, using one tear off
    irsdk_PitCommand_Fuel,                    // Add fuel, optionally specify the amount to add in liters or pass '0' to use existing amount
    irsdk_PitCommand_LF,                      // Change the left front tire, optionally specifying the pressure in KPa or pass '0' to use existing pressure
    irsdk_PitCommand_RF,                      // right front
    irsdk_PitCommand_LR,                      // left rear
    irsdk_PitCommand_RR,                      // right rear
    irsdk_PitCommand_ClearTires,              // Clear tire pit checkboxes
    irsdk_PitCommand_FR,                      // Request a fast repair
    irsdk_PitCommand_ClearWS,                 // Uncheck Clean the winshield checkbox
    irsdk_PitCommand_ClearFR,                 // Uncheck request a fast repair
    irsdk_PitCommand_ClearFuel,               // Uncheck add fuel

}
