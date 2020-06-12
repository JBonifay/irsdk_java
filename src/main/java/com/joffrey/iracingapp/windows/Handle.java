package com.joffrey.iracingapp.windows;


import com.sun.jna.platform.win32.WinNT;

/**
 * Defines a wrapper for a HANDLE to hide the Windows definition details
 */
public class Handle {

    private WinNT.HANDLE handle;

    public Handle(WinNT.HANDLE handle) {
        this.handle = handle;
    }

    protected WinNT.HANDLE get() {
        return handle;
    }
}
