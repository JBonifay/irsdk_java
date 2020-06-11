package com.joffrey.iracingapp.windows;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * Defines a wrapper for a Pointer to hide the JNA definition details
 */
public class Pointer {

    private final com.sun.jna.Pointer pointer;

    public Pointer(com.sun.jna.Pointer pointer) {
        this.pointer = pointer;
    }

    protected com.sun.jna.Pointer get() {
        return pointer;
    }

    public byte[] getByteArray(long offset, int arraySize) {
        return pointer.getByteArray(offset, arraySize);
    }

    public ByteBuffer getByteBuffer(long offset, long length) {
        return pointer.getByteBuffer(offset, length);
    }

    public <T extends com.sun.jna.Structure> T asStructure(Function<com.sun.jna.Pointer, T> structureCreator) {
        return structureCreator.apply(pointer);
    }


}
