package com.e9ab98e991ab.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class PlayerThread extends Thread {
    private static final String TAG = Player.class.getSimpleName();
    private static final int SAMPLE_RATE_INHZ = 8000;
    private boolean running;
    private AudioTrack audioTrack;
    private int bufSize;
    private DataInputStream dis;
    private float pitchShift;

    public PlayerThread(File file, float pitchShift) {
        this.pitchShift = pitchShift;
        bufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_INHZ, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_INHZ,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize * 2, AudioTrack.MODE_STREAM);

        try {
            dis = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            running = true;
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            byte[] tempBuffer = new byte[bufSize];
            int readCount = 0;
            while (dis.available() > 0 && running) {
                readCount = dis.read(tempBuffer);
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (readCount != 0 && readCount != -1) {
                    audioTrack.play();

                    SoundTypeFix.fix(String.valueOf(readCount), SoundTypeFix.MODE_NORMAL);
                   // audioTrack.write(pitchShift == 1.0f ? tempBuffer : VoiceProcessor.dispose(pitchShift, SAMPLE_RATE_INHZ, bufSize, tempBuffer), 0, readCount);
                }
            }
            release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        running = false;
    }

    private void release() {
        audioTrack.stop();
        audioTrack.release();
        audioTrack = null;
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "stop");
    }

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }
}
