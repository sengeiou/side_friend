package com.zankong.tool.side_friend.diy_view.release;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;
import org.dom4j.Element;
import java.util.HashMap;
/**
 * @author Fsnzzz
 * @Created on 2018/11/14 0014 16:56
 */
@ZKAppView("release")
public class Release extends ZKViews {
    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     *
     * @param ZKDocument
     * @param element
     */
    public Release(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", ReleaseSuccess.class);
            put("2", ReleaseEditChecked.class);
            put("3", ReleaseEditRadioGroup.class);
        }
    };
    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }
}
