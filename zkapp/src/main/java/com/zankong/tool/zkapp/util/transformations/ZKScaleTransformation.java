package com.zankong.tool.zkapp.util.transformations;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Created by YF on 2018/7/18.
 */

public class ZKScaleTransformation extends BitmapTransformation {
    private String scale;

    public ZKScaleTransformation(String scale){
        this.scale = scale;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap source = null;
        switch (scale){
            case "centerCrop":
                source = TransformationUtils.centerCrop(pool,toTransform,outWidth,outHeight);
                break;
            case "centerInside":
                source = TransformationUtils.centerInside(pool,toTransform,outWidth,outHeight);
                break;
            case "fitCenter":
                source = TransformationUtils.fitCenter(pool,toTransform,outWidth,outHeight);
                break;
            case "circleCrop":
                source = TransformationUtils.circleCrop(pool,toTransform,outWidth,outHeight);
                break;
            default:
                source = TransformationUtils.centerCrop(pool,toTransform,outWidth,outHeight);
        }
        return source;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
