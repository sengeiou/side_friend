package com.example.zkapp_map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zankong.tool.zkapp.ZKToolApi;

import static com.example.zkapp_map.MapOne.LOCATION_CODE;

/**
 * @author Fsnzzz
 * @Created on 2018/9/20 0020 16:30
 */

public class UtilesMap {
    /**
     * @author Fsnzzz
     * @time 2018/9/20 0020  17:01
     * @describe 地图坐标，两点间的距离
     */
    public static double getDistance(LatLng latLng1, LatLng latLng2) {
        return DistanceUtil.getDistance(latLng1, latLng2);
    }
    /**
     *  @author Fsnzzz
     *  @time 2018/9/21 0021  14:58
     *  @describe 开启定位权限
     */

    //        public static boolean getUserPermission(){
    //            if (showCheckPermissions()){
    //
    //
    //            }
    //        }

    /**
     * @author Fsnzzz
     * @time 2018/9/21 0021  14:58
     * @describe 判断是否需要开启权限
     */
    public static boolean showCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Fsnzzz
     * @time 2018/9/25 0025  21:51
     * @describe baiduMap定位
     */
    public static void getLocation() {


    }


    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  0:27
     * @describe 将View转换成Bitmap
     */
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }



}