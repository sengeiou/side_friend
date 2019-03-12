package com.zankong.tool.zkapp.util.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.zankong.tool.zkapp.util.Util;

import java.security.MessageDigest;

/**
 * Created by YF on 2018/7/6.
 */

/**
 * 圆形图片
 */
public class ZKCircleTransform extends BitmapTransformation {
    private int cornerSize = 0;
    private int radius = 0;
    private int position = 1;

    public ZKCircleTransform(int cornerSize,int radius,int position) {
        this.cornerSize = cornerSize;
        this.radius = radius;
        this.position = position;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        //画板
        Bitmap bitmap = Bitmap.createBitmap(width, height, toTransform.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);//创建同尺寸的画布
        paint.setAntiAlias(true);//画笔抗锯齿
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        //画圆角背景
        RectF rectF = null;
        switch (position){
            case 2:
                rectF = new RectF(new Rect(cornerSize, cornerSize, width, height));//赋值
                break;
            case 3:
                rectF = new RectF(new Rect(cornerSize, 0, width, height-cornerSize));//赋值
                break;
            case 4:
                rectF = new RectF(new Rect(0, 0, width-cornerSize, height-cornerSize));//赋值
                break;
            case 1:
            default:
                rectF = new RectF(new Rect(0, cornerSize, width-cornerSize, height));//赋值
        }
        canvas.drawRoundRect(rectF, radius, radius, paint);//画圆角矩形
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(toTransform, -cornerSize, cornerSize, paint);
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
