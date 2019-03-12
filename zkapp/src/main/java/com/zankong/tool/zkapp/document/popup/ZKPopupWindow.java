package com.zankong.tool.zkapp.document.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YF on 2018/4/17.
 */

public class ZKPopupWindow extends PopupWindow {
    private ZKDocument mZKDocument;
    private Element mElement;
    private Context mContext;
    private String page;
    private V8Object arguments = new V8Object(ZKToolApi.runtime);


    @Override
    public void showAsDropDown(View anchor) {
        boolean isFocusable = true;
        int xoff = 0, yoff = 0,gravity;
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "position":
                    String[] position = value.split(",");
                    xoff = Util.px2px(position[0]);
                    yoff = Util.px2px(position[1]);
                    break;
                case "focusable":
                    isFocusable = Boolean.parseBoolean(value);
                    break;
                case "weight":
                    setWidth(Util.px2px(value));
                    break;
                case "width":
                    setWidth(Util.px2px(value));
                    break;
                case "height":
                    setHeight(Util.px2px(value,getZKDocument().getDisplayMetrics().heightPixels));
                    break;
                case "gravity":
                    gravity = Integer.parseInt(value);
                    break;
                case "alpha":
                    WindowManager.LayoutParams lp = ((Activity)mZKDocument.getContext()).getWindow().getAttributes();
                    lp.alpha = Float.parseFloat(value); //0.0-1.0
                    ((Activity)mZKDocument.getContext()).getWindow().setAttributes(lp);
                    break;
            }
        }
        setBackgroundFocusable(isFocusable);
        showAsDropDown(anchor, xoff, yoff);
    }

    private void setBackgroundFocusable(boolean focusable){
        setFocusable(focusable);
        setOutsideTouchable(focusable);
        setBackgroundDrawable(focusable?new ColorDrawable(0x33000000):null);
    }

    public ZKPopupWindow(Context context, String url){
        super(context);
        mContext = context;
        page = Util.getPath(url);
        mElement = Util.getElementByPath(page);
        try {
            mZKDocument = (ZKDocument) ZKDocument.docMap.get(mElement.getName().toLowerCase()).getConstructor(Context.class, Element.class, ViewGroup.class).newInstance(context,mElement,null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.createFrom(arguments);
            }
        },"arguments");
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Object dismissData = null;
                if (parameters.length() > 0) dismissData = parameters.get(0);
                if (mOnDismiss != null) mOnDismiss.dismiss(dismissData);
                dismiss();
                return null;
            }
        },"next");
    }

    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = ((Activity)mZKDocument.getContext()).getWindow().getAttributes();
        lp.alpha = 1; //0.0-1.0
        ((Activity)mZKDocument.getContext()).getWindow().setAttributes(lp);
        super.dismiss();
    }

    public void setArguments(V8Object arguments) {
        this.arguments = arguments;
    }

    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    public String getPage() {
        return page;
    }

    private OnDismiss mOnDismiss;
    public interface OnDismiss{
        void dismiss(Object obj);
    }
    public void setDismiss(OnDismiss onDismiss){
        mOnDismiss = onDismiss;
    }
}
