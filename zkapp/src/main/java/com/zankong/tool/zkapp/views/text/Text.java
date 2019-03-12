package com.zankong.tool.zkapp.views.text;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;
import java.util.HashMap;

/**
 * Created by YF on 2018/6/21.
 */
@ZKAppView("text")
public class Text extends ZKViews {
    public static HashMap<String,Class<?>> styleMap = new HashMap<String, Class<?>>(){
        {
            put("1",Text1.class);
            put("input",Text_input.class);
            put("media",Text_media.class);
            put("test",Text_test.class);
        }
    };

    public Text(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
