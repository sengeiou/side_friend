package com.zankong.tool.side_friend.diy_view.index_user;

/**
 * @author Fsnzzz
 * @Created on 2018/11/9 0009 15:16
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

public class IndexUser1 extends ZKViewAgent implements View.OnClickListener{
    private LinearLayout layout1;
    private LinearLayout mRight;
    private V8Function onClickListener;
    private HashMap<String, V8Object> mChildList;
    private LinearLayout pangyouLayout;
    private LinearLayout sosLayout;
    private LinearLayout creditLayout;
    private ZKDocument mZKDocument;
    public IndexUser1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList = new HashMap<>();
        this.mZKDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.index_user_layout);
        pangyouLayout = (LinearLayout) findViewById(R.id.pangyou_layout);
        sosLayout = (LinearLayout) findViewById(R.id.sos_layout);
        creditLayout = (LinearLayout) findViewById(R.id.credit_layout);
        pangyouLayout.setOnClickListener(this);
        sosLayout.setOnClickListener(this);
        creditLayout.setOnClickListener(this);



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
//                    img.init(getZKDocument(), element);
//                    mChildList.put("img", img.getThisV8());
//                    break;
//                case "sex_img":
//                    sex_img.init(getZKDocument(), element);
//                    mChildList.put("sex_img", sex_img.getThisV8());
//                    break;
//                case "nikename":
//                    nikename.init(getZKDocument(), element);
//                    mChildList.put("nikename", nikename.getThisV8());
//                    break;
//                case "tv_credit":
//                    tv_credit.init(getZKDocument(), element);
//                    mChildList.put("tv_credit", tv_credit.getThisV8());
//                    break;
//                case "complete_tv":
//                    complete_tv.init(getZKDocument(), element);
//                    mChildList.put("complete_tv", complete_tv.getThisV8());
//                    //   mChildList.put("input1",zkInputView.getThisV8());
//                case "industry_tv":
//                    industry_tv.init(getZKDocument(), element);
//                    mChildList.put("industry_tv", industry_tv.getThisV8());
//                    //    mChildList.put("cp1",zkpView.getThisV8());
//                    break;
            }
        }
    }

    //    private void addView(LinearLayout viewGroup,Element element){
    //        ZKBaseView zkBaseView;
    //        if (element.getName().contains("p")){
    //            ZKPView zkpView = new ZKPView(getContext());
    //            zkpView.setGravity(Gravity.CENTER);
    //            zkBaseView = zkpView;
    //        }else if (element.getName().contains("img")){
    //            zkBaseView = new ZKImgView(getContext());
    //        }else {
    //            return;
    //        }
    //        View baseView = zkBaseView.getBaseView();
    //        viewGroup.addView(baseView);
    //        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) baseView.getLayoutParams();
    //        layoutParams.gravity = Gravity.CENTER;
    //        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
    //        if (element.getName().contains("img")){
    //            layoutParams.height = 60;
    //            layoutParams.width = 60;
    //        }
    //        baseView.setLayoutParams(layoutParams);
    //        zkBaseView.init(getZKDocument(),element);
    //        int i = 1;
    //        String name = element.getName() + i;
    //        while (mChildList.containsKey(name)){
    //            name  = element.getName() + i++;
    //        }
    //        mChildList.put(name,zkBaseView.getThisV8());
    //    }
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            int index = 1;
            if (parameters.length() >= 2) {
                index = parameters.getInteger(1);
            }
            String name = parameters.getString(0) + index;
            if (mChildList.containsKey(name)) {
                return V8Utils.createFrom(mChildList.get(name));
            }
            return null;
        }, "get");

        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Object object = null;
            if (parameters.length() > 0){
                 object = parameters.getObject(0);
            }

            return null;
        }, "set");
    }
    @Override
    public Object getResult() {
        String val = "";
        //        if (zkInputView != null) {
        //            val = zkInputView.getText().toString();
        //        }
        return val;
    }
    @Override
    public void onClick(View v) {
        V8Object object = new V8Object(ZKToolApi.runtime);
        switch (v.getId()){
            case R.id.pangyou_layout:
                object.add("index",0);
                break;
            case R.id.sos_layout:
                object.add("index",1);
                break;
            case R.id.credit_layout:
                object.add("index",2);
                break;
        }
        mZKDocument.invokeWithContext(onClickListener,object);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onClickListener != null) onClickListener.release();
    }
}
