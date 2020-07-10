package com.joffrey.iracingapp.model.defines;

public enum PitCommandMode {

    irsdk_PitCommand_Clear(0),                   // Clear all pit checkboxes
    irsdk_PitCommand_WS(1),                      // Clean the winshield, using one tear off
    irsdk_PitCommand_Fuel(2),                    // Add fuel, optionally specify the amount to add in liters or pass '0' to use existing amount
    irsdk_PitCommand_LF(3),                      // Change the left front tire, optionally specifying the pressure in KPa or pass '0' to use existing pressure
    irsdk_PitCommand_RF(4),                      // right front
    irsdk_PitCommand_LR(5),                      // left rear
    irsdk_PitCommand_RR(6),                      // right rear
    irsdk_PitCommand_ClearTires(7),              // Clear tire pit checkboxes
    irsdk_PitCommand_FR(8),                      // Request a fast repair
    irsdk_PitCommand_ClearWS(9),                 // Uncheck Clean the winshield checkbox
    irsdk_PitCommand_ClearFR(10),                 // Uncheck request a fast repair
    irsdk_PitCommand_ClearFuel(11),
    ;               // Uncheck add fuel

    private int value;

    PitCommandMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
