package com.example.zkapp_identity.identity;


import com.example.zkapp_identity.identity_face.IdentityFaceEntrance;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;
@ZKAppView("identity")
public class IdentityView extends ZKViews {
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public IdentityView(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }

    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", IDCardActivityView.class);
            put("2",IdentityFaceEntrance.class);
        }
    };
}
