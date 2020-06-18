package com.joffrey.iracingapp.service.windows;

import com.joffrey.iracingapp.model.windows.Handle;
import com.joffrey.iracingapp.model.windows.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import org.springframework.stereotype.Service;

@Service
public class WindowsService {

    public int lastError = 0;

    public Handle openMemoryMapFile(String filename) {
        WinNT.HANDLE memMapFile = Kernel32Impl.KERNEL_32.OpenFileMapping(WinNT.SECTION_MAP_READ, false, filename);
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

    public int registerWindowMessage(final String lpString) {
        final int messageId = User32.INSTANCE.RegisterWindowMessage(lpString);
        if (messageId == 0)
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        return messageId;
    }

}
