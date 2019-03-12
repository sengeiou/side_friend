package com.zankong.tool.zkapp.views.switcher;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/10 0010.
 */

public class Switcher1 extends ZKViewAgent {

    private ZKPView m_lp;
    private ZKPView m_lp2;
    private ZKImgView ml_img;
    private ZKImgView mswitch_on;
    private ZKImgView mswitch_off;
    private String on = "none";
    private String itemClick;
    public Switcher1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.item_switch);
        m_lp = (ZKPView)findViewById(R.id.left_text);
        m_lp2 = (ZKPView)findViewById(R.id.left_text_second);
        ml_img = (ZKImgView)findViewById(R.id.img_left);
        mswitch_on = (ZKImgView) findViewById(R.id.switch_img_on);
        mswitch_off = (ZKImgView)findViewById(R.id.switch_img_off);

    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String s = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "on":
                    on = s;
                    break;
                case "itemclick":
                    itemClick = s;
                    break;
            }
        }
        ArrayList<ZKPView> lplist = new ArrayList<>();
        lplist.add(m_lp);
        lplist.add(m_lp2);
        int lp = 0;
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "lp":
                    m_lp.init(getZKDocument(),element,getThisV8());
                    break;
                case "limg":
                    ml_img.init(getZKDocument(),element,getThisV8());
                    break;
                case "on":
                    mswitch_on.init(getZKDocument(),element,getThisV8());
                    break;
                case "off":
                    mswitch_off.init(getZKDocument(),element,getThisV8());
                    break;
            }
        }
        if (on.equals("true")) {
            mswitch_off.setVisibility(View.GONE);
        } else {
            mswitch_on.setVisibility(View.GONE);
        }


        mswitch_on.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        V8Object isStatus = new V8Object(ZKToolApi.runtime);
                        isStatus.add("status", false);
                        mswitch_off.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        if (itemClick!=null) {
                            V8Function v8Function = getZKDocument().genContextFn(itemClick);
                            getZKDocument().invokeWithContext(v8Function,isStatus);
                            v8Function.release();
                        }
                        return true;
                }
                return false;
            }
        });

        mswitch_off.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        V8Object isStatus = new V8Object(ZKToolApi.runtime);
                        isStatus.add("status", true);
                        mswitch_on.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        if (itemClick != null) {
                            V8Function v8Function = getZKDocument().genContextFn(itemClick);
                            getZKDocument().invokeWithContext(v8Function,isStatus);
                            v8Function.release();
                        }
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public Object getResult() {
        return null;
    }
}
