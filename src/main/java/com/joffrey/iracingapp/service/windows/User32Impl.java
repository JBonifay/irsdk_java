package com.joffrey.iracingapp.service.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public interface User32Impl extends User32 {

    static final User32Impl USER_32 = Native.loadLibrary("user32", User32Impl.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean SendNotifyMessage(HWND hWnd, int Msg, int wParam, int lParam);

}
