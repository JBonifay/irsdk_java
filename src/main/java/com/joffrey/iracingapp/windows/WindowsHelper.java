package com.joffrey.iracingapp.windows;

import com.joffrey.iracingapp.irsdk.IrsdkDefines;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import org.springframework.stereotype.Component;

@Component
public class WindowsHelper {

    public int lastError = 0;

    public interface Kernel32Impl extends Kernel32 {

        Kernel32Impl instance = (Kernel32Impl) Native.loadLibrary("kernel32", Kernel32Impl.class, W32APIOptions.DEFAULT_OPTIONS);

        HANDLE openFileMapping(int lfProtect, boolean bInherit, String lpName);

        HANDLE OpenEvent(int i, boolean bManualReset, String lpName);

        boolean SetConsoleTitle(String lpTitle);

        boolean PurgeComm(HANDLE hFile, WinNT.DWORD dwFlags);

        boolean EscapeCommFunction(HANDLE hFile, WinNT.DWORD dwFunc);

    }

    public Handle openMemoryMapFile() {
        WinNT.HANDLE hMemMapFile = Kernel32Impl.instance.openFileMapping(WinNT.SECTION_MAP_READ,
                                                                         false,
                                                                         IrsdkDefines.IRSDK_MEMMAPFILENAME);
        return hMemMapFile == null ? null : new Handle(hMemMapFile);
    }

    public Pointer mapViewOfFile(Handle handle) {
        if (handle == null) {
            return null;
        }

        com.sun.jna.Pointer pointer = Kernel32.INSTANCE.MapViewOfFile(handle.get(), WinNT.SECTION_MAP_READ, 0, 0, 0);

        return pointer == null ? null : new Pointer(pointer);
    }

    public void waitForSingleObject(Handle h, int timeout) {
        if (h != null) {
            Kernel32.INSTANCE.WaitForSingleObject(h.get(), timeout);
            lastError = Kernel32.INSTANCE.GetLastError();
        }
    }

    public Handle openEvent(String eventName) {
        WinNT.HANDLE h = Kernel32.INSTANCE.OpenEvent(WinNT.SYNCHRONIZE, false, eventName);
        lastError = Kernel32.INSTANCE.GetLastError();
        return h == null ? null : new Handle(h);
    }


}
