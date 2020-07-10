package com.joffrey.iracingapp.model.defines;

// You can call this any time
public enum FFBCommandMode {

    irsdk_FFBCommand_MaxForce(0),          // Set the maximum force when mapping steering torque force to direct input units (float in Nm)
    irsdk_FFBCommand_Last      (1)         // unused placeholder
    ;

    private final int value;

    FFBCommandMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
