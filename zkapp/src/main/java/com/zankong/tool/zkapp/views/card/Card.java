package com.zankong.tool.zkapp.views.card;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/7/6.
 */

@ZKAppView("card")
public class Card extends ZKViews {
    public static HashMap<String ,Class<?>> styleMap = new HashMap<String,Class<?>>(){{
        put("1",Card1.class);
    }};

    public Card(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
