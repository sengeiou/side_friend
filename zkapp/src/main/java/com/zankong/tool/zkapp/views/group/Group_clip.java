package com.zankong.tool.zkapp.views.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKParentAttr;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Created by YF on 2018/7/27.
 */

public class Group_clip extends ZKViewAgent {
    private ArrayList<Element> mItems,oldItems;
    private int mHeight;
    private ArrayList<View> mButtons;

    public Group_clip(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mItems = new ArrayList<>();
        oldItems = new ArrayList<>();
        mButtons = new ArrayList<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_group_clip);

    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            String name = attribute.getName();
            switch (name) {
                case "height":
                    mHeight = Util.px2px(attribute.getValue());
                    break;
                default:
                    if (name.contains("-")){
                        String childName = name.split("-")[0];
                        String childAttr = name.split("-")[1];
                        for (Element child : Util.getChildElement(selfElement, childName)) {
                            if (child.attribute(childAttr) == null)
                                child.addAttribute(childAttr,value);
                        }
                    }
            }
        }
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "button":
                    oldItems.add(element);
                    break;
            }
        }
        mItems.clear();
        for (Element oldItem : oldItems) {
            mItems.add((Element) oldItem.clone());
        }
        for (int i = 0; i < mItems.size(); i++) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_group_clip_button, ((LinearLayout) getView()), false);
            ((LinearLayout) getView()).addView(inflate);
            mButtons.add(inflate);
        }
        initButton();
    }
    private void initButton(){
        for (int i = 0; i < mButtons.size(); i++) {
            setButton(i);
        }
    }

    private void setButton(int i){
        Element element = mItems.get(i);
        View inflate = mButtons.get(i);
        ZKParentAttr zkParentAttr = new ZKParentAttr(getZKDocument(),mItems.get(i));
        zkParentAttr.setView(inflate);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) inflate.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        layoutParams.height = mHeight;
        inflate.setLayoutParams(layoutParams);
        ZKImgView centerImg = inflate.findViewById(R.id.centerImg);
        ZKPView hint = inflate.findViewById(R.id.hint);
        for (Element child : element.elements()) {
            switch (child.getName()) {
                case "img":
                    centerImg.init(getZKDocument(),child);
                    break;
                case "p":
                    hint.init(getZKDocument(),child);
                    break;
            }
        }
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "clip":
                    if ("true".equals(value)) setClip(centerImg);
                    break;
            }
        }
    }

    private void setClip(View view){
        if (view != null){
            if (view.getParent() != null){
                if (view.getParent() instanceof ViewGroup){
                    ((ViewGroup) view.getParent()).setClipChildren(false);
                    setClip((View) view.getParent());
                }
            }
        }
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            mItems.clear();
            for (Element oldItem : oldItems) {
                mItems.add((Element) oldItem.clone());
            }
            initButton();
            for (int i = 0; i < parameters.length(); i++) {
                int item = setData(parameters.getObject(i));
                setButton(item);
            }
            return V8Utils.createFrom(thisV8);
        },"init");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            for (int i = 0; i < parameters.length(); i++) {
                int item = setData(parameters.getObject(i));
                setButton(item);
            }
            return V8Utils.createFrom(thisV8);
        },"set");
    }

    //修改buttons里的子标签
    private int setData(V8Object object){
        int item = object.getInteger("item");
        Element element = mItems.get(item);
        View childAt = ((LinearLayout) getView()).getChildAt(item);
        ZKImgView centerImg = childAt.findViewById(R.id.centerImg);
        ZKPView hint = childAt.findViewById(R.id.hint);
        for (String key : object.getKeys()) {
            if ("item".equals(key))continue;
            Element child = element.element(key);
            if (child == null){
                child = DocumentHelper.createElement(key);
                element.add(child);
            }
            V8Object childObject = object.getObject(key);
            for (String s : childObject.getKeys()) {
                switch (s) {
                    case "text":
                    case "p":
                        child.setText(childObject.getString(s));
                        break;
                    default:
                        child.addAttribute(s,childObject.getString(s));
                        break;
                }
            }
        }
        return item;
    }
}
