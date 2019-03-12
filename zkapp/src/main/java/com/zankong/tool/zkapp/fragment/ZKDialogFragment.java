package com.zankong.tool.zkapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ViewUtil;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/4.
 */

public class ZKDialogFragment extends DialogFragment implements Releasable {
    private ZKDocument mZKDocument,parent;
    private String page = "";
    private Object dismissData;
    private V8Object arguments;
    private Element mElement;

    @Override
    public void release() {
        if (mZKDocument != null) mZKDocument.release();
    }

    /**
     * 消失回调
     */
    private OnDismiss mOnDismiss;
    public interface OnDismiss{
        void dismiss(Object obj);
    }
    public void setOnDismiss(OnDismiss onDismiss){
        mOnDismiss = onDismiss;
    }

    /**
     * 设置arguments
     */
    public void setArguments(V8Object arguments) {
        this.arguments = arguments;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化函数,page:xml路径;parent:父类的document
     */
    public static ZKDialogFragment newInstance(String page, ZKDocument parent){
        Bundle bundle = new Bundle();
        bundle.putString("page",page);
        ZKDialogFragment zkFragment = new ZKDialogFragment();
        zkFragment.setParent(parent);
        zkFragment.setArguments(bundle);
        return zkFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ZKActivity activity = (ZKActivity) getActivity();
        View rootView;
        if (activity != null){
            assert getArguments() != null;
            page = getArguments().getString("page");
            Element root = Util.getElementByPath(page);
            try {
                rootView = initZKDocument(root,container);
            } catch (Exception e) {
                e.printStackTrace();
                rootView = inflater.inflate(R.layout.main_activity_null, container, false);
            }
        }else {
            rootView = inflater.inflate(R.layout.main_activity_null, container, false);
        }
        return rootView;
    }

    /**
     * 设置父类document
     */
    private void setParent(ZKDocument parent) {
        this.parent = parent;
    }

    /**
     * 生成自己的document
     */
    private View initZKDocument(Element root, ViewGroup container) throws Exception{
        mElement = root;
        mZKDocument = (ZKDocument) ZKDocument.docMap.get(root.getName().toLowerCase()).getConstructor(Context.class,Element.class, ViewGroup.class).newInstance(getActivity(),root,container);
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.createFrom(parent.getDocument());
            }
        },"getParent");
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.length() > 0){
                    dismissData = parameters.get(0);
                }
                dismiss();
                return null;
            }
        },"next");
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.createFrom(arguments);
            }
        },"arguments");
        mZKDocument.getDocument().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                dismiss();
                return null;
            }
        },"finish");
        mZKDocument.onCreate();
        return mZKDocument.getView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        assert getActivity() != null;
        Dialog dialog = getDialog();
        if (dialog == null)return;
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0f000000")));//背景颜色一定要有，看自己需求
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = (int) (dm.heightPixels * 0.76);
        int width = (int) (dm.widthPixels * 0.9);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "height":
                    height = Util.px2px(value,dm.heightPixels);
                    attributes.height = height;
                    break;
                case "width":
                    width = Util.px2px(value,dm.widthPixels);
                    attributes.width = width;
                    Util.log(width+"ffffffffffffffff");
                    Util.log(dm.widthPixels+"ffffffffffffffff");
                    break;
                case "gravity":
                    int gravity = 0x0;
                    for (String s : value.split("\\|")) {
                        Util.log("dialog",s);
                        gravity = gravity|ViewUtil.getGravityMap.get(s);
                    }
                    attributes.gravity = gravity;
                    break;
                case "position":
                    String[] position = value.split(",");
                    attributes.x = Util.px2px(position[0]);
                    attributes.y = Util.px2px(position[1]);
                    break;
                case "dimAmount":
                    attributes.dimAmount = Float.parseFloat(value);
                    break;
                case "cancel":
                    setCancelable(Boolean.parseBoolean(value));
                    break;
            }
        }
        dialog.getWindow().setAttributes(attributes);
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onResume() {
        super.onResume();
        V8Function onResume = (V8Function) mZKDocument.getDocument().getObject("onResume");
        if (onResume != null) {
            onResume.call(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        V8Function onDestroy = (V8Function) mZKDocument.getDocument().getObject("onDestroy");
        if (onDestroy != null) {
            onDestroy.call(null);
        }
        if (mOnDismiss != null) mOnDismiss.dismiss(dismissData);
    }

    @Override
    public void onStop() {
        super.onStop();
        V8Function onStop = (V8Function) mZKDocument.getDocument().getObject("onStop");
        if (onStop != null) {
            onStop.call(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        V8Function onPause = (V8Function) mZKDocument.getDocument().getObject("onPause");
        if (onPause != null) {
            onPause.call(null);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        V8Function onResume = (V8Function) mZKDocument.getDocument().getObject("onResume");
        if (onResume != null) {
            onResume.call(null);
        }
    }

    public ZKDocument getZKDocument(){
        return mZKDocument;
    }
}
