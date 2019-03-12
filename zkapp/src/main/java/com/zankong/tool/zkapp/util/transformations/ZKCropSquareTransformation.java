package com.zankong.tool.zkapp.util.transformations;//package com.zankong.tool.zkapp.util.transformations;
//
//import android.graphics.Bitmap;
//
//import com.squareup.picasso.Transformation;
//
///**
// * Created by YF on 2018/7/6.
// */
//
///**
// * 方形图片
// */
//public class ZKCropSquareTransformation implements Transformation {
//    //截取从宽度和高度最小作为bitmap的宽度和高度
//    @Override
//    public Bitmap transform(Bitmap source) {
//        int x;
//        int size = Math.min(source.getWidth(), source.getHeight());
//
//        if (source.getWidth() >= source.getHeight()){
//            x = (source.getHeight() - size) / 2;
//        }else {
//            x = (source.getWidth() - size) / 2;
//        }
//        Bitmap result = Bitmap.createBitmap(source, x, x, size, size);
//        if (result != source) {
//            source.recycle();
//        }
//
//        return result;
//    }
//
//    @Override
//    public String key() {
//        return "crop()";
//    }
//
//}
