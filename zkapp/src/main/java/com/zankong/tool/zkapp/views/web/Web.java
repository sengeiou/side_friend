package com.zankong.tool.zkapp.views.web;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/7/23.
 */

public class Web extends ZKViews {
    public static HashMap<String,Class<?>> styleMap = new HashMap<String,Class<?>>(){{
        put("1",Web_1.class);
    }};
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public Web(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
