package com.zankong.tool.zkapp.views.table;

import android.view.ViewGroup;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

/**
 * Created by YF on 2018/8/14.
 */

public class Table1 extends ZKViewAgent {
    public Table1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_table_1);
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
