package com.zankong.tool.zkapp.views.time;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;
import com.zankong.tool.zkapp.views.text.Text1;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/24 0024.
 */

@ZKAppView("time")
public class Time extends ZKViews {
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", Time1.class);
        }
    };

    public Time(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}