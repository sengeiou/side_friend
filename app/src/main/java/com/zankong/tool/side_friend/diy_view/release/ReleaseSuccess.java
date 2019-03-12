package com.zankong.tool.side_friend.diy_view.release;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/15 0015 16:04
 */

public class ReleaseSuccess extends ZKViewAgent implements View.OnClickListener{
    private LinearLayout layout1;
    private LinearLayout mRight;
    private V8Function onClickListener;
    private HashMap<String, V8Object> mChildList;
    private LinearLayout pangyouLayout;
    private LinearLayout sosLayout;
    private LinearLayout creditLayout;
    private ZKDocument mZKDocument;
    private TextView type_tv;
    private TextView reward_tv;
    private TextView sex_tv;
    private TextView credit_tv;

    public ReleaseSuccess(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList = new HashMap<>();
        this.mZKDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.release_success);
        type_tv = (TextView) findViewById(R.id.type_tv);
        reward_tv = (TextView) findViewById(R.id.reward_tv);
        sex_tv = (TextView) findViewById(R.id.sex_tv);
        credit_tv = (TextView) findViewById(R.id.credit_tv);

    }



    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "click":
                    onClickListener = mZKDocument.genContextFn(value);
                    break;
            }
        }
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "img":
              break;
            }
        }
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
//  "style": "跑腿代办",
//          "styleId": 1,
//          "type": "快递",
//          "typeId": 1,
//          "taskId": 61,
//          "reward": 86,
//          "otherReward": "技能交换",
//          "bargaining": 1,
//          "sex": "女",
//          "credit": 1,
//          "isAdvance": false,
//          "isNeedCert": 0,
//          "startTime": "2018-12-3 18:12:04"
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {

            if (parameters.length() > 0){
                V8Object object = parameters.getObject(0);
                String type = object.getString("type");
                String style = object.getString("style");
                String otherReward = object.getString("otherReward");
                String sex = object.getString("sex");
                String reward = object.get("reward")+"";
                String credit = object.get("credit")+"";
                type_tv.setText("类型:"+type);
                sex_tv.setText("性别:"+sex);
                credit_tv.setText("信用等级:"+credit+"以上");
                if ("".equals(otherReward) || otherReward == null){
                    reward_tv.setText("￥"+reward);
                }else {
                    reward_tv.setText("￥"+reward+"("+otherReward+")");
                }
            }
            return null;
        }, "set");

    }
}
