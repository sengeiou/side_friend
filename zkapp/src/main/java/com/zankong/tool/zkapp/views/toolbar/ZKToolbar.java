package com.zankong.tool.zkapp.views.toolbar;

import android.support.design.widget.AppBarLayout;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

@ZKAppView("toolbar")
public class ZKToolbar extends ZKViews implements AppBarLayout.OnOffsetChangedListener {
    public static HashMap<String, Class<?>>  styleMap = new HashMap<String, Class<?>>(){{

    }};
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public ZKToolbar(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mZKViewAgent instanceof AppBarLayout.OnOffsetChangedListener) {
            ((AppBarLayout.OnOffsetChangedListener) mZKViewAgent).onOffsetChanged(appBarLayout,i);
        }
    }
}
