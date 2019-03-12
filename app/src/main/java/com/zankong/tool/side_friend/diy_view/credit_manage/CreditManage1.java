package com.zankong.tool.side_friend.diy_view.credit_manage;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 11:21
 */
public class CreditManage1 extends ZKViewAgent {
    private LinearLayout layout1;
    private LinearLayout mRight;
    private V8Function onClickListener;
    private HashMap<String, V8Object> mChildList;
    private ZKDocument mZKDocument;
    private RadioButton creditAssess;
    private RadioButton meili_tv;
    private RadioButton jiedan_tv;
    private RadioGroup group_l;

    public CreditManage1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList = new HashMap<>();
        this.mZKDocument = zkDocument;
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.credit_manage_top);
        group_l = (RadioGroup) findViewById(R.id.group_layout);
        creditAssess = (RadioButton) findViewById(R.id.credit_assess);
        meili_tv = (RadioButton) findViewById(R.id.meili_tv);
        jiedan_tv = (RadioButton) findViewById(R.id.jiedan_tv);

        creditAssess.setChecked(true);
        group_l.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                V8Object object = new V8Object(ZKToolApi.runtime);
                switch (checkedId){
                    case R.id.credit_assess:
                        object.add("index",0);
                        break;
                    case R.id.meili_tv:
                        object.add("index",1);
                        break;
                    case R.id.jiedan_tv:
                        object.add("index",2);
                        break;
                }
                mZKDocument.invokeWithContext(onClickListener,object);
            }
        });

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
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
    }
    @Override
    public Object getResult() {
        String val = "";
        return val;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onClickListener != null) onClickListener.release();
    }
}
