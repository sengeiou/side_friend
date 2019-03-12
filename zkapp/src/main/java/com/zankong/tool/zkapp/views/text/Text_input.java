package com.zankong.tool.zkapp.views.text;

import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKInputView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/17.
 */

public class Text_input extends ZKViewAgent {

    private ZKInputView mInput;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Text_input(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_text_input);
        mInput = ((ZKInputView) findViewById(R.id.input));
    }

    @Override
    public void fillData(Element viewElement) {
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "focusable":
                    if ("false".equals(value)){
                        getView().setFocusable(false);
                        getView().setFocusableInTouchMode(false);
                    }
                    break;
            }
        }
        for (Element element : viewElement.elements()) {
            switch (element.getName()) {
                case "input":
                    mInput.init(getZKDocument(),element);
                    break;
            }
        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String key = parameters.getString(0);
                switch (key){
                    case "input":
                        return V8Utils.createFrom(mInput.getThisV8());
                }
                return null;
            }
        },"get");
    }

    @Override
    public Object getResult() {
        return mInput.getText().toString();
    }
}
