package com.zankong.tool.side_friend.diy_view.wallet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class MyWalletView extends ZKViewAgent implements View.OnClickListener {

    private V8Object v8Object;
    private TextView tv_balance;
    private TextView tv_record;
    private TextView tv_advancePay;
    private TextView tv_look_context;

    private V8Function onItemClickListener;
    private ZKDocument zkDocument;
    public MyWalletView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        
        setContentView(R.layout.my_wallet_view);

        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_record = (TextView) findViewById(R.id.tv_record);
        tv_advancePay = (TextView) findViewById(R.id.tv_advancePay);
        tv_look_context = (TextView) findViewById(R.id.tv_look_context);
        
        tv_record.setOnClickListener(this);
        tv_look_context.setOnClickListener(this);
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "click":
                    onItemClickListener = zkDocument.genContextFn(value);
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
            if (parameters.length() > 0){
                V8Object object = parameters.getObject(0);
                String balance = object.get("balance") + "";
                if (!"".equals(balance))
                    tv_balance.setText(balance);
                String advancePay = object.get("advancePay") + "";
                if (!"".equals(advancePay))
                    tv_advancePay.setText(advancePay);
            }
            return null;
        }, "setData");

    }

    @Override
    public void onClick(View v) {
        if (v8Object == null){
            v8Object = new V8Object(ZKToolApi.runtime);
        }
        
        switch (v.getId()){
            case R.id.tv_record:
                v8Object.add("index",0);
                break;
            case R.id.tv_look_context:
                v8Object.add("index",1);
                break;
        }
        zkDocument.invokeWithContext(onItemClickListener,v8Object);
    }

    @Override
    public void release() {
        super.release();
        if (onItemClickListener != null) onItemClickListener.release();
    }
}
