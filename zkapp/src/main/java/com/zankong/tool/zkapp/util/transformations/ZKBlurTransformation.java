package com.zankong.tool.zkapp.util.transformations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by YF on 2018/7/6.
 */

/**
 * 模糊图片
 */
public class ZKBlurTransformation extends BitmapTransformation {
    private RenderScript rs;
    private int blur;

    public ZKBlurTransformation(Context context,int blur) {
        rs = RenderScript.create(context);
        this.blur = blur;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        if (blur == 0){
            return toTransform;
        }
        // 创建一个Bitmap作为最后处理的效果Bitmap
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

        // 分配内存
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // 根据我们想使用的配置加载一个实例
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs));
        script.setInput(input);

        // 设置模糊半径
        script.setRadius(blur);

        //开始操作
        script.forEach(output);

        // 将结果copy到blurredBitmap中
        output.copyTo(blurredBitmap);

        //释放资源
//        toTransform.recycle();
        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
