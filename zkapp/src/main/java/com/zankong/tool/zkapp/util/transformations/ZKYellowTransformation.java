package com.zankong.tool.zkapp.util.transformations;//package com.zankong.tool.zkapp.util.transformations;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
//
//import com.squareup.picasso.Transformation;
//
///**
// * Created by YF on 2018/7/6.
// */
//
///**
// * 泛黄图片
// */
//public class ZKYellowTransformation implements Transformation {
//    @Override
//    public Bitmap transform(Bitmap source) {
//        Bitmap output = Bitmap.createBitmap(source.getWidth(),
//                source.getHeight(), Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(output);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        float[] array = { 1, 0, 0, 0, 100, 0, 1, 0, 0, 100, 0, 0, 1, 0, 0, 0,
//                0, 0, 1, 0 };
//        cm.set(array);
//        paint.setColorFilter(new ColorMatrixColorFilter(cm));
//        canvas.drawBitmap(source, 0, 0, paint);
//        source.recycle();
//        return output;
//    }
//
//    @Override
//    public String key() {
//        return "yellow()";
//    }
//
//}
