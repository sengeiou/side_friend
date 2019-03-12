package com.zankong.tool.side_friend.diy_document;

import android.content.Context;
import android.view.GestureDetector;

import android.view.ViewGroup;
import android.view.Window;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppDocument;
import com.zankong.tool.zkapp.util.pull_refresh_layout.ZKPullRefreshLayout;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/31.
 */

@ZKAppDocument("linearlayout")
public class LinearLayout extends ZKDocument {
    private GestureDetector mGestureDetector;
    private android.widget.LinearLayout body,header;
    public LinearLayout(Context context, Element root, Window window) {
        super(context, root, window);
    }
    public LinearLayout(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }
    @Override
    protected void initView() {
        setContextView(R.layout.document_linearlayout);
        body = (android.widget.LinearLayout) findViewById(R.id.body_layout);
        header = (android.widget.LinearLayout) findViewById(R.id.header);
    }

    @Override
    protected void fillData(Element docElement) {
        for (Element element : docElement.elements()) {
            switch (element.getName().toLowerCase()) {
                case "header":
                    setXml(element,header);
                    break;
                case "body":
                    setXml(element,body);
                    break;
            }
        }
    }
}
