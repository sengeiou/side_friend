package com.zankong.tool.zkapp.views.script;

import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.V8Function;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.IOException;
import java.util.List;


/**
 * Created by YF on 2018/6/25.
 */

public class script1 extends ZKViewAgent {
    public V8Function srcFn,scriptFn;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public script1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_script_1);
    }

    @Override
    public void fillData(Element viewElement) {
        List<Attribute> attributes = viewElement.attributes();
        for (Attribute attribute : attributes) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "src":
                    try {
                        srcFn = getZKDocument().genContext(Util.getFileFromAssets(value));
                        getZKDocument().invokeWithContext(srcFn);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Util.log("script src js文件解释错误",viewElement.asXML());
                    }
                    break;
            }
        }
        scriptFn = getZKDocument().genContext(viewElement.getText());
        getZKDocument().invokeWithContext(scriptFn);
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {

    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void release() {
        super.release();
        if (scriptFn != null) {
            scriptFn.release();
        }
        if (srcFn != null)srcFn.release();
    }
}
