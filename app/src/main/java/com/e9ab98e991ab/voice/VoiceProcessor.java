package com.e9ab98e991ab.voice;


public class VoiceProcessor {
    static {
        System.loadLibrary("smbPitchShift");
    }

    public static synchronized byte[] dispose(float pitchShift, int sampleRate, int size, byte[] in) {
        byte[] out = new byte[size];
        dispose(pitchShift, sampleRate, size, in, out, new float[size / 2], new float[size / 2]);
        return out;
    }

    private static native void dispose(float pitchShift, int sampleRate, int size, byte[] in, byte[] out, float[] floatIn, float[] floatOut);
}
