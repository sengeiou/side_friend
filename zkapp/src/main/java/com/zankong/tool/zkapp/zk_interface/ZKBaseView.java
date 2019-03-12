package com.zankong.tool.zkapp.zk_interface;

import android.view.View;

import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.document.ZKDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/24.
 */

public interface ZKBaseView extends Releasable {
    void init(ZKDocument zkDocument, Element element);//view的初始化
    void init(ZKDocument zkDocument, Element element, V8Object object);//有object传入的view的初始化
    V8Object getThisV8();//获得jsObject
    View getBaseView();//获得布局
}
