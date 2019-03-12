package com.zankong.tool.side_friend.diy_view.evaluate;


import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 14:10
 */
@ZKAppView("ratingbar")
public class RatingBarView extends ZKViews {
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public RatingBarView(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1",RatingBar1.class);
            put("2",RatingBar2.class);
            put("3",CircleBar3.class);
            put("4",LabelView.class);
            put("5",SenderPlayTourView.class);
        }
    };
    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
