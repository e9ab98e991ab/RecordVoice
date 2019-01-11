package com.e9ab98e991ab.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Looper;
import android.util.Log;

import com.e9ab98e991ab.voice.utils.FileUtil;

/**
 * @author gaoxin 19-1-8 上午11:56
 * @version V1.0.0
 * @name RecordVoiceThread
 * @mail godfeer@aliyun.com
 * @description  录音直接播放
 */
public class RecordVoiceThread extends Thread {
    /**
     * 频率
     */
    private static final int FREQUENCY = 44100;
    /**
     * 通道配置
     */
    private static final int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    /**
     * 音频编码
     */
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    /*
     * 录音
     */
    private AudioRecord audioRecord;
    /*
     * 音轨
     */
    private AudioTrack audioTrack;

    /***
     * 是否可以录音 true 可以录音
     */
    public  boolean recordEnable = false;


    private float pitchShift;
    public RecordVoiceThread(float pitchShift){
        this.pitchShift = pitchShift;
    }

    @Override
    public  void run() {
        if (!FileUtil.validateMicAvailability()) {
            Log.e(this.getName().getClass() + "", "设备占用！");
            return;
        }
        int recBufSize = AudioRecord.getMinBufferSize(FREQUENCY,
                CHANNEL_CONFIGURATION, AUDIO_ENCODING) * 2;

        int plyBufSize = AudioTrack.getMinBufferSize(FREQUENCY,
                CHANNEL_CONFIGURATION, AUDIO_ENCODING) * 2;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY,
                CHANNEL_CONFIGURATION, AUDIO_ENCODING, recBufSize);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY,
                CHANNEL_CONFIGURATION, AUDIO_ENCODING, plyBufSize, AudioTrack.MODE_STREAM);

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        byte[] recBuf = new byte[recBufSize];
        try {
            audioRecord.startRecording();
            audioTrack.play();
            recordEnable = true;

            while (recordEnable) {
                int readLen = audioRecord.read(recBuf, 0, recBufSize);

                byte[] read = pitchShift == 1.0f ? recBuf : VoiceProcessor.dispose(pitchShift, FREQUENCY, recBufSize, recBuf);
//
                 audioTrack.write(read, 0, readLen);
            }
            release();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void release() {
        audioTrack.stop();
        audioRecord.stop();

        recordEnable = false;

        audioTrack.release();
        audioRecord.release();

        audioTrack = null;
    }
    public void quit() {
        recordEnable = false;
    }

    public boolean stopAudio() {
        try {
            audioTrack.stop();
            audioRecord.stop();

            recordEnable = false;

            audioTrack.release();
            audioRecord.release();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}

