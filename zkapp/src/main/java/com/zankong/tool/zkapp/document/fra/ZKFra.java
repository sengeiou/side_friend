package com.zankong.tool.zkapp.document.fra;

import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/6/28.
 */

@ZKAppDocument("frame")
public class ZKFra extends ZKDocument {
    private ZKFrameLayout           mZKFrameLayout;             //fragment容器
    private LinearLayout            mHeader;                    //头布局
    private LinearLayout            mFooter;                    //尾布局

    public ZKFra(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKFra(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }


    @Override
    protected void initView() {
        setContextView(R.layout.doc_fra);
        analysisDoc(mElement,findViewById(R.id.back_layout),null);
        mZKFrameLayout = (ZKFrameLayout) findViewById(R.id.frame);
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mFooter = ((LinearLayout) findViewById(R.id.footer));
    }

    @Override
    protected void fillData(Element docElement) {
        for (Element element : docElement.elements()) {
            switch (element.getName()) {
                case "header":
                    setXml(element,mHeader);
                    break;
                case "body":
                    mZKFrameLayout.init(this);
                    mZKFrameLayout.fillData(element);
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
            }
        }
    }

    @Override
    protected void initV8This() {
        super.initV8This();
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int index = 0;
                if (parameters.length() > 0 && parameters.get(0) instanceof Integer){
                    index = parameters.getInteger(0);
                }
                mZKFrameLayout.showFragment(index);
                return null;
            }
        },"showFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
//        for (Fragment fragment : getFragmentManager().getFragments()) {
//            fragment.onHiddenChanged(false);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        for (Fragment fragment : getFragmentManager().getFragments()) {
//            fragment.onHiddenChanged(true);
//        }
    }
}
