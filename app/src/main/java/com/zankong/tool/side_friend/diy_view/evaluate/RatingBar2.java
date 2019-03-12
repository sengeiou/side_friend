package com.zankong.tool.side_friend.diy_view.evaluate;

import android.util.Log;
import android.view.ViewGroup;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class RatingBar2 extends ZKViewAgent {
    private BatingBarView1 ratingBar;
    private ZKDocument zkDocument;
    private V8Function onClickListener;
    private V8Object object;
    private int num = 5;

    public RatingBar2(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.rating_bar2);
        ratingBar = (BatingBarView1) findViewById(R.id.rb);
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "totalnumber":
                    ratingBar.setStartTotalNumber(Integer.parseInt(value));
                    break;
                case "selectednumber":
                    ratingBar.setSelectedNumber(Integer.parseInt(value));
                    break;
                case "enabled":
                    ratingBar.setEnabled(false);
                    break;
                case "click":
                    onClickListener = zkDocument.genContextFn(value);
                    break;
            }
        }
    }

    @Override
    public Object getResult() {
        return num;
    }


    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {
                object = parameters.getObject(0);
                int number = object.getInteger("number");

                String n = object.get("nums")+"";

                int nums = Integer.parseInt(n);
                Log.e("AAAAAA",(nums-number)+"");
                ratingBar.setSelectedNumber((nums-number));
                num = num - number;
            }
            return null;
        }, "minus");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {
                object = parameters.getObject(0);
                int number = object.getInteger("number");
                String n = (String) object.get("nums");
                int nums = Integer.parseInt(n);
                ratingBar.setSelectedNumber((nums+number));
                num = nums+number;
            }
            return null;
        }, "add");


    }
}
