package com.zankong.tool.zkapp.v8fn;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKVersion implements ZKV8Fn {
    private V8Object version;
    private DownloadManager mManager;
    public static final String     MINE_TYPE = "application/vnd.android.package-archive";
    private Context mContext;
    private boolean isAuto = false,isLoading = false;
    private Long                     downLoadId;
    private V8Function loadComplete,loadFail;




    public ZKVersion() {
        version = new V8Object(ZKToolApi.runtime);
        mContext = ZKToolApi.getInstance().getContext();
        mManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void addV8Fn() {
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                PackageInfo packageInfo = null;
                try {
                    packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (packageInfo == null){
                    return null;
                }
                V8Object info = new V8Object(ZKToolApi.runtime);
                info.add("name",packageInfo.versionName);
                info.add("code",packageInfo.versionCode);
                info.add("lastUpdateTime",packageInfo.lastUpdateTime);
                return info;
            }
        },"");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                downLoadId  = Long.valueOf(parameters.getString(0));
                return apkFile(downLoadId);
            }
        },"isLoaded");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                final String apkUrl = parameters.getString(0);
                downLoadId = Long.valueOf(parameters.getString(1));
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        if (isLoading){
                            resolve.call(null,"isLoading");
                            return;
                        }
                        Log.d(TAG,"downLoadId:"+downLoadId);
                        if (downLoadId != 0){
                            if (isAuto){
                                Log.d(TAG,"start installApk:");
                                installApk(downLoadId);
                            }else {
                                Log.d(TAG,"start resolve:");
                                resolve.call(null,downLoadId);
                                Log.d(TAG,"end resolve:");
                            }
                            return;
                        }
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            Log.d(TAG,"进入6.0权限判断");
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            for (String permission : permissions) {
                                if (mContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                                    Log.d(TAG,"读写权限没有");
                                }else {
                                    Log.d(TAG,"读写权限有");
                                }
                            }
                        }
                        loadComplete = resolve;
                        loadFail = reject;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl))
                                .setMimeType(MINE_TYPE)
                                .setTitle("test")
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "app.apk" );
                        downLoadId = mManager.enqueue(request);
                        AppDownReceiver appDownReceiver = new AppDownReceiver();
                        IntentFilter inflater = new IntentFilter();
                        inflater.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        inflater.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
                        mContext.registerReceiver(appDownReceiver,inflater);
                        isLoading = true;
                    }
                });
            }
        },"downLoad");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return installApk(Math.round(parameters.getDouble(0)));
            }
        },"install");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return isLoading;
            }
        },"isLoading");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                isAuto = parameters.getBoolean(0);
                return null;
            }
        },"setAuto");
        version.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                downLoadId = Long.valueOf(parameters.getString(0));
                query(downLoadId);
                return null;
            }
        },"query");
    }

    private Object query(long downLoadId){
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = mManager.query(query.setFilterById(downLoadId));
        String file = "";
        if (cursor != null && cursor.moveToFirst()) {
            file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        }
        return file;
    }
    private Boolean apkFile(long id){
        if (id == 0){
            return false;
        }
        File file = new File(mManager.getMimeTypeForDownloadedFile(id));
        if (file.exists()){
            return true;
        }
        return false;
    };

    private Boolean installApk(long downLoadId){
        try{
            if (downLoadId == 0){
//                mEditor.putLong(APK_DOWNLOAD_ID,0);
                return false;
            }
            Uri fileUri = mManager.getUriForDownloadedFile(downLoadId);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                intent.setDataAndType(fileUri,MINE_TYPE);
            }else {
                intent.setDataAndType(fileUri,MINE_TYPE);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class AppDownReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                isLoading = false;
                if (isAuto){
                    installApk(downLoadId);
                }else {
                    if (loadComplete != null) loadComplete.call(null,downLoadId);
                }
            }else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
                if (loadFail != null)loadFail.call(null,downLoadId);
            }
        }
    }
}
