package com.zankong.tool.zkapp.views.text;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class Text_test extends ZKViewAgent {
    EditText editText;
    TextView show,hide,nothing,resize;
    InputMethodManager imm;
    public Text_test(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_text_test);
        editText = (EditText) findViewById(R.id.edit);
        show = (TextView) findViewById(R.id.show);
        hide = (TextView) findViewById(R.id.hide);
        nothing = (TextView) findViewById(R.id.nothing);
        resize = (TextView) findViewById(R.id.resize);
    }

    @Override
    public void fillData(Element selfElement) {
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//得到软键盘
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public Object getResult() {
        return null;
    }
}
