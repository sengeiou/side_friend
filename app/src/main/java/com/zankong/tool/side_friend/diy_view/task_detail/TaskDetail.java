package com.zankong.tool.side_friend.diy_view.task_detail;

/**
 * @author Fsnzzz
 * @Created on 2018/11/9 0009 15:16
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.credit_manage.ImageAdapter;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import com.zankong.tool.zkapp.views.text.Text;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskDetail extends ZKViewAgent {
    private LinearLayout layout10;
    private LinearLayout mRight;
    private HashMap<String, V8Object> mChildList;
    // ZKInputView zkInputView;
    ZKPView zkpView;
    private RoundedImageView img;
    private ZKImgView sex_img;
    private ZKDocument mZkDocument;
    private TextView nikename;
    private TextView tv_credit, complete_tv, context_tv1, agreement_tv;
    private ZKPView industry_tv, out_time, createTime, reward;
    private TextView claim_tv, content_tv, start_mine_danc, end_den, end_name, start_den, address_start;
    private RecyclerView rv;
    private RelativeLayout layout5, layout2;
    private RelativeLayout layout3;
    private CheckBox check_box;

    private V8Object v8Object;
    private V8Function onClickListener;
    private boolean isFlag = false;

    public TaskDetail(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList = new HashMap<>();
        this.mZkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.task_layout);
        layout10 = ((LinearLayout) findViewById(R.id.layout10));
        img = (RoundedImageView) findViewById(R.id.img);
        sex_img = (ZKImgView) findViewById(R.id.sex_img);
        nikename = (TextView) findViewById(R.id.nikename);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        complete_tv = (TextView) findViewById(R.id.complete_tv);
        industry_tv = (ZKPView) findViewById(R.id.industry_tv);
        out_time = (ZKPView) findViewById(R.id.out_time);
        createTime = (ZKPView) findViewById(R.id.createTime);
        reward = (ZKPView) findViewById(R.id.reward);
        claim_tv = (TextView) findViewById(R.id.claim_tv);
        agreement_tv = (TextView) findViewById(R.id.agreement_tv);
        content_tv = (TextView) findViewById(R.id.content_tv);
        end_den = (TextView) findViewById(R.id.end_den);
        context_tv1 = (TextView) findViewById(R.id.context_tv1);
        end_name = (TextView) findViewById(R.id.end_name);
        start_den = (TextView) findViewById(R.id.start_den);
        address_start = (TextView) findViewById(R.id.address_start);
        start_mine_danc = (TextView) findViewById(R.id.start_mine_danc);
        layout5 = (RelativeLayout) findViewById(R.id.layout5);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
        check_box = (CheckBox) findViewById(R.id.check_box);
        check_box.setChecked(isFlag);

        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFlag = isChecked;
                if (v8Object == null){
                    v8Object = new V8Object(ZKToolApi.runtime);
                }
                v8Object.add("isChecked",isChecked);
                mZkDocument.invokeWithContext(onClickListener,isChecked);
            }
        });
        agreement_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V8Function v8Function = getZKDocument().genContext("document.open('pangyou/agreement.xml')");
                getZKDocument().invokeWithContext(v8Function);
                v8Function.release();
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(layoutManager);
        context_tv1.setText("服务中产生其他费用，请自行商议解决" + "\n" + "服务的时间地点如有变化，双方需要及时通知对方" + "\n" + "服务期间出现任何问题请及时联系客服小助手");
    }

    @Override
    public void fillData(Element selfElement) {
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "img":
                    //  img.init(getZKDocument(), element);
                    //  mChildList.put("img", img.getThisV8());
                    break;
            }
        }

        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "click":
                    onClickListener = mZkDocument.genContextFn(value);
                    break;
            }
        }
    }

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
            if (parameters.length() >= 0) {
                object = parameters.getObject(0);
                Log.e("TTTTTTT", object.getString("nickname") + "");
                nikename.setText(object.getString("nickname") + "");
                tv_credit.setText("下单评分" + object.get("userCharm").toString() + "");
                complete_tv.setText("信用等级" + object.get("userCredit").toString() + "分");
                out_time.setText(object.get("outTime").toString() + "小时内完成");
                createTime.setText(object.get("createTime").toString());
                reward.setText("￥" + object.get("reward").toString() + "(" + object.get("otherReward").toString() + ")");
                Glide.with(getContext())
                        .load(object.getString("img"))
                        .into(img);
                String serviceGrade = object.get("serviceGrade").toString();
                String credit = object.get("credit").toString();
                String otherReward = object.get("otherReward").toString();
                String bargaining = object.get("bargaining").toString();
                String sex = object.get("sex").toString();
                String isAdvance = object.get("isAdvance").toString();
                String isNeedCert = object.get("isNeedCert").toString();

                String claim_tvs = "";

                if (bargaining.equals("1")) {
                    claim_tvs = claim_tvs + "可议价、";
                } else {
                    claim_tvs = claim_tvs + "不可议价、";
                }
                if (!sex.equals("all")) {
                    claim_tvs = claim_tvs + sex + "、";
                }

                claim_tvs = claim_tvs + "接单评分" + serviceGrade + "以上、";

                claim_tvs = claim_tvs + "信用等级" + credit + "以上、";

                if (isNeedCert.equals("1")) {
                    claim_tvs = claim_tvs + "需要技能证书";
                }
                claim_tv.setText(claim_tvs);
                String description = object.get("description").toString();
                content_tv.setText(description + "");
                try {
                    V8Object additionalMessage = object.getObject("additionalMessage");
                    V8Array image = additionalMessage.getArray("image");
                    List<String> imgs = new ArrayList<>();
                    imgs.clear();
                    for (int i = 0; i < image.length(); i++) {
                        imgs.add(image.getString(i));
                    }

                    if (imgs.size() == 0) {
                        rv.setVisibility(View.GONE);
                    }
                    ImageAdapter adapter = new ImageAdapter(getContext(), imgs);
                    rv.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String distanceA2B = object.get("distanceA2B") + "";
                if (distanceA2B.equals("undefined")) {
                    layout5.setVisibility(View.GONE);
                    layout10.setVisibility(View.GONE);
                } else {
                    String s = distanceA2B.toString();
                    layout5.setVisibility(View.VISIBLE);
                    layout10.setVisibility(View.VISIBLE);
                    start_mine_danc.setText("始发地距目的地约:" + s);
                }

                String addressB = object.get("addressB") + "";
                if (addressB.equals("undefined")) {
                    layout3.setVisibility(View.GONE);
                } else {
                    V8Object addressB1 = object.getObject("addressB");
                    String distance = addressB1.get("distance") + "";
                    layout3.setVisibility(View.VISIBLE);
                    end_den.setText("距您约:" + distance);
                    String name = addressB1.getString("name");
                    end_name.setText(name + "");
                }

                String addressA = object.get("addressA") + "";
                if (addressA.equals("undefined")) {
                    layout2.setVisibility(View.GONE);
                } else {
                    V8Object addressA1 = object.getObject("addressA");
                    String distance = addressA1.get("distance") + "";
                    layout2.setVisibility(View.VISIBLE);
                    start_den.setText("距您约:" + distance);
                    String name = addressA1.getString("name");
                    address_start.setText(name + "");
                }
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
}
