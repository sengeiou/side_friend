package com.zankong.tool.zkapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;

import org.dom4j.Element;

/**
 * Created by YF on 2018/6/25.
 */

public class ZKFragment extends Fragment implements Releasable{
    private ZKDocument mZKDocument,parent;
    private String page = null;
    private int index = 0;
    private V8Object data;
    private boolean isShowed = false,//是否之前是显示状态
            isPrepared = false;//是否布局什么的初始化完成

    @Override
    public void release() {
        if (mZKDocument != null) mZKDocument.release();
        if (data != null)data.release();
    }

    private static ZKFragment newInstance(ZKFragmentBuilder builder){
        ZKFragment zkFragment = new ZKFragment();
        zkFragment.parent = builder.parent;
        zkFragment.index = builder.index;
        zkFragment.data = builder.data;
        zkFragment.page = builder.page;
        return zkFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ZKActivity activity = (ZKActivity) getActivity();
        View rootView;
        if (activity != null){
            assert page != null;
            Element root = Util.getElementByPath(page);
            try {
                rootView = initZKDocument(root,container);
            } catch (Exception e) {
//                e.printStackTrace();
                rootView = inflater.inflate(R.layout.main_activity_null, container, false);
            }
        }else {
            rootView = inflater.inflate(R.layout.main_activity_null, container, false);
        }
        isPrepared = true;
        return rootView;
    }

    /**
     * 生成自己的document
     */
    private View initZKDocument(Element root, ViewGroup container) throws Exception{
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
                return index;
            }
        },"getIndex");
        if (data == null){
            data = new V8Object(ZKToolApi.runtime);
        }
        mZKDocument.getDocument().add("data",data);
        mZKDocument.onCreate();
        return mZKDocument.getView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mZKDocument.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && getUserVisibleHint()){
            Util.log("fragment","self onResume");
            mZKDocument.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isHidden() && getUserVisibleHint()){
            Util.log("fragment","self onPause");
            mZKDocument.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mZKDocument.onStop();
    }

    //自己管理时调用
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Util.log("fragment","onHiddenChanged:"+hidden);
        if (hidden){
            Util.log("fragment","document onPause");
            mZKDocument.onPause();
        }else {
            Util.log("fragment","document onResume");
            mZKDocument.onResume();
        }
    }

    //viewPage管理时调用,此方法可能会在所有fragment生命周期之前被调用;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Util.log("fragment","setUserVisibleHint:"+isVisibleToUser);
        if (isVisibleToUser){
            isShowed = true;
            if (isPrepared){
                mZKDocument.onResume();
            }
        }else {
            if (isShowed){
                mZKDocument.onPause();
            }
            isShowed = false;
        }
    }
    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    //fragment构建器
    public static class ZKFragmentBuilder{
        private ZKDocument parent;
        private String page = null;
        private int index = 0;
        private V8Object data;

        public ZKFragmentBuilder(){
        }
        public ZKFragment builder(){
            return ZKFragment.newInstance(this);
        }

        public ZKFragmentBuilder setParent(ZKDocument parent) {
            this.parent = parent;
            return this;
        }

        public ZKFragmentBuilder setPage(String page) {
            this.page = page;
            return this;
        }
        public ZKFragmentBuilder setIndex(int index) {
            this.index = index;
            return this;
        }

        public ZKFragmentBuilder setData(V8Object data) {
            this.data = data;
            return this;
        }
    }

}
