package com.zankong.tool.zkapp.views.rating;

import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/11.
 */

public class Rating1 extends ZKViewAgent {

    private V8Function mRatingWatcher;
    private ZKRatingBar mRatingBar;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Rating1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_rating_1);
        mRatingBar = ((ZKRatingBar) findViewById(R.id.ratingBar));
    }

    @Override
    public void fillData(Element viewElement) {
        String val = null,star = null;
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "star":
                    star = value;
                    break;
                case "count":
                    mRatingBar.setStarCount(Util.getInt(value));
                    break;
                case "clickable":
                    mRatingBar.setmClickable(Boolean.parseBoolean(value));
                    break;
                case "empty":
                    break;
                case "imgheight":
                    mRatingBar.setStarImageHeight(Float.parseFloat(value));
                    break;
                case "imgwidth":
                    mRatingBar.setStarImageWidth(Float.parseFloat(value));
                    break;
                case "padding":
                    mRatingBar.setImagePadding(Float.parseFloat(value));
                    break;
                case "val":
                    try{
                        V8Function v8Function = getZKDocument().genContextVal(value);
                        val = (String) getZKDocument().invokeWithContext(v8Function);
                        v8Function.release();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
        for (Element element : viewElement.elements()) {
            switch (element.getName()) {
                case "empty":
                    mRatingBar.setStarEmptyDrawable(getContext().getResources().getDrawable(R.drawable.star));
                    break;
                case "half":
                    mRatingBar.halfStar(true);
                    mRatingBar.setStarHalfDrawable(getContext().getResources().getDrawable(R.drawable.no_1));
                    break;
                case "fill":
                    mRatingBar.setStarFillDrawable(getContext().getResources().getDrawable(R.drawable.splash));
                    break;
            }
        }
        mRatingBar.init(getContext());
        if(val != null){
            mRatingBar.setStar(Float.parseFloat(val));
        }else if(star != null){
            mRatingBar.setStar(Float.parseFloat(star));
        }
    }

    @Override
    public Object getResult() {
        return mRatingBar.getZKStar();
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        mRatingBar.setOnRatingChangeListener(
                                new ZKRatingBar.OnRatingChangeListener() {
                                    @Override
                                    public void onRatingChange(float RatingCount) {
                                        if (mRatingWatcher != null) {
                                            mRatingWatcher.call(null,RatingCount);
                                        }
                                    }
                                }
                        );
                        mRatingWatcher = resolve;
                    }
                });
            }
        },"click");
    }

    @Override
    public void release() {
        super.release();
        if (mRatingWatcher != null)mRatingWatcher.release();
    }
}
