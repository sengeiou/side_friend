package com.zankong.tool.zkapp.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ViewUtil;
import com.zankong.tool.zkapp.util.ZKRipple;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Iterator;

/**
 * Created by GC on 2017/7/28.
 */
public class ZKPView extends AppCompatTextView implements ZKBaseView {
    private ZKParentAttr mZKParentAttribute;
    private ZKDocument mZKDocument;
    private Element mElement,oldElement;
    private V8Object mThisV8;

    public ZKPView(Context context) {
        super(context);
    }

    public ZKPView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKPView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void init(ZKDocument zkDocument,Element element){
        mThisV8 = new V8Object(ZKToolApi.runtime);
        init(zkDocument,element,mThisV8);
    };

    @Override
    public void init(ZKDocument zkDocument,Element element,V8Object v8Object){
        mZKDocument = zkDocument;
        oldElement = element;
        mElement = (Element) oldElement.clone();
        mZKParentAttribute = new ZKParentAttr(zkDocument,element);
        mThisV8 = v8Object;
        mZKParentAttribute.initThisV8(v8Object);
        initThisV8(mZKParentAttribute.getThisV8());
        fillData();
        setBackground(ZKRipple.getRipple(getContext(),element).getDrawable());
    }

    //解析element
    private void fillData(){
        mZKParentAttribute.setView(this);
        setText(mElement);
        mZKDocument.getActivity().setOnReleaseListeners(new ZKNaiveActivity.OnReleaseListener() {
            @Override
            public void release() {
                ZKPView.this.release();
            }
        });
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "gravity":
                    int gravity = 0x0;
                    for (String s : value.split("\\|")) {
                        Util.log("dialog",s);
                        gravity = gravity|ViewUtil.getGravityMap.get(s);
                    }
                    setGravity(gravity);
                    break;
            }
        }
    }

    //让p标签支持内置p标签
    private SpannableStringBuilder getSpannableString(Element element){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Attribute val = element.attribute("val");
        if ( val!= null){
            V8Function v8Function = mZKDocument.genContextVal(val.getValue());
            String text = (String) mZKDocument.invokeWithContext(v8Function);
            spannableStringBuilder.append(setSpan(element,text));
            Util.log("TEXT",text);
            v8Function.release();
            element.remove(val);
            return spannableStringBuilder;
        }
        Iterator<Node> iterator = element.nodeIterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            switch (node.getNodeTypeName().toLowerCase()){
                case "text":
                    spannableStringBuilder.append(setSpan(element,node.getText()));
                    break;
                case "element":
                    Element childElement = (Element) node;
                    Attribute valAttr = childElement.attribute("val");
                    if (valAttr != null) {
                        V8Function v8Function = mZKDocument.genContextVal(valAttr.getValue());
                        String text = (String) mZKDocument.invokeWithContext(v8Function);
                        spannableStringBuilder.append(setSpan(childElement,text));
                        Util.log("TEXT",text);
                        v8Function.release();
                        childElement.remove(valAttr);
                    }else {
                        spannableStringBuilder.append(getSpannableString(childElement));
                    }
                    break;
                default:
                    Util.log("node type",node.getNodeTypeName()+":"+node.getNodeType());
                    break;
            }
        }
        return spannableStringBuilder;
    }
    private SpannableString setSpan(Element element,String text){
        SpannableString spannableString = new SpannableString(text);
        final int length = spannableString.length();
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            Object what = null;
            switch (attribute.getName().toLowerCase()) {
                case "underline":
                    what = new UnderlineSpan();
                    break;
                case "font":
                    for (String font : value.split(",")) {
                        if (font.contains("#")) {
                            what = new ForegroundColorSpan(Color.parseColor(font));
                            spannableString.setSpan(what,0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }else {
                            what = new AbsoluteSizeSpan(Util.px2px(font));
                            spannableString.setSpan(what,0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    break;
                case "color":
                    what = new ForegroundColorSpan(Color.parseColor(value));
                    break;
                case "background":
                    what = new BackgroundColorSpan(Color.parseColor(value));
                    break;
                case "size":
                    what = new AbsoluteSizeSpan(Util.px2px(value));
                    break;
                case "delete":
                    what = new StrikethroughSpan();
                    break;
                case "superscript":
                    what = new SuperscriptSpan();
                    break;
                case "subscript":
                    what = new SubscriptSpan();
                    break;
                case "textstyle":
                    what = new StyleSpan(ViewUtil.getTextStyle.get(value));
                    break;
            }
            spannableString.setSpan(what,0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public void setText(Element element){
        setText(getSpannableString(element));
    }

    @Override
    public void release() {
        if (mThisV8 != null) {
            mThisV8.release();
        }
    }

    @Override
    public V8Object getThisV8() {
        return mThisV8;
    }

    @Override
    public View getBaseView() {
        return this;
    }

    //添加js方法
    protected void initThisV8(V8Object object){
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.get(0) instanceof String){
                    setText(parameters.getString(0));
                }else if (parameters.get(0) instanceof V8Object){
                    V8Object object = parameters.getObject(0);
                    setText(object);
                }
                return null;
            }
        },"set");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return getText().toString();
            }
        },"val");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                init();
                if(parameters.length() > 0){
                    if (parameters.get(0) instanceof String){
                        setText(parameters.getString(0));
                    }else if (parameters.get(0) instanceof V8Object){
                        V8Object object = parameters.getObject(0);
                        setText(object);
                    }
                }
                return null;
            }
        },"init");
        object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return getText().toString();
            }
        },"get");
    }

    //通过V8Object设置p标签
    public void setText(V8Object object){
        setText(createElement(object));
    }

    public Element createElement(V8Object object){
        for (String s : object.getKeys()) {
            switch (s) {
                case "text":
                    mElement.setText(object.getString(s));
                    break;
                case "p":
                    mElement.setText(object.getString(s));
                    break;
                default:
                    mElement.addAttribute(s,object.getString(s));
            }
        }
        return mElement;
    }

    public void init(){
        mElement = (Element) oldElement.clone();
        setText(mElement);
    }

    public void setFaceText(String data){
        setFaceText(data,"#\\[[^\\]#]+\\]#");
    }
    public void setFaceText(String data,String zhengze){
        setText(Util.getExpressionString(getContext(), data, zhengze));
    }
}
