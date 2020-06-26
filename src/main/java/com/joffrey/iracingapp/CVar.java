package com.joffrey.iracingapp;

import lombok.Data;

@Data
public class CVar {

    private String name;
    private int idx = -1;
    private int statusID = -1;
    private int entry = 0;

    public CVar(String name) {
        this.name = name;
    }
}
