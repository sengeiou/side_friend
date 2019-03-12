package com.zankong.tool.zkapp.util.zkpermission;//package com.zankong.tool.zkapp.util.zkpermission;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.widget.Toast;
//
///**
// * Created by YF on 2018/7/3.
// */
//
//public class ZKPermission {
//    private Activity mActivity;
//    private Context mContext;
//
//    void init(){
//        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
//        } else {
//            mActivity.navigateToActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), (resultCode, data) -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    String path = Dom.saveBitmap(bitmap, file);
//                    resolve.call(null, path);
//                    resolve.release();
//                    reject.release();
//                }
//            });
//        }
//
//        mActivity.setOnMyRequestPermissionsResult((requestCode, permissions, grantResults) -> {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                //  ActivityCompat.requestPermissions(((MainActivity) mContext), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO}, 1);
//                Toast.makeText(mContext, "相机权限不足,请预先设置权限(相机,录音,读取存储)!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            mActivity.navigateToActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), new NaiveActivity.ActivityReceiveResult() {
//                @Override
//                public void process(int resultCode, Intent data) {
//                    if (resultCode == Activity.RESULT_OK) {
//                        Bundle bundle = data.getExtras();
//                        Bitmap bitmap = (Bitmap) bundle.get("data");
//                        String path = Dom.saveBitmap(bitmap, file);
//                        resolve.call(null, path);
//                        resolve.release();
//                        reject.release();
//                    }
//                }
//            });
//        });
//    }
//}
