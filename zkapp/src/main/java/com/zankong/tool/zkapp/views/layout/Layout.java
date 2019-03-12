package com.zankong.tool.zkapp.views.layout;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/7/10.
 */

@ZKAppView("layout")
public class Layout extends ZKViews {
    public static HashMap<String,Class<?>> styleMap = new HashMap<String,Class<?>>(){{
        put("1",Layout1.class);
    }};

    public Layout(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
