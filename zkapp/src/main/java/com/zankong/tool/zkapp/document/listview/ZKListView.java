package com.zankong.tool.zkapp.document.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/6/26.
 */

@ZKAppDocument("listView")
public class ZKListView extends ZKDocument {

    private ZKRefreshView          mZKRefreshView;           //刷新动画相关布局
    private ZKRecycleView          mZKRecycleView;           //装适配器的布局
    private LinearLayout mHeader , mFooter , mFloatView;

    public ZKListView(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKListView(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_listview);
//        analysisDoc(mElement,findViewById(R.id.back_layout),null);
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mFooter = (LinearLayout) findViewById(R.id.footer);
        mFloatView = (LinearLayout)findViewById(R.id.floatView);
        mZKRefreshView = (ZKRefreshView) findViewById(R.id.refreshLayout);
        mZKRecycleView = (ZKRecycleView) findViewById(R.id.recycle);
    }

    @Override
    protected void fillData(Element docElement) {
        for (Element element : docElement.elements()) {
            switch (element.getName()) {
                case "header":
                    setXml(element,mHeader);
                    break;
                case "body":
                    try {
                        setAdapterXml(element,mZKRecycleView);
                        mZKRecycleView.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mZKRefreshView.setEnabled(false);
                    for (Attribute attribute : element.attributes()) {
                        String value = attribute.getValue();
                        switch (attribute.getName()){
                            case "refresh":
                                mZKRefreshView.setEnabled(true);
                                mZKRefreshView.setOnRefreshListener(() -> {
                                    V8Function v8Function = genContext(value);
                                    invokeWithContext(v8Function);
                                    v8Function.release();
                                });
                                break;
                            case "load":
                                mZKRefreshView.setOnLoadListener(() -> {
                                    V8Function v8Function = genContext(value);
                                    invokeWithContext(v8Function);
                                    v8Function.release();
                                });
                                break;
                        }
                    }
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
                case "float":
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFloatView.getLayoutParams();
                    for (Attribute attribute : element.attributes()) {
                        String value = attribute.getValue();
                        switch (attribute.getName()) {
                            case "":
                                break;
                        }
                    }
                    setXml(element,mFloatView);
                    analysisFloatView(element,mFloatView);
                    break;
            }
        }
    }

    @Override
    protected void initV8This() {
        super.initV8This();
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Util.log("document.adapter");
                if(parameters.length() > 0 && parameters.get(0) instanceof String){
                    for (ZKAdapter zkAdapter : adapterMap) {
                        if (parameters.getString(0).equals(zkAdapter.getId()))return V8Utils.createFrom(zkAdapter.getThisV8());
                    }
                }
                if (adapterMap.size() > 0){
                    return V8Utils.createFrom(adapterMap.get(0).getThisV8());
                }
                return null;
            }
        },"adapter");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mZKRefreshView.setRefreshing(false);
                mZKRefreshView.stopLoad();
                return null;
            }
        },"stop");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int index = mZKRecycleView.getAdapter().getItemCount();
                if (parameters.length() >= 0){
                    int i = parameters.getInteger(0);
                    if (i>=0){
                        index = i;
                    }else {
                        index += i;
                    }
                }
                mZKRecycleView.scrollToPosition(index);
                return null;
            }
        },"scroll");
    }

    @Override
    public RecyclerView getRecycleView() {
        return mZKRecycleView;
    }
}
