package com.zankong.tool.side_friend.diy_view.index_user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class UserShareRecordView extends ZKViewAgent {

    private TextView tv_profit;
    private TextView tv_count;

    public UserShareRecordView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.user_share_record_layout);
        tv_profit = (TextView) findViewById(R.id.tv_profit);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }


    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() > 0) {
                V8Object object = parameters.getObject(0);
                String profit = object.get("profit") + "";
                String count = object.get("count") + "";
                if (!"".equals(profit))
                    tv_profit.setText(profit+"");
                if (!"".equals(count))
                    tv_count.setText(count+"");
            }
            return null;
        }, "set");
    }
}
