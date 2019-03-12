package com.zankong.tool.zkapp.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by YF on 2018/7/10.
 */

public class ViewUtil {

    /**
     * view方位集合
     */
    public static HashMap<String, Integer> getGravityMap = new HashMap<String, Integer>() {{
        put("center", Gravity.CENTER);
        put("left", Gravity.START);
        put("right", Gravity.END);
        put("bottom", Gravity.BOTTOM);
        put("top", Gravity.TOP);
    }};

    /**
     * 字体大小,颜色设置
     */
    public static void setFont(String font, TextView view) {
        for (String s : font.split(",")) {
            if (s.contains("#")) {
                view.setTextColor(Color.parseColor(s));
            } else {
                view.setTextSize(Util.px2px(s));
            }
        }
    }

    /**
     * input输入类型设置
     */
    public static HashMap<String, Integer> getInputTypeInt = new HashMap<String, Integer>() {{
        put("null",0x000);
        put("text",0x001);
        put("number",0x010);
        put("password",0x100);
    }};
    public static HashMap<Integer,Integer> getInputType = new HashMap<Integer,Integer>(){{
        put(0x000,InputType.TYPE_NULL);
        put(0x001,InputType.TYPE_CLASS_TEXT);
        put(0x010,InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
        put(0x011,InputType.TYPE_CLASS_TEXT);
        put(0x110,InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
        put(0x101,InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        put(0x100,InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        put(0x111,InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }};


    /**
     * 图片伸缩集合
     */
    public static HashMap<String, ImageView.ScaleType> getImgScaleType = new HashMap<String, ImageView.ScaleType>() {{
        put("center".toLowerCase(), ImageView.ScaleType.CENTER);
        put("centerCrop".toLowerCase(), ImageView.ScaleType.CENTER_CROP);
        put("centerInside".toLowerCase(), ImageView.ScaleType.CENTER_INSIDE);
        put("fitCenter".toLowerCase(), ImageView.ScaleType.FIT_CENTER);
        put("fitEnd".toLowerCase(), ImageView.ScaleType.FIT_END);
        put("fitStart".toLowerCase(), ImageView.ScaleType.FIT_START);
        put("fitXY".toLowerCase(), ImageView.ScaleType.FIT_XY);
        put("fitMatrix".toLowerCase(), ImageView.ScaleType.MATRIX);
    }};


    /**
     * 字体样式
     */
    public static HashMap<String, Integer> getTextStyle = new HashMap<String, Integer>() {{
        put("bold", Typeface.BOLD);
        put("normal", Typeface.NORMAL);
        put("italic", Typeface.ITALIC);
        put("bold|italic", Typeface.BOLD_ITALIC);
        put("italic|bold", Typeface.BOLD_ITALIC);
    }};
}
