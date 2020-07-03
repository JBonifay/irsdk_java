package com.joffrey.iracingapp.service.windows;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class WindowsService {

    private int lastError = 0;

    public WinNT.HANDLE openMemoryMapFile(String filename) {
        WinNT.HANDLE memMapFile = Kernel32Impl.KERNEL_32.OpenFileMapping(WinNT.SECTION_MAP_READ, false, filename);
        lastError = Kernel32Impl.KERNEL_32.GetLastError();

        return memMapFile;
    }

    public Pointer mapViewOfFile(WinNT.HANDLE handle) {
        if (handle == null) {
            return null;
        }
        return Kernel32.INSTANCE.MapViewOfFile(handle, WinNT.SECTION_MAP_READ, 0, 0, 0);
    }

    public void waitForSingleObject(WinNT.HANDLE handle, int timeout) {
        if (handle != null) {
            Kernel32.INSTANCE.WaitForSingleObject(handle, timeout);
            lastError = Kernel32.INSTANCE.GetLastError();
        }
    }

    public WinNT.HANDLE openEvent(String eventName) {
        WinNT.HANDLE handle = Kernel32Impl.KERNEL_32.OpenEvent(WinNT.SYNCHRONIZE, false, eventName);
        lastError = Kernel32.INSTANCE.GetLastError();
        return handle;
    }

    public int registerWindowMessage(final String lpString) {
        final int messageId = User32.INSTANCE.RegisterWindowMessage(lpString);
        if (messageId == 0)
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        return messageId;
    }

}
