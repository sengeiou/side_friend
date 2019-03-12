package com.zankong.tool.side_friend.diy_view.group_details;

import com.zankong.tool.side_friend.diy_view.header.header_1;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/20 0020 18:12
 */
@ZKAppView("groupdetails")
public class GroupDetails extends ZKViews {
    private HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>(){{
        put("1",GroupDetails1.class);
    }};
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public GroupDetails(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return null;
    }
}
