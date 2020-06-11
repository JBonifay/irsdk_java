package com.joffrey.iracingapp.windows;


import com.sun.jna.platform.win32.WinNT;

/**
 * Defines a wrapper for a HANDLE to hide the Windows definition details
 */
public class Handle {

    private WinNT.HANDLE m_handle;

    public Handle(WinNT.HANDLE handle) {
        m_handle = handle;
    }

    protected WinNT.HANDLE get() {
        return m_handle;
    }
}
