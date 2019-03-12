package com.zankong.tool.side_friend.diy_view.pangyou;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;
import com.zankong.tool.zkapp.views.text.Text1;
import com.zankong.tool.zkapp.views.text.Text_input;
import com.zankong.tool.zkapp.views.text.Text_media;
import com.zankong.tool.zkapp.views.text.Text_test;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/19 0019 16:16
 */
@ZKAppView("pangyouheader")
public class PangYouHeader extends ZKViews {
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", PangYouHeader1.class);
        }
    };

    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public PangYouHeader(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }


}
