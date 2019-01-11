package com.e9ab98e991ab.voice.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import com.e9ab98e991ab.voice.BuildConfig;


import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    //申请两个权限，录音和文件读写
    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();
    //权限请求码
    public int mRequestCode = 100;

    private Activity mActivity;

    public PermissionUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //权限判断和申请
    public void initPermission() {
        //清空没有通过的权限
        mPermissionList.clear();

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //添加还未授予的权限
                mPermissionList.add(permissions[i]);
            }
        }

        //申请权限
        //有权限没有通过，需要申请
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(mActivity, permissions, mRequestCode);

        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }
    }

    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;

    public void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(mActivity)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();

                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    public void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

}
