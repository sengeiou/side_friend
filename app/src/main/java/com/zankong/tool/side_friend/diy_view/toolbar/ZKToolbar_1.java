package com.zankong.tool.side_friend.diy_view.toolbar;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;


public class ZKToolbar_1 extends ZKViewAgent implements AppBarLayout.OnOffsetChangedListener {

    private TextView address;
    private LinearLayout mAddressBtn;
    private LinearLayout mSearchBtn;

    public ZKToolbar_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_toolbar);
        address = ((TextView) findViewById(R.id.text));
        mAddressBtn = ((LinearLayout) findViewById(R.id.addressBtn));
        mSearchBtn = ((LinearLayout) findViewById(R.id.searchBtn));
    }

    @Override
    public void fillData(Element selfElement) {
//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getView().getLayoutParams();
//        layoutParams.setMargins(0,getZKDocument().getStatusBarHeight(),0,0);
//        getView().setLayoutParams(layoutParams);
        for (Element element : selfElement.elements()) {
            switch (element.getName().toLowerCase()) {
                case "address":
                    for (Attribute attribute : element.attributes()) {
                        String value = attribute.getValue();
                        switch (attribute.getName().toLowerCase()) {
                            case "click":
                                Util.log("FFFFFFFFFF",value);
                                mAddressBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.log("FFFFFFFFFF2",value);
                                        V8Function v8Function = getZKDocument().genContext(value);
                                        getZKDocument().invokeWithContext(v8Function);
                                        v8Function.release();
                                    }
                                });
                                break;
                        }
                    }
                    break;
                case "search":
                    for (Attribute attribute : element.attributes()) {
                        String value = attribute.getValue();
                        switch (attribute.getName().toLowerCase()) {
                            case "click":
                                mSearchBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        V8Function v8Function = getZKDocument().genContext(value);
                                        getZKDocument().invokeWithContext(v8Function);
                                        v8Function.release();
                                    }
                                });
                                break;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String address = parameters.getString(0);
                if(address.length() > 9){
                    address = address.substring(0,9);
                }
                ZKToolbar_1.this.address.setText(address);
                return null;
            }
        },"setAddress");
    }

    @Override
    public Object getResult() {
        return null;
    }

    private int bannerH = getZKDocument().getDisplayMetrics().widthPixels * 9 / 16;
    private int toolbarH = Util.px2px("144");
    private int h = bannerH - toolbarH - getZKDocument().getStatusBarHeight();
    private int bColor = Color.parseColor("#5c9afd");
    private int red = (bColor & 0xff0000)>>16,green = (bColor & 0x00ff00)>>8,blue = bColor & 0x0000ff;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int backColor = Color.argb(255*-i/h,red,green,blue);
        int c = (h+i)*255/h;
        getView().setBackgroundColor(backColor);
    }
}
