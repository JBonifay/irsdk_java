package com.joffrey.iracingapp.model;

import lombok.Data;

@Data
public class VarLookup {

    private String name;
    private int memIndex;
    private int diskIndex;
    private int logMode;

}