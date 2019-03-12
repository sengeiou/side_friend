package com.zankong.tool.side_friend.diy_view.release;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/12/1 0001 13:41
 */
public class ReleaseEditRadioGroup extends ZKViewAgent {
    private RadioGroup radio_group;
    private RadioButton radio_btn_01;
    private RadioButton radio_btn_02;
    private TextView type_tv_left;
    private V8Object v8Object;
    private V8Function onClickListener;
    private ZKDocument zkDocument;
    public ReleaseEditRadioGroup(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {

        setContentView(R.layout.release_edit_radiogroup);

        type_tv_left = (TextView) findViewById(R.id.type_tv_left);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        radio_btn_01 = (RadioButton) findViewById(R.id.radio_btn_01);
        radio_btn_02 = (RadioButton) findViewById(R.id.radio_btn_02);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (v8Object == null){
                    v8Object = new V8Object(ZKToolApi.runtime);
                }
                switch (checkedId){
                    case R.id.radio_btn_01:
                        v8Object.add("check",radio_btn_01.getText()+"");
                        break;
                    case R.id.radio_btn_02:
                        v8Object.add("check",radio_btn_02.getText()+"");
                        break;
                }
                zkDocument.invokeWithContext(onClickListener,v8Object);
            }
        });
    }
    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "type_left":
                    type_tv_left.setText(value + "");
                    break;
                case "btn_tv1":
                    radio_btn_01.setText(value + "");
                    break;
                case "btn_tv2":
                    radio_btn_02.setText(value + "");
                    break;
                case "click":
                    onClickListener = zkDocument.genContextFn(value);
                    break;
            }
        }
    }
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            int length = parameters.length();
            if (length > 0){
                V8Object object = parameters.getObject(0);
                boolean check = object.getBoolean("check");
                if (check){
                    radio_btn_01.setChecked(true);
                }
            }
            return null;
        }, "set");

        thisV8.registerJavaMethod((receiver, parameters) -> {
            int length = parameters.length();
            if (length > 0){
                V8Object object = parameters.getObject(0);
                boolean check = object.getBoolean("check");
                if (check){
                    radio_btn_01.setChecked(true);
                    radio_btn_02.setEnabled(false);
                    radio_btn_01.setEnabled(false);
                }
            }
            return null;
        }, "end");
    }
    @Override
    public Object getResult() {
        return null;
    }

    
}
