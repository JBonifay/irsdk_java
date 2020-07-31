/*
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.joffrey.irsdkjava.service.windows;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
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

    public void closeHandle(HANDLE memMapFile) {
        Kernel32Impl.KERNEL_32.CloseHandle(memMapFile);
    }

    public Pointer mapViewOfFile(WinNT.HANDLE handle) {
        if (handle == null) {
            return null;
        }
        return Kernel32.INSTANCE.MapViewOfFile(handle, WinNT.SECTION_MAP_READ, 0, 0, 0);
    }

    public void unmapViewOfFile(Pointer sharedMemory) {
        Kernel32Impl.KERNEL_32.UnmapViewOfFile(sharedMemory);
        lastError = Kernel32Impl.KERNEL_32.GetLastError();
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
        if (messageId == 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return messageId;
    }

    public boolean sendNotifyMessage(int msg, int wParam, int lParam) {
        if (msg != 0) {
            boolean sent = User32Impl.USER_32.SendNotifyMessage(User32.HWND_BROADCAST,
                                                                msg,
                                                                new WinDef.WPARAM(wParam).intValue(),
                                                                new WinDef.LPARAM(lParam).intValue());
            lastError = Kernel32.INSTANCE.GetLastError();
            return sent;
        }
        return false;
    }


}
