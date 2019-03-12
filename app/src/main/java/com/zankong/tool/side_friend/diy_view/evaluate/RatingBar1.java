package com.zankong.tool.side_friend.diy_view.evaluate;

import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 14:13
 */
public class RatingBar1 extends ZKViewAgent {

    private BatingBarView1 mRatingBar;
    private TextView wenzi_tv;

    public RatingBar1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.ratingbar_layout);
        mRatingBar = (BatingBarView1) findViewById(R.id.rb);
        wenzi_tv = (TextView) findViewById(R.id.wenzi_tv);
       mRatingBar.setEnabled(false);

    }
    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "totalnumber":
                    mRatingBar.setStartTotalNumber(Integer.parseInt(value));
                    break;
                case "selectednumber":
                    mRatingBar.setSelectedNumber(Integer.parseInt(value));
                    break;
                case "enabled":
                    mRatingBar.setEnabled(false);
                    break;
                case "type":
                    wenzi_tv.setText(value+"");
                    break;
            }
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0){
                object = parameters.getObject(0);

                String totalnumber = object.getString("totalnumber");
                String selectednumber = object.getString("selectednumber");

                mRatingBar.setSelectedNumber(Integer.parseInt(selectednumber));
                mRatingBar.setStartTotalNumber(Integer.parseInt(totalnumber));
            }

            return null;
        }, "set");


    }
}
