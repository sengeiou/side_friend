package com.zankong.tool.zkapp.v8fn;

import android.content.Context;
import android.os.Environment;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.math.BigDecimal;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKCache implements ZKV8Fn {
    private V8Object Cache;


    @Override
    public void addV8Fn() {
        Cache = new V8Object(ZKToolApi.runtime);
        ZKToolApi.runtime.add("Cache",Cache);
        Cache.registerJavaMethod((receiver, parameters) -> {
            try {
                return getCacheSize(ZKToolApi.getInstance().getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "0";
        },"size");
        Cache.registerJavaMethod((receiver, parameters) -> {
            clearAllCache(ZKToolApi.getInstance().getContext());
            return null;
        },"clear");
    }

    /**
     * @param context 删除缓存
     */
    public void clearAllCache(Context context) {
        Util.deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Util.deleteDir(context.getExternalCacheDir());
        }
    }



    /**
     * 获得缓存大小
     */
    private String getCacheSize(Context context) throws Exception {
        long cacheSize = Util.getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cacheSize += Util.getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 格式化单位
     * 计算缓存的大小
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
