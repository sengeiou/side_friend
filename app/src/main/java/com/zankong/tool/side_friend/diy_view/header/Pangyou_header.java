package com.zankong.tool.side_friend.diy_view.header;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/9/17.
 */

@ZKAppView("pangyou_header")
public class Pangyou_header extends ZKViews {
    private HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>(){{
        put("1",header_1.class);
    }};
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public Pangyou_header(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
