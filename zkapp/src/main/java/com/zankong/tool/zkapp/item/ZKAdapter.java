package com.zankong.tool.zkapp.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.zk_interface.ZKActivityLife;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YF on 2018/6/26.
 */

public abstract class ZKAdapter extends RecyclerView.Adapter implements Releasable,ZKActivityLife {
    protected ArrayList<V8Object>           mList;                             //数据
    private Context                         mContext;                          //java上下文
    private V8Object                        mThisV8;                           //item自身的V8Object
    protected Element                       mElement;                          //item的xml元素
    protected LayoutInflater                mLayoutInflater;
    protected int                           mSelectPosition;                   //点击项
    protected V8                            runtime = ZKToolApi.runtime;   //js运行环境
    protected V8Function                    onClickJs,onLongClickJs;           //点击事件和长按事件
    protected V8Object                      document;                          //js上下文document
    private ZKDocument                      mZKDocument;                       //加载此适配器的document
    public static HashMap<String,Class<?>> adapterMap = new HashMap<>();      //已有适配器集合
    private ArrayList<View>                 mHeaderViews;                      //头布局列表
    private ArrayList<View>                 mFooterViews;                      //尾布局列表
    private String                          id;
    private final int isHeader = -10000,isFooter = 10000;

    private HashMap<String, String> clickMap = new HashMap<>();
    protected HashMap<String, String> getClickMap(){
        return clickMap;
    }

    public ZKAdapter(ZKDocument zKDocument,Element element){
        mElement = element;
        mZKDocument = zKDocument;
        init();
        for (Attribute attribute : element.elements().get(0).attributes()) {
            clickMap.put(attribute.getName(), attribute.getValue());
        }
    }

    /**
     * 初始化
     */
    private void init(){
        mContext = mZKDocument.getContext();
        document = mZKDocument.getDocument();
        mList = new ArrayList<>();
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(mContext);
        if (mElement != null){
            for (Attribute attribute : mElement.elements().get(0).attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName().toLowerCase()) {
                    case "click":
                        onClickJs = mZKDocument.genContext(value);
                        break;
                    case "longclick":
                        onLongClickJs = mZKDocument.genContext(value);
                        break;
                    case "id":
                        id = value;
                        break;
                }
            }
        }
        initV8this();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType <= isHeader){
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(mHeaderViews.get(isHeader - viewType));
            headerViewHolder.setIsRecyclable(false);
            return headerViewHolder;
        }
        if(viewType >= isFooter){
            return new FooterViewHolder(mFooterViews.get(viewType - isFooter));
        }
        return onCreate(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position < mHeaderViews.size() || position >= mHeaderViews.size() + mList.size())return;
        viewHolder.itemView.setOnClickListener(view -> {
            mSelectPosition = setSelectPosition(viewHolder);
            if (onClickJs != null && !onClickJs.isReleased()) {
                mZKDocument.invokeWithContext(onClickJs);
            }
        });
        viewHolder.itemView.setOnLongClickListener(view -> {
            mSelectPosition = setSelectPosition(viewHolder);
            if (onLongClickJs != null && !onLongClickJs.isReleased()) {
                mZKDocument.invokeWithContext(onLongClickJs);
            }
            return true;
        });
        onBind(viewHolder,position - mHeaderViews.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mHeaderViews.size())
            return isHeader-position;
        if(position >= mHeaderViews.size() + mList.size())
            return isFooter + position - mHeaderViews.size() - mList.size();
        return getViewType(position - mHeaderViews.size());
    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size()+mList.size()+mFooterViews.size();
    }

    /**
     * 获得adapter自己的V8Object
     */
    public V8Object getThisV8() {
        return mThisV8;
    }

    public Context getContext() {
        return mContext;
    }

    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    /**
     * 得到id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置v8方法
     */
    protected void initV8this(){
        mThisV8 = new V8Object(runtime);
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mList.clear();
                if (parameters.get(0) instanceof V8Array){
                    for (int i = 0; i < parameters.getArray(0).length(); i++) {
                        mList.add(parameters.getArray(0).getObject(i));
                    }
                }else if (parameters.get(0) instanceof V8Object){
                    mList.add(parameters.getObject(0));
                }
                notifyDataSetChanged();
                return null;
            }
        },"refresh");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Util.log("adapter","load");
                if (parameters.get(0) instanceof V8Array){
                    for (int i = 0; i < parameters.getArray(0).length(); i++) {
                        mList.add(parameters.getArray(0).getObject(i));
                    }
                }else if (parameters.get(0) instanceof V8Object){
                    mList.add(parameters.getObject(0));
                }
                notifyDataSetChanged();
                return null;
            }
        },"load");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.get(0) instanceof V8Array){
                    for (int i = 0; i < parameters.getArray(0).length(); i++) {
                        mList.add(0,parameters.getArray(0).getObject(i));
                    }
                }else if (parameters.get(0) instanceof V8Object){
                    mList.add(0,parameters.getObject(0));
                }
                notifyDataSetChanged();
                return null;
            }
        },"upload");
        mThisV8.registerJavaMethod((receiver, parameters) -> {
            int index = mSelectPosition;
            if (parameters.length() > 0 && parameters.get(0) instanceof Integer){
                index = parameters.getInteger(0);
            }
            V8Object newObj = new V8Object(ZKToolApi.runtime);
            V8Object object = mList.get(index);
            for (String key : object.getKeys()) {
                if (object.get(key) instanceof Integer){
                    newObj.add(key,object.getInteger(key));
                }else if (object.get(key) instanceof Double){
                    newObj.add(key,object.getDouble(key));
                }else if (object.get(key) instanceof Boolean){
                    newObj.add(key,object.getBoolean(key));
                }else if (object.get(key) instanceof String){
                    newObj.add(key,object.getString(key));
                }else if (object.get(key) instanceof V8Object){
                    newObj.add(key,object.getObject(key));
                }
            }
            return newObj;
        },"getItem");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int index = mSelectPosition;
                V8Object newData;
                if(parameters.length() == 1){
                    newData = parameters.getObject(0);
                }else {
                    index = parameters.getInteger(0);
                    newData = parameters.getObject(1);
                }
                V8Utils.mixV8Object(mList.get(index),newData);
                notifyItemChanged(index);
                return null;
            }
        },"notify");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return mSelectPosition;
            }
        },"getPosition");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int index = mSelectPosition;
                if (parameters.length() > 0 && parameters.get(0) instanceof Integer){
                    index = parameters.getInteger(0);
                }
                mList.remove(index);
                notifyItemRemoved(index);
                return null;
            }
        },"delete");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return mList.size();
            }
        },"getSize");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array res = new V8Array(ZKToolApi.runtime);
                for (V8Object object : mList) {
                    res.push(object);
                }
                return res;
            }
        },"val");
    }

    /**
     * 释放js资源
     */
    @Override
    public void release() {
        if (onClickJs != null) onClickJs.release();
        if (onLongClickJs != null) onLongClickJs.release();
        if (mThisV8 != null) mThisV8.release();
    }

    /**
     * 空布局
     */
    private class NullViewHolder extends RecyclerView.ViewHolder {
        private NullViewHolder(ViewGroup viewGroup) {
            super(mLayoutInflater.inflate(R.layout.item_empty_view, viewGroup, false));
        }
    }

    /**
     * 头布局
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        private HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 尾布局
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder{

        private FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 对应onCreateViewHolder
     */
    protected abstract RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i);

    /**
     *
     */
    protected abstract int getViewType(int position);

    /**
     * 对应onBindViewHolder
     */
    protected abstract void onBind(@NonNull final RecyclerView.ViewHolder viewHolder, int position);

    /**
     * 添加头布局
     */
    public void addHeaderView(View header){
        mHeaderViews.add(header);
    }
    public void addHeaderView(int index,View header){
        mHeaderViews.add(index,header);
    }

    /**
     * 添加尾布局
     */
    public void addFooterView(View footer){
        mFooterViews.add(footer);
    }
    public void removeFooterView(View footer){
        mFooterViews.remove(footer);
    };

    protected int setSelectPosition(RecyclerView.ViewHolder viewHolder){
        mSelectPosition = viewHolder.getLayoutPosition() - mHeaderViews.size();
        return mSelectPosition;
    }

    protected int getSelectPosition(){
        return mSelectPosition;
    }

    protected void invokeWithContext(String click, RecyclerView.ViewHolder viewHolder){
        setSelectPosition(viewHolder);
        if (click == null) return;
        V8Function v8Function = mZKDocument.genContext(click);
        mZKDocument.invokeWithContext(v8Function);
        v8Function.release();
    }

    @Override
    public void onCreate() {

    }

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
    public boolean onBack() {
        return true;
    }
}
