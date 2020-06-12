package com.joffrey.iracingapp.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import org.springframework.stereotype.Service;

@Service
public class WindowsService {

    public int lastError = 0;

    public interface Kernel32Impl extends Kernel32 {

        static final Kernel32Impl KERNEL_32 = Native.loadLibrary("kernel32",
                                                                               Kernel32Impl.class,
                                                                               W32APIOptions.DEFAULT_OPTIONS);

        HANDLE OpenFileMapping(int lfProtect, boolean bInherit, String lpName);

        HANDLE OpenEvent(int i, boolean bManualReset, String lpName);

        boolean SetConsoleTitle(String lpTitle);

        boolean PurgeComm(HANDLE hFile, WinNT.DWORD dwFlags);

        boolean EscapeCommFunction(HANDLE hFile, WinNT.DWORD dwFunc);

    }

    public Handle openMemoryMapFile(String filename) {
        WinNT.HANDLE memMapFile = Kernel32Impl.KERNEL_32.OpenFileMapping(WinNT.SECTION_MAP_READ,
                                                                          false,
                                                                          filename);
        lastError = Kernel32Impl.KERNEL_32.GetLastError();

        return memMapFile == null ? null : new Handle(memMapFile);
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
