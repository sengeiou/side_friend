package com.zankong.tool.zkapp.views.card;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKParentAttr;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/6.
 */

public class Card1 extends ZKViewAgent {

    private CardView mCardView;
    private LinearLayout mBody;
    ZKParentAttr zkParentAttribute;


    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public Card1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        zkParentAttribute = new ZKParentAttr(zkDocument,element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_card_1);
        mCardView = ((CardView) findViewById(R.id.card));
        mBody = ((LinearLayout) findViewById(R.id.body));
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {

    }

    @Override
    public void fillData(Element viewElement) {
        mCardView.setCardElevation(8);
        zkParentAttribute.initThisV8(getThisV8());
//        zkParentAttribute.setView(mCardView);
        ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()){
                case "radius":
                    mCardView.setRadius(Util.px2px(value));
                    break;
                case "z":
                    mCardView.setCardElevation(Float.parseFloat(value));
                    break;
                case "gravity":
                    break;
                case "width":
                    layoutParams.width = Util.px2px(value,getZKDocument().getDisplayMetrics().widthPixels);
                    break;
                case "padding":
                    String[] padding = value.split(",");
                    if (padding.length == 1){
                        mCardView.setContentPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0])
                        );
                    }else {
                        mCardView.setContentPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[1]),
                                Util.px2px(padding[2]),
                                Util.px2px(padding[3])
                        );
                    }
                    break;
                case "background":
                    mCardView.setCardBackgroundColor(Color.parseColor(value));
                    break;
                case "margin":
                    String[] margin = value.split(",");
                    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                        if (margin.length == 1) {
                            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0])
                            );
                        } else {
                            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[1]),
                                    Util.px2px(margin[2]),
                                    Util.px2px(margin[3])
                            );
                        }

                    }
                    break;
            }
        }
        mCardView.setLayoutParams(layoutParams);
        getZKDocument().setXml(viewElement,mBody);
    }

    @Override
    public Object getResult() {
        return null;
    }
}
