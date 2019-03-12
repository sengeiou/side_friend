package com.zankong.tool.zkapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eclipsesource.v8.Releasable;
import com.zankong.tool.zkapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZKNaiveActivity extends AppCompatActivity implements Releasable {
    public static int requestCode100 = 100;


    private OnResultListener mResultListener = null;
    public interface OnResultListener{
        void result(int requestCode, int resultCode, @Nullable Intent data);
    };
    public void setResultListener(OnResultListener listener){
        mResultListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnReleaseListeners = new ArrayList<>();
    }

    @Override
    public void release() {
        for (OnReleaseListener onReleaseListener : mOnReleaseListeners) {
            onReleaseListener.release();
        }
        mOnReleaseListeners.clear();
    }

    /**
     * 传值
     */
    public interface OnForResult {
        void result(int requestCode, int resultCode, @Nullable Intent data);
    }
    private OnForResult mForResult;
    public void openForResult(Intent intent,OnForResult forResult) {
        startActivityForResult(intent,requestCode100);
        mForResult = forResult;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mForResult != null)
            mForResult.result(requestCode, resultCode, data);
        if(mResultListener != null)
            mResultListener.result(requestCode, resultCode, data);
    }

    /**
     * 申请权限
     */
    public interface PermissionListener {
        void granted();
        void denied(List<String> deniedList);
    }
    int i=  0;
    private HashMap<Integer,PermissionListener> listenerHashMap = new HashMap<>();
    public void requestRuntimePermissions(String[] permissions, PermissionListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            listener.granted();

            return;
        }
     

        final int code = i++;
        listenerHashMap.put(code,listener);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE}, code);

        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                listener.granted();
            }
        }

        // 申请权限
        if (!permissionList.isEmpty()) {


        }
    }

    /**
     * 申请后的处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            List<String> deniedList = new ArrayList<>();
            // 遍历所有申请的权限，把被拒绝的权限放入集合
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    listenerHashMap.get(requestCode).granted();
                } else {
                    deniedList.add(permissions[i]);
                }
            }
            if (!deniedList.isEmpty()) {
                listenerHashMap.get(requestCode).denied(deniedList);
            }
            if (listenerHashMap.containsKey(requestCode))
                listenerHashMap.remove(requestCode);
        }
    }


    /**
     * release监听
     */
    public interface OnReleaseListener{
        void release();
    }
    private ArrayList<OnReleaseListener> mOnReleaseListeners;
    public void setOnReleaseListeners(OnReleaseListener releaseListeners){
        mOnReleaseListeners.add(releaseListeners);
    };
}
