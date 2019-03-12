package com.zankong.tool.zkapp.views.p;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ViewUtil;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/5.
 */

public class p1 extends ZKViewAgent {
    private ZKPView mP;
    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public p1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_p_1);
        mP = ((ZKPView) findViewById(R.id.p));

    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {
    }
    @Override
    public void fillData(Element viewElement) {
        mP.init(getZKDocument(),viewElement,getThisV8());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mP.getLayoutParams();
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "gravity":
                    layoutParams.gravity = ViewUtil.getGravityMap.get(value);
                    break;
            }
        }
    }
    @Override
    public Object getResult() {
        return null;
    }

}
