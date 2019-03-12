package com.zankong.tool.zkapp.views.script;

import android.view.ViewGroup;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/6/25.
 */

@ZKAppView("script")
public class script extends ZKViews {
    public static HashMap<String,Class<?>> styleMap = new HashMap<String, Class<?>>(){{
        put("1",script1.class);
    }};

    public script(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }

}
