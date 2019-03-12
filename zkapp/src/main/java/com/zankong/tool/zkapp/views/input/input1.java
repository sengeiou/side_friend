package com.zankong.tool.zkapp.views.input;

import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.views.ZKInputView;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/11.
 */

public class input1 extends ZKViewAgent {

    private ZKInputView mInputView;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public input1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_input_1);
        mInputView = ((ZKInputView) findViewById(R.id.input));
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {
        for (Attribute attribute : selfElement.attributes()) {
            switch (attribute.getName()) {
                case "focusable":
                    view.setFocusable(false);
                    view.setFocusableInTouchMode(false);
                    break;
            }
        }
    }

    @Override
    public void fillData(Element viewElement) {
        mInputView.init(getZKDocument(),viewElement,getThisV8());
    }

    @Override
    public Object getResult() {
        return mInputView.getText();
    }
}
