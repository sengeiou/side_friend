package com.zankong.tool.side_friend.diy_view.group_details;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/11/20 0020 18:14
 */
public class GroupDetails1 extends ZKViewAgent {

    private RecyclerView rv_group;

    public GroupDetails1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.group_details_layout);
        rv_group = (RecyclerView) findViewById(R.id.group_rv);
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
