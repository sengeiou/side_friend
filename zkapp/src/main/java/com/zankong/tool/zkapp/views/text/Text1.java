package com.zankong.tool.zkapp.views.text;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKInputView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

public class Text1 extends ZKViewAgent {
    private LinearLayout mLift,mRight,mCenter;
    private HashMap<String , V8Object> mChildList;
    private ZKInputView zkInputView;

    public Text1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList =  new HashMap<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_text_1);
        mLift = ((LinearLayout) findViewById(R.id.left));
        mRight = ((LinearLayout) findViewById(R.id.right));
        zkInputView = (ZKInputView) findViewById(R.id.input);
        mCenter = (LinearLayout) findViewById(R.id.center);
    }

    @Override
    public void fillData(Element selfElement) {
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "lp":
                    mLift.setVisibility(View.VISIBLE);
                    mRight.setVisibility(View.VISIBLE);
                    addView(mLift,element);
                    break;
                case "rp":
                    addView(mRight,element);
                    mLift.setVisibility(View.VISIBLE);
                    mRight.setVisibility(View.VISIBLE);
                    break;
                case "limg":
                    addView(mLift,element);
                    mLift.setVisibility(View.VISIBLE);
                    mRight.setVisibility(View.VISIBLE);
                    break;
                case "rimg":
                    addView(mRight,element);
                    mLift.setVisibility(View.VISIBLE);
                    mRight.setVisibility(View.VISIBLE);
                    break;
                case "input":
                    zkInputView.init(getZKDocument(),element);
                    mChildList.put("input1",zkInputView.getThisV8());
                case "cp":
                    addView(mCenter,element);
                    break;
                case "cimg":
                    addView(mCenter,element);
                    break;
            }
        }
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "lw":
                    ViewGroup.LayoutParams llp = mLift.getLayoutParams();
                    llp.width = Util.px2px(value);
                    mLift.setLayoutParams(llp);
                    break;
                case "rw":
                    ViewGroup.LayoutParams rlp = mRight.getLayoutParams();
                    rlp.width = Util.px2px(value);
                    mRight.setLayoutParams(rlp);
                    break;
            }
        }
    }

    private void addView(LinearLayout viewGroup,Element element){
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

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            int index = 1;
            if (parameters.length() >= 2){
                index = parameters.getInteger(1);
            }
            String name = parameters.getString(0)+index;
            if (mChildList.containsKey(name)) {
                return V8Utils.createFrom(mChildList.get(name));
            }
            return null;
        },"get");
    }

    @Override
    public Object getResult() {
        String val = "";
        if (zkInputView != null) {
            val = zkInputView.getText().toString();
        }
        return val;
    }


}
