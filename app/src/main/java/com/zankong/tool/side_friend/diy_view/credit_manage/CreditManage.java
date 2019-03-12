package com.zankong.tool.side_friend.diy_view.credit_manage;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 11:18
 */
@ZKAppView("creditmanage")
public class CreditManage extends ZKViews {
    private HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>(){{
        put("1",CreditManage1.class);
        put("2",CreditDetail.class);
    }};
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public CreditManage(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
