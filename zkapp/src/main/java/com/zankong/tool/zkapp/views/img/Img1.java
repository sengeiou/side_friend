package com.zankong.tool.zkapp.views.img;

import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/11.
 */

public class Img1 extends ZKViewAgent {

    private ZKImgView mImgView;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Img1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_img_1);
        mImgView = ((ZKImgView) findViewById(R.id.img));
    }

    @Override
    public void fillData(Element viewElement) {
        mImgView.init(getZKDocument(),viewElement,getThisV8());
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {

    }

    @Override
    public Object getResult() {
        return "";
    }
}
