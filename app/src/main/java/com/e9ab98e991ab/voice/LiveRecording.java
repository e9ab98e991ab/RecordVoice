package com.e9ab98e991ab.voice;

import android.content.Context;
import android.view.View;
import com.e9ab98e991ab.voice.view.RecordViewDialog;


public class LiveRecording implements View.OnClickListener{
    private static LiveRecording instance;
    private RecordVoiceThread recordVoiceThread;
    private float pitchShift;
    private RecordViewDialog recordViewDialog;

    private LiveRecording() {
    }

    public static synchronized LiveRecording getInstance() {
        if (instance == null) {
            instance = new LiveRecording();
        }
        return instance;
    }

    public LiveRecording setPitchShift(float pitchShift) {
        this.pitchShift = pitchShift;
        return instance;
    }

    public void start(Context context) {
        if (recordVoiceThread != null) {
            stop();
        }

        recordViewDialog = new RecordViewDialog(context, R.style.Dialog, this,true);
        recordViewDialog.show();
        recordVoiceThread = new RecordVoiceThread(pitchShift);
        recordVoiceThread.start();
    }

    public void stop() {
        if (recordVoiceThread != null) {
            recordVoiceThread.quit();
            recordVoiceThread = null;
            recordViewDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        recordViewDialog.dismiss();
    }
}
