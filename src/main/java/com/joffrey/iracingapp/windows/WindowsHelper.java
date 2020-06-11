package com.joffrey.iracingapp.windows;

import com.joffrey.iracingapp.irsdk.IrsdkDefines;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import org.springframework.stereotype.Component;

@Component
public class WindowsHelper {

    public interface Kernel32Impl extends Kernel32 {

        Kernel32Impl instance = (Kernel32Impl) Native.loadLibrary("kernel32", Kernel32Impl.class, W32APIOptions.DEFAULT_OPTIONS);

        HANDLE OpenFileMapping(int lfProtect, boolean bInherit, String lpName);

        HANDLE OpenEvent(int i, boolean bManualReset, String lpName);

        boolean SetConsoleTitle(String lpTitle);

        boolean PurgeComm(HANDLE hFile, WinNT.DWORD dwFlags);

        boolean EscapeCommFunction(HANDLE hFile, WinNT.DWORD dwFunc);

    }

    public Handle openMemoryMapFile() {
        WinNT.HANDLE hMemMapFile = Kernel32Impl.instance.OpenFileMapping(WinNT.SECTION_MAP_READ,
                                                                         false,
                                                                         IrsdkDefines.IRSDK_MEMMAPFILENAME);
        return hMemMapFile == null ? null : new Handle(hMemMapFile);
    }

    public Pointer mapViewOfFile(Handle handle) {
        if (handle == null) {
            return null;
        }

        com.sun.jna.Pointer pointer = Kernel32.INSTANCE.MapViewOfFile(handle.get(),
                                                                         WinNT.SECTION_MAP_READ,
                                                                         0,
                                                                         0,
                                                                         0);


        return pointer == null ? null : new Pointer(pointer);
    }

}
