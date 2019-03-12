package com.zankong.tool.side_friend.diy_view.wallet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eclipsesource.v8.V8Function;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class HeaderLayoutBill extends ZKViewAgent {

    private RelativeLayout layout_screen;

    public HeaderLayoutBill(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.header_layout_bill);
        findViewById(R.id.layout_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V8Function v8Function = getZKDocument().genContext("document.finish()");
                getZKDocument().invokeWithContext(v8Function);
                v8Function.release();
            }
        });
        layout_screen = (RelativeLayout) findViewById(R.id.layout_screen);
        
   
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
