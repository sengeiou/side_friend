package com.zankong.tool.side_friend.diy_view.task_detail;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/9 0009 16:21
 */
@ZKAppView("taskdetail")
public class TaskDetailall extends ZKViews {
    public static HashMap<String, Class<?>> styleMap = new HashMap<String, Class<?>>() {
        {
            put("1", TaskDetail.class);
            put("2", TaskDetail2.class);
        }
    };
    public TaskDetailall(ZKDocument ZKDocument, Element element) {
        super(ZKDocument, element);
    }
    @Override
    protected HashMap<String, Class<?>> getStyleMap() {
        return styleMap;
    }

}
