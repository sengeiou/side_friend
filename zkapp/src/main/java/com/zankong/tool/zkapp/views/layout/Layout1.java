package com.zankong.tool.zkapp.views.layout;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ViewUtil;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import org.dom4j.Attribute;
import org.dom4j.Element;
/**
 * Created by YF on 2018/7/10.
 */
public class Layout1 extends ZKViewAgent {

    private LinearLayout mLayout;


    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Layout1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_layout_1);
        mLayout = ((LinearLayout) findViewById(R.id.back_layout));
    }

    @Override
    public void fillData(Element viewElement) {

        for (Element element : viewElement.elements()) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            mLayout.addView(linearLayout);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            for (Attribute attribute : element.attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName()) {
                    case "width"://宽度
                        layoutParams.width = Util.px2px(value);
                        break;
                    case "weight"://权重
                        layoutParams.weight = Float.parseFloat(value);
                        break;
                    case "height"://高度
                        layoutParams.height = Util.px2px(value);
                        break;
                    case "gravity":
                        layoutParams.gravity = 0;
                        Util.log("gravity");

                        for (String s : value.split("\\|")) {
                            Util.log("gravity for");

                            Util.log(s);
                            Util.log("gravity for");
                            Util.log("gravity for",s);

                            layoutParams.gravity = layoutParams.gravity| ViewUtil.getGravityMap.get(s);
                        }
                        break;
                }
            }
            linearLayout.setLayoutParams(layoutParams);
            getZKDocument().setXml(element,linearLayout);
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
