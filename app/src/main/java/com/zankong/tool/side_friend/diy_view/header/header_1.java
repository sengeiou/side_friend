package com.zankong.tool.side_friend.diy_view.header;

import android.view.ViewGroup;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.side_friend.R;

import org.dom4j.Element;

/**
 * Created by YF on 2018/9/17.
 */

public class header_1 extends ZKViewAgent {
    public header_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_pangyou_header_1);
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
