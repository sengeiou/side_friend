package com.zankong.tool.zkapp.views.switcher;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/6/21.
 */
@ZKAppView("switch")
public class Switcher extends ZKViews {
    public static HashMap<String,Class<?>> styleMap = new HashMap<String,Class<?>>(){{
        put("1",Switcher1.class);
        put("2",Switcher2.class);
        put("3",Switcher3.class);
    }};

    public Switcher(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
