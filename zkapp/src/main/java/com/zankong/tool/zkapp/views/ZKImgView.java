package com.zankong.tool.zkapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.transformations.ZKBlurTransformation;
import com.zankong.tool.zkapp.util.transformations.ZKCircleTransform;
import com.zankong.tool.zkapp.util.transformations.ZKScaleTransformation;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.File;


/**
 * Created by GC on 2017/7/28.
 */
public class ZKImgView extends AppCompatImageView implements ZKBaseView{
    private ZKParentAttr mZKParentAttribute;
    private Element mElement,oldElement;
    private ZKDocument mZKDocument;
    private V8Object mThisV8;
    private RequestOptions options;

    public ZKImgView(Context context) {
        super(context);
    }

    public ZKImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(ZKDocument zkDocument,Element element){
        mThisV8 = new V8Object(ZKToolApi.runtime);
        init(zkDocument,element,mThisV8);
    };

    @Override
    public void init(ZKDocument zkDocument,Element element,V8Object v8Object){
        oldElement = element;
        mElement = (Element) oldElement.clone();
        mZKDocument = zkDocument;
        initCorner();
        mZKParentAttribute = new ZKParentAttr(zkDocument,element);
        mZKParentAttribute.initThisV8(v8Object);
        initThisV8(mZKParentAttribute.getThisV8());
        fillData();
    }

    private void analysisElement(){
        String val = mZKParentAttribute.setView(this);
        String scale = "centerCrop";
        int radius = 0,blur = 0;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "src"://图片路径
                    if (val == null)
                        val = value;
                    break;
                case "blur"://模糊度
                    blur = Util.px2px(value);
                    break;
                case "attr"://图片大小
                    String[] mAttr = value.split(",");
                    if (mAttr.length == 1) {
                        layoutParams.width = Util.px2px(mAttr[0]);
                        layoutParams.height = Util.px2px(mAttr[0]);
                    } else {
                        layoutParams.width = Util.px2px(mAttr[0]);
                        layoutParams.height = Util.px2px(mAttr[1]);
                    }
                    break;
                case "radius"://圆角
                    radius = Util.px2px(value);
                    break;
                case "corner"://角标
                    cornerTest = value;
                    cornerBackgroundSize = 20;
                    break;
                case "scale"://图片伸缩类型
                    scale = value;
                    break;
            }
        }
        //添加角标
        Element cornerElement = mElement.element("corner");
        if (cornerElement != null){
            cornerBackgroundSize = 20;
            for (Attribute attribute : cornerElement.attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName().toLowerCase()) {
                    case "font":
                        for (String s : value.split(",")) {
                            if (s.contains("#")){
                                cornerTextPaint.setColor(Color.parseColor(s));
                            }else {
                                cornerTextPaint.setTextSize(Util.px2px(s));
                            }
                        }
                        break;
                    case "background":
                        cornerBackgroundPaint.setColor(Color.parseColor(value));
                        break;
                    case "size":
                        cornerBackgroundSize = Util.px2px(value);
                        break;
                    case "position":
                        position = Util.px2px(value);
                        break;
                }
            }
            cornerTest = cornerElement.getTextTrim();
        }
        if(val != null || !"".equals(val)){
            options = new RequestOptions().skipMemoryCache(false).dontAnimate().transforms(
                    new ZKScaleTransformation(scale),
                    new ZKBlurTransformation(getContext(),blur),
                    new ZKCircleTransform(cornerBackgroundSize,radius,position)
            );
            Glide.with(getContext()).load(getUrl(val)).apply(options).into(this);
        }
        setLayoutParams(layoutParams);
    }


    public void fillData(){
        analysisElement();
        mZKDocument.getActivity().setOnReleaseListeners(new ZKNaiveActivity.OnReleaseListener() {
            @Override
            public void release() {
                ZKImgView.this.release();
            }
        });
    }

    /**
     * 角标相关
     */
    private int cornerBackgroundSize = 0; //角标背景大小
    private Paint cornerBackgroundPaint;//角标背景画笔
    private String cornerTest = "";//角标文字
    private Paint cornerTextPaint;//角标文字画笔
    private int position = 1;

    /**
     * 角标相关
     */
    private void initCorner(){
        cornerBackgroundPaint = new Paint();
        cornerBackgroundPaint.setAntiAlias(true);
        cornerBackgroundPaint.setStyle(Paint.Style.FILL);
        cornerBackgroundPaint.setColor(Color.parseColor("#ff4444"));
        cornerTextPaint = new Paint();
        cornerTextPaint.setAntiAlias(true);
        cornerTextPaint.setStyle(Paint.Style.FILL);
        cornerTextPaint.setColor(Color.parseColor("#ffffff"));
        cornerTextPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (cornerTest) {
            case "":
                break;
            case "point":
            case ".":
                drawCircle(canvas);
                break;
            default:
                drawCircle(canvas);
                drawText(canvas);
        }
    }

    private void drawCircle(Canvas canvas){
        switch (position){
            case 2:
                canvas.drawCircle(cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundPaint);
                break;
            case 3:
                canvas.drawCircle(cornerBackgroundSize, getHeight() - cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundPaint);
                break;
            case 4:
                canvas.drawCircle(getWidth() - cornerBackgroundSize, getHeight() - cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundPaint);
                break;
            case 1:
            default:
                canvas.drawCircle(getWidth() - cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundSize, cornerBackgroundPaint);
        }
    }
    private void drawText(Canvas canvas){
        float textWidth = cornerTextPaint.measureText(cornerTest);
        float baseLineY = Math.abs(cornerTextPaint.ascent() + cornerTextPaint.descent()) / 2;
        switch (position){
            case 2:
                canvas.drawText(cornerTest,cornerBackgroundSize - textWidth/2, cornerBackgroundSize + baseLineY,cornerTextPaint);
                break;
            case 3:
                canvas.drawText(cornerTest,cornerBackgroundSize - textWidth/2, getHeight() - cornerBackgroundSize + baseLineY,cornerTextPaint);
                break;
            case 4:
                canvas.drawText(cornerTest,getWidth() - cornerBackgroundSize - textWidth/2, getHeight() - cornerBackgroundSize + baseLineY,cornerTextPaint);
                break;
            case 1:
            default:
                canvas.drawText(cornerTest,getWidth() - cornerBackgroundSize - textWidth/2, cornerBackgroundSize + baseLineY,cornerTextPaint);
        }
    }

    protected void initThisV8(V8Object object){
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"src");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                boolean noCorner1 = "".equals(cornerTest);
                if (parameters.length() == 0){
                }else {
                    if (parameters.get(0) instanceof Boolean){
                        if (parameters.getBoolean(0)) {
                            cornerTest = "point";
                        }else {
                            cornerTest = "";
                        }
                    } else if (parameters.get(0) instanceof Integer){
                        cornerTest = parameters.getInteger(0) +"";
                    }else if (parameters.get(0) instanceof String){
                        cornerTest = parameters.getString(0);
                    }else if (parameters.get(0) instanceof  V8Object){
                        V8Object cornerData = parameters.getObject(0);
                        for (String s : cornerData.getKeys()) {
                            switch (s) {
                                case "text":
                                    cornerTest = cornerData.get(s)+"";
                                    break;
                                case "textSize":
                                    cornerTextPaint.setTextSize(Util.px2px(s));
                                    break;
                                case "textColor":
                                    cornerTextPaint.setColor( Color.parseColor(cornerData.getString(s)));
                                    break;
                                case "backgroundSize":
                                    cornerBackgroundSize = cornerData.getInteger(s);
                                    break;
                                case "backgroundColor":
                                    cornerBackgroundPaint.setColor(Color.parseColor(cornerData.getString(s)));
                                    break;
                            }
                        }
                    }
                }
                boolean noCorner2 = "".equals(cornerTest);
                invalidate();
                return null;
            }
        },"corner");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                cornerTest = "";
                invalidate();
                return null;
            }
        },"clearCorner");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.get(0) instanceof V8Object) {
                    V8Object attr = parameters.getObject(0);
                    for (String key : attr.getKeys()) {
                        switch (key) {
                            case "src":
                                Glide.with(getContext()).load(getUrl(attr.getString(key))).apply(options).into(ZKImgView.this);
                                break;
                        }
                    }
                }else if (parameters.get(0) instanceof String) {
                    Glide.with(getContext()).load(parameters.getString(0)).apply(options).into(ZKImgView.this);
                }else if (parameters.get(0) instanceof Integer){
                    setImageBitmap(mZKDocument.mBitmapHashMap.get(parameters.getInteger(0)));
                }
                return null;
            }
        },"set");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                fillData();
                return null;
            }
        },"init");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int key = (int) (Math.random() * 100000000);
                mZKDocument.mBitmapHashMap.put(key,Util.drawable2Bitmap(getDrawable()));
                return key;
            }
        },"get");
    }

    public static String getUrl(String fileName){
        if (fileName == null)return null;
        if (fileName.contains("http://") || fileName.contains("https://")) {
            return fileName;
        }else if (fileName.contains("/storage/")){
            return new File(fileName).getAbsolutePath();
        }else {
            return Util.getPath("file:///android_asset/" + fileName);
        }
    }

    public void setImg(Element element) {
        mElement = element;
        analysisElement();
    }

    @Override
    public V8Object getThisV8() {
        return mThisV8;
    }

    @Override
    public View getBaseView() {
        return this;
    }

    @Override
    public void release() {
        if (mThisV8 != null)mThisV8.release();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
