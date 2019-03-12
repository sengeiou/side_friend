package com.zankong.tool.zkapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKRipple;
import com.zankong.tool.zkapp.zk_interface.ZKActivityLife;

import org.dom4j.Element;

/**
 * 控件style控制类
 * Created by YF on 2018/6/22.
 */

public abstract class ZKViewAgent implements ZKActivityLife, Releasable {
    private View mView;//自己的view
    private V8Object mThisV8;//自己的V8Object
    private ZKDocument mZKDocument;
    private Context mContext;
    private Element mElement;
    private ViewGroup mViewGroup;//父布局
    private ZKParentAttr mZKParentAttr; //父属性设置类

    public ZKViewAgent(ZKDocument zkDocument,Element element){
        mZKDocument = zkDocument;
        mElement = element;
        mZKParentAttr = new ZKParentAttr(zkDocument,element);
        mContext = zkDocument.getContext();
    }

    public void initThisV8(V8Object thisV8) {
        mThisV8 = thisV8;
        mZKParentAttr.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Util.log("val:","执行了");
                return getResult();
            }
        },"val");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object object = parameters.getObject(0);
                for (String s : object.getKeys()) {
                    mElement.addAttribute(s,object.getString(s));
                }
                setParentAttr(mElement,mView);
                return null;
            }
        },"set");
    }

    //重写初始化布局的方法
    public abstract void initView(ViewGroup viewGroup);
    //解析element
    public abstract void fillData(Element selfElement);
    //val方法的返回值
    public abstract Object getResult();

    //给跟布局设置父属性
    protected void setParentAttr(Element selfElement,View view){
        mZKParentAttr.setView(view);
        mView.setBackground(ZKRipple.getRipple(mContext,mElement).getDrawable());
    }

    //get,set方法
    public void setViewGroup(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public View getView() {
        if (mView == null){
            throw new RuntimeException("view不存在:"+mElement.asXML());
        }
        return mView;
    }

    public V8Object getThisV8() {
        return mThisV8;
    }

    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    public Context getContext() {
        return mContext;
    }

    public Element getElement() {
        return mElement;
    }

    //快捷方法
    protected View findViewById(int id){
        return mView.findViewById(id);
    }

    protected void setContentView(int layout){
        setContentView(LayoutInflater.from(mContext).inflate(layout, mViewGroup, false));
    }
    protected void setContentView(View view){
        mView = view;
    }

    @Override
    public void onCreate() {

    }

    //生命周期
    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public boolean onBack(){
        return true;
    }

    @Override
    public void release() {

    }

}
