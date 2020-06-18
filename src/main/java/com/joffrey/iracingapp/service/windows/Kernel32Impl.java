package com.joffrey.iracingapp.service.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface Kernel32Impl extends com.sun.jna.platform.win32.Kernel32 {

    static final Kernel32Impl KERNEL_32 = Native.loadLibrary("kernel32", Kernel32Impl.class, W32APIOptions.DEFAULT_OPTIONS);

    HANDLE OpenFileMapping(int lfProtect, boolean bInherit, String lpName);

    HANDLE OpenEvent(int i, boolean bManualReset, String lpName);

    boolean SetConsoleTitle(String lpTitle);

    boolean PurgeComm(HANDLE hFile, WinNT.DWORD dwFlags);

    boolean EscapeCommFunction(HANDLE hFile, WinNT.DWORD dwFunc);

}
