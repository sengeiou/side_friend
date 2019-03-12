package com.zankong.tool.side_friend.diy_view.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKInputView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

public class Text_edit extends ZKViewAgent {
    private LinearLayout leftLayout;
    private ZKInputView edit;
    private ZKPView textNum;
    private HashMap<String , V8Object> mChildList;
    private int num = 200;

    public Text_edit(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList =  new HashMap<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_text_edit);
        leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
        edit = (ZKInputView) findViewById(R.id.edit);
        textNum = (ZKPView) findViewById(R.id.textNum);
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "num":
                    try{
                        num = Integer.parseInt(value);
                    }catch (Exception e){
                        e.printStackTrace();
                        Util.log("num 必须为int类型");
                    }
                    break;
            }
        }
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "limg":
                    addView(leftLayout,element);
                    break;
                case "input":
                    edit.init(getZKDocument(),element);
                    mChildList.put("input",edit.getThisV8());
                    break;
                case "rp":
                    textNum.init(getZKDocument(),element);
                    mChildList.put("rp",textNum.getThisV8());
                    break;
            }
        }
        textNum.setText(edit.getText().toString().length() + "/" + num);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if (length > num){
                    Util.showT("最多输入"+num+"个字");
                    return;
                }
                textNum.setText(length+"/"+num);
            }
        });
    }

    @Override
    public Object getResult() {
        return edit.getText().toString();
    }

    private void addView(LinearLayout viewGroup, Element element){
        ZKBaseView zkBaseView;
        if (element.getName().contains("p")){
            ZKPView zkpView = new ZKPView(getContext());
            zkpView.setGravity(Gravity.CENTER);
            zkBaseView = zkpView;
        }else if (element.getName().contains("img")){
            zkBaseView = new ZKImgView(getContext());
        }else {
            return;
        }
        View baseView = zkBaseView.getBaseView();
        viewGroup.addView(baseView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) baseView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        if (element.getName().contains("img")){
            layoutParams.height = 60;
            layoutParams.width = 60;
        }
        baseView.setLayoutParams(layoutParams);
        zkBaseView.init(getZKDocument(),element);
        int i = 1;
        String name = element.getName() + i;
        while (mChildList.containsKey(name)){
            name  = element.getName() + i++;
        }
        mChildList.put(name,zkBaseView.getThisV8());
    }
}
