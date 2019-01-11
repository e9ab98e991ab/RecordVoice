package com.e9ab98e991ab.voice;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.e9ab98e991ab.voice.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecordListener {
    private TextView tv, tv2;
    private Button btn1, btn2;
    private SeekBar seekBar;
    private float pitchShift = 1;
    private Button btn3, btn4;
    private RecordVoiceThread rec;
    private Boolean falg = true;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        findViewById(R.id.btnChanger1).setOnClickListener(this);
        findViewById(R.id.btnChanger2).setOnClickListener(this);
        findViewById(R.id.btnChanger3).setOnClickListener(this);
        findViewById(R.id.btnChanger3).setOnClickListener(this);
        findViewById(R.id.btnChanger4).setOnClickListener(this);
        findViewById(R.id.btnChanger5).setOnClickListener(this);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitchShift = (float) progress / 100;
                tv2.setText(String.valueOf(pitchShift));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //6.0才用动态权限
        if (Build.VERSION.SDK_INT >= 23) {
            permissionUtils = new PermissionUtils(this);
            permissionUtils.initPermission();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn1:
                Recorder.getInstance().setListener(this).start(this);
                break;
            case R.id.btn2:
                Player.getInstance().setPitchShift(pitchShift).start(this);
                break;
            case R.id.btn3:
                LiveRecording.getInstance().setPitchShift(pitchShift).start(this);
                break;
            case R.id.btnChanger1:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_NORMAL);
                break;
            case R.id.btnChanger2:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_LUOLI);
                break;
            case R.id.btnChanger3:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_DASHU);
                break;
            case R.id.btnChanger4:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_JINGSONG);
                break;
            case R.id.btnChanger5:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_GAOGUAI);
                break;
            case R.id.btnChanger6:
                SoundTypeFix.fix(tv.getText().toString(),SoundTypeFix.MODE_KONGLING);
                break;

            default:
                break;
        }
    }

    @Override
    public void onComplete(String path) {
        tv.setText("path:" + path);
    }

    @Override
    public void onCancel() {
        tv.setText("path:");
    }




    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (permissionUtils.mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionUtils.showPermissionDialog();
            } else {
                //全部权限通过，可以进行下一步操作。。。

            }
        }

    }


}
