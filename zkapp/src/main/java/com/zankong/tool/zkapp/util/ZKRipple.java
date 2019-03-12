package com.zankong.tool.zkapp.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;

import com.zankong.tool.zkapp.R;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/5.
 */

public class ZKRipple {
    private Context             mContext;
    private int                 color,
                                 strokeColor,
                                 strokeSize,
                                 solid;
    private float[]             radius;
    private boolean             isRipple;

    private Drawable mDrawable;

    private ZKRipple(Builder builder){
        this.mContext = builder.mContext;
        this.color = builder.color;
        this.solid = builder.solid;
        this.strokeColor = builder.strokeColor;
        this.strokeSize = builder.strokeSize;
        this.radius = builder.radius;
        this.isRipple = builder.isRipple;
    }

    /**
     * 根据传来的element参数解析背景属性
     */
    public static ZKRipple getRipple(Context context,Element element){
        Builder builder = new Builder();
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "ripple"://点击效果颜色
                    builder.setRipple(true);
                    if (!"".equals(value)){
                        builder.setColor(Color.parseColor(value));
                    }
                    break;
                case "background"://背景色
                    if (value.contains("#")) {
                        builder.setSolid(Color.parseColor(value));
                    }
                    break;
                case "radius"://圆角
                    String[] radius = value.split(",");
                    if (radius.length == 1){
                        builder.setRadius(Float.parseFloat(radius[0]));
                    }else {
                        builder.setRadius(
                                Float.parseFloat(radius[0]),
                                Float.parseFloat(radius[1]),
                                Float.parseFloat(radius[2]),
                                Float.parseFloat(radius[3])
                        );
                    }
                    break;
                case "border"://边框线
                    String[] split = value.split(",");
                    for (String s : split) {
                        if (s.contains("#")) {
                            builder.setStrokeColor(Color.parseColor(s));
                        }else {
                            builder.setStrokeSize(Util.px2px(s));
                        }
                    }
                    break;
            }
        }
        return builder.build(context);
    }

    /**
     * 根据api和ripple属性不同判断获得哪种drawable
     */
    public Drawable getDrawable(){
        if (isRipple)
            return getRippleDrawable();
        else
            return getGradientDrawable();
    }

    /**
     * 获得shape背景
     */
    private GradientDrawable getGradientDrawable(){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(strokeSize,strokeColor);
        gradientDrawable.setCornerRadii(radius);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(solid);
        return gradientDrawable;
    }

    /**
     * 获得ripple背景
     */
    private Drawable getRippleDrawable(){
        GradientDrawable gradientDrawable = getGradientDrawable();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mDrawable = new RippleDrawable(
                    ColorStateList.valueOf(color),//灰色波纹
                    gradientDrawable,//一个圆角5dp,白色填充,灰色边框的shape图形,在xml中定义的
                    gradientDrawable//一个圆角5dp,白色填充,灰色边框的shape图形,在xml中定义的
            );
        }else {
            mDrawable = mContext.getResources().getDrawable(R.drawable.zk_background_ripple_no);
        }
        return mDrawable;
    }

    /**
     * 创建者模式
     */
    public static class Builder{
        private int color = Color.GRAY,strokeColor,strokeSize,solid = Color.parseColor("#00000000");
        private float[] radius =new float[]{0,0,0,0,0,0,0,0};
        private Context mContext;
        private boolean isRipple = false;
        public Builder(){

        }
        public ZKRipple build(Context context){
            mContext = context;
            return new ZKRipple(this);
        }

        public Builder setRipple(Boolean isRipple){
            this.isRipple = isRipple;
            if (isRipple){
                solid = Color.parseColor("#ffffffff");
            }
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setStrokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public Builder setStrokeSize(int strokeSize) {
            this.strokeSize = strokeSize;
            return this;
        }

        public Builder setSolid(int solid) {
            this.solid = solid;
            return this;
        }

        public Builder setRadius(float radius1,float radius2,float radius3,float radius4) {
            this.radius[0] = radius1;
            this.radius[1] = radius1;
            this.radius[2] = radius2;
            this.radius[3] = radius2;
            this.radius[4] = radius3;
            this.radius[5] = radius3;
            this.radius[6] = radius4;
            this.radius[7] = radius4;
            return this;
        }
        public Builder setRadius(float radius) {
            this.radius[0] = radius;
            this.radius[1] = radius;
            this.radius[2] = radius;
            this.radius[3] = radius;
            this.radius[4] = radius;
            this.radius[5] = radius;
            this.radius[6] = radius;
            this.radius[7] = radius;
            return this;
        }
    }
}
