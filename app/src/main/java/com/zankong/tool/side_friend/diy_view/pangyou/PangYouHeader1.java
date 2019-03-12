package com.zankong.tool.side_friend.diy_view.pangyou;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eclipsesource.v8.V8Object;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Element;

import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/19 0019 16:18
 */
public class PangYouHeader1 extends ZKViewAgent {
    private LinearLayout mLift;
    private LinearLayout mRight;
    private LinearLayout center;
    private HashMap<String , V8Object> mChildList;

    public PangYouHeader1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList =  new HashMap<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.pangyou_header);
        mLift = ((LinearLayout) findViewById(R.id.left));
        mRight = ((LinearLayout) findViewById(R.id.right));
        center = ((LinearLayout) findViewById(R.id.center));
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
                    break;
                case "rimg":
                    addView(mRight,element);
                    break;
                case "cp":
                    addView(center,element);
                    break;
                case "cimg":
                    addView(center,element);
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

        return val;
    }

}
