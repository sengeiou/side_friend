package com.zankong.tool.zkapp.views.web;

import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/23.
 */

public class Web_1 extends ZKViewAgent {
    private WebView mWebView;
    private String url = "";

    public Web_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_web_1);
        mWebView = ((WebView) findViewById(R.id.web));
    }

    @Override
    public void fillData(Element selfElement) {
        boolean js = false;
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "src":
                    if ("".equals(url)){
                        url = value;
                    }
                    break;
                case "val":
                    V8Function v8Function = getZKDocument().genContextVal(value);
                    url = (String) getZKDocument().invokeWithContext(v8Function);
                    v8Function.release();
                    break;
                case "js":
                    js = "true".equals(value);
                    break;
            }
        }
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(js);//启用js
        mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        mWebView.getSettings().setSavePassword(false);//不保存密码
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mWebView.loadUrl(parameters.getString(0));
                return null;
            }
        },"src");
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mWebView.loadDataWithBaseURL(null, parameters.getString(0), "text/html", "utf-8", null);
                return null;
            }
        },"html");
    }
}
