package com.joffrey.iracingapp.windows;

/* Copyright (c) 2007, 2013 Timothy Wall, Markus Karg, All Rights Reserved
 *
 * The contents of this file is dual-licensed under 2
 * alternative Open Source/Free licenses: LGPL 2.1 or later and
 * Apache License 2.0. (starting with JNA version 4.0.0).
 *
 * You can freely decide which license you want to apply to
 * the project.
 *
 * You may obtain a copy of the LGPL License at:
 *
 * http://www.gnu.org/licenses/licenses.html
 *
 * A copy is also included in the downloadable source code package
 * containing JNA, in file "LGPL2.1".
 *
 * You may obtain a copy of the Apache License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing JNA, in file "AL2.0".
 */


import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Wincon;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Interface definitions for <code>kernel32.dll</code>. Includes additional alternate mappings from {@link WinNT} which make use
 * of NIO buffers, {@link Wincon} for console API.
 */
public interface Kernel32 extends StdCallLibrary, WinNT, Wincon {

    /**
     * The instance.
     */
    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

    HANDLE OpenFileMapping(int dwDesiredAccess, boolean bInheritHandle, String lpName);

}
