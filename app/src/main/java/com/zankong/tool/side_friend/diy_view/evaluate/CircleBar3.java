package com.zankong.tool.side_friend.diy_view.evaluate;

import android.graphics.Color;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class CircleBar3 extends ZKViewAgent {

    private int default_ratio = 100;
    private CircleProgressBar circle_bar;
    private double rewrad1 = 0;
    private V8Object v8Object;
    private double reward;
    private TextView reward_tv;

    public CircleBar3(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.clircle_bar_layout);
        circle_bar = (CircleProgressBar) findViewById(R.id.circBar);
        circle_bar.setPercent(default_ratio);
        reward_tv = (TextView) findViewById(R.id.reward_tv);
    }

    @Override
    public void fillData(Element selfElement) {

        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "text_color":
                    circle_bar.setCenterTextColor(Color.parseColor(value));
                    break;
                case "text_size":
                    circle_bar.setmCenterTextSize(Integer.parseInt(value));
                    break;
                case "stripe_width":
                    circle_bar.setStripeWidth(Integer.parseInt(value));
                    break;
                case "reward":
                    try{
                        V8Function v8Function = getZKDocument().genContextVal(value);
                        String val = getZKDocument().invokeWithContext(v8Function)+"";
                        reward = Double.parseDouble(val);
                        rewrad1 = reward;
                        reward_tv.setText("￥" + reward);

                        v8Function.release();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;



                case "val"://变量
                 
                    break;
            }
        }
    }


    @Override
    public Object getResult() {

        return default_ratio;
    }

    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {
                object = parameters.getObject(0);
                String m = object.get("nums") + "";
                int nums = Integer.parseInt(m);
                String n = object.get("setnum") + "";
                int setNum = Integer.parseInt(n);
                circle_bar.setPercent((nums - setNum));
                default_ratio = (nums - setNum);
                double flag = sub((object.get("flag") + ""));
                reward_tv.setText("￥" + flag);


            }
            return null;
        }, "minus");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0) {
                object = parameters.getObject(0);
                String m = object.get("nums") + "";
                int nums = Integer.parseInt(m);
                String n = object.get("setnum") + "";
                int setNum = Integer.parseInt(n);
                circle_bar.setPercent((nums + setNum));
                default_ratio = (nums + setNum);
                double flag = add((object.get("flag") + ""));
                reward_tv.setText("￥" + flag);

            }
            return null;
        }, "add");


    }


    private double add(String str) {
        Double rebate = CalcUtils.multiply(reward, 0.1);
        Double multiply;
        Log.e("AAAAAAA", str + "");
        if (str.equals("1")) {
            multiply = CalcUtils.multiply(rebate, 0.4);
        } else {
            multiply = CalcUtils.multiply(rebate, 0.2);
        }
        rewrad1 = CalcUtils.add(rewrad1, multiply);
        return rewrad1;
    }


    private double sub(String str) {
        Double rebate = CalcUtils.multiply(reward, 0.1);
        Double multiply;
        if (str.equals("1")) {
            multiply = CalcUtils.multiply(rebate, 0.4);
        } else {
            multiply = CalcUtils.multiply(rebate, 0.2);
        }

        rewrad1 = CalcUtils.sub(rewrad1, multiply);
        return rewrad1;
    }


}
