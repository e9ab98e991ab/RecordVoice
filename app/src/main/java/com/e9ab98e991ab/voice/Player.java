package com.e9ab98e991ab.voice;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.e9ab98e991ab.voice.utils.FileUtil;
import com.e9ab98e991ab.voice.view.RecordViewDialog;

import java.io.File;


public class Player implements View.OnClickListener{
    private static Player instance;
    private String fileName = "temp.wav";
    private PlayerThread playerThread;
    private File file;
    private float pitchShift;
    private RecordViewDialog recordViewDialog;

    private Player() {
    }

    public static synchronized Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public Player setPitchShift(float pitchShift) {
        this.pitchShift = pitchShift;
        return instance;
    }

    public void start(Context context) {
        if (playerThread != null) {
            stop();
        }
        file = new File(FileUtil.getCacheRootFile(context), fileName);
        if (!file.exists()){
            Toast.makeText(context,"文件不存在",Toast.LENGTH_LONG).show();
            return;
        }
        recordViewDialog = new RecordViewDialog(context, R.style.Dialog, this,true);
        recordViewDialog.show();
        playerThread = new PlayerThread(file, pitchShift);
        playerThread.start();
    }

    public void stop() {
        if (playerThread != null) {
            playerThread.quit();
            playerThread = null;
            recordViewDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        recordViewDialog.dismiss();
    }
}
