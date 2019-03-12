package com.zankong.tool.zkapp.document.plain;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;
import com.zankong.tool.zkapp.util.pull_refresh_layout.ZKPullRefreshLayout;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/6/22.
 */

@ZKAppDocument("plain")
public class ZKPlain extends ZKDocument {
    private LinearLayout        mHeader,           //头布局
                                mBody,             //身体可滑动区域
                                mFooter;           //尾布局
    private ZKScrollView mScrollView;
    private ZKPullRefreshLayout mPullToRefreshLayout;

    public ZKPlain(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKPlain(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initV8This() {
        super.initV8This();
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mPullToRefreshLayout.stop(ZKPullRefreshLayout.SUCCEED);
                return null;
            }
        },"stop");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"scroll");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mScrollView.setCanPullUp(parameters.getBoolean(0));
                return null;
            }
        },"canUp");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mScrollView.setCanPullDown(parameters.getBoolean(0));
                return null;
            }
        },"canDown");
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_plain);
        analysisDoc(mElement,findViewById(R.id.back_layout),null);
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mBody = ((LinearLayout) findViewById(R.id.body));
        mFooter = ((LinearLayout) findViewById(R.id.footer));
        mScrollView = (ZKScrollView) findViewById(R.id.scrollView);
        mPullToRefreshLayout = (ZKPullRefreshLayout) findViewById(R.id.zkRefresh);
    }

    @Override
    protected void fillData(Element docElement) {
        for (Element element : docElement.elements()) {
            switch (element.getName().toLowerCase()) {
                case "header":
                    setXml(element,mHeader);
                    break;
                case "body":
                    setXml(element,mBody);
                    for (Attribute attribute : element.attributes()) {
                        switch (attribute.getName()) {
                            case "refresh":
                                mScrollView.setCanPullDown(true);
                                mPullToRefreshLayout.setOnRefreshingStartListener(new ZKPullRefreshLayout.OnRefreshingStartListener() {
                                    @Override
                                    public void refresh() {
                                        V8Function v8Function = genContext(attribute.getValue());
                                        invokeWithContext(v8Function);
                                        v8Function.release();
                                    }
                                });
                                break;
                            case "load":
                                mScrollView.setCanPullUp(true);
                                mPullToRefreshLayout.setOnLoadingStartListener(new ZKPullRefreshLayout.OnLoadingStartListener() {
                                    @Override
                                    public void load() {
                                        V8Function v8Function = genContext(attribute.getValue());
                                        invokeWithContext(v8Function);
                                        v8Function.release();
                                    }
                                });
                                break;
                            case "background":
                                mPullToRefreshLayout.setBackgroundColor(Color.parseColor(attribute.getValue()));
                                break;
                        }
                    }
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
            }
        }
    }

    @Override
    public ScrollView getScrollView() {
        return mScrollView;
    }

}
