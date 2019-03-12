package com.zankong.tool.zkapp.views.range;

import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/17.
 */

public class Range_circle extends ZKViewAgent {

    private ZKProgressBarCircle mProgressBarCircle;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Range_circle(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_range_circle);
        mProgressBarCircle = ((ZKProgressBarCircle) findViewById(R.id.progressBar));
    }

    @Override
    public void fillData(Element viewElement) {
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "progress":
                    mProgressBarCircle.setProgress(Float.parseFloat(value));
                    break;
            }
        }
    }

    @Override
    public Object getResult() {
        return mProgressBarCircle.getProgress();
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mProgressBarCircle.setProgress(parameters.getInteger(0));
                return null;
            }
        },"setProgress");
    }
}
