package com.example.zkapp_map;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;
import org.dom4j.Element;
import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/9/20 0020 14:42
 */
@ZKAppView("map")
public class MapView extends ZKViews {
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", MapOne.class);
            put("2", MapTwo.class);


        }
    };
    public MapView(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }

    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}