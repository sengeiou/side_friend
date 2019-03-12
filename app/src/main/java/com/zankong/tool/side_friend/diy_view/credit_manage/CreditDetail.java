package com.zankong.tool.side_friend.diy_view.credit_manage;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.evaluate.BatingBarView1;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fsnzzz
 * @Created on 2018/11/27 0027 11:48
 */
public class CreditDetail extends ZKViewAgent {
    private RecyclerView top_rv, bootom_rv_02, centre_rv, bottom_rv_01, centre_rv2;
    private RoundedImageView centre_rimg, bottom_rimg;
    private BatingBarView1 rating;
    private TextView top_type_tv;
    private TextView top_time_tv;
    private TextView top_startAddress_tv;
    private TextView top_endAddress_tv;
    private TextView top_claim_tv;
    private TextView top_detail_tv;
    private TextView centre_name;
    private TextView centre_service_charge;
    private TextView centre_gratuity;
    private TextView centre_release_time;
    private TextView centre_reason_tv;
    private TextView bottom_name;
    private TextView bottom_service_charge;
    private TextView bottom_stat_time;
    private TextView bottom_end_time;
    private TextView bottom_reason;

    public CreditDetail(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.credit_detail_layout);
        top_type_tv = (TextView) findViewById(R.id.top_type_tv);
        top_time_tv = (TextView) findViewById(R.id.top_time_tv);
        top_startAddress_tv = (TextView) findViewById(R.id.top_startAddress_tv);
        top_endAddress_tv = (TextView) findViewById(R.id.top_endAddress_tv);
        top_claim_tv = (TextView) findViewById(R.id.top_claim_tv);
        top_rv = (RecyclerView) findViewById(R.id.top_rv);
        centre_rimg = (RoundedImageView) findViewById(R.id.centre_rimg);
        top_detail_tv = (TextView) findViewById(R.id.top_detail_tv);
        centre_name = (TextView) findViewById(R.id.centre_name);
        centre_service_charge = (TextView) findViewById(R.id.centre_service_charge);
        centre_gratuity = (TextView) findViewById(R.id.centre_gratuity);
        centre_release_time = (TextView) findViewById(R.id.centre_release_time);
        centre_rv = (RecyclerView) findViewById(R.id.centre_rv);
        bootom_rv_02 = (RecyclerView) findViewById(R.id.bootom_rv_02);
        centre_rv2 = (RecyclerView) findViewById(R.id.centre_rv2);
        bottom_rv_01 = (RecyclerView) findViewById(R.id.bottom_rv_01);
        centre_reason_tv = (TextView) findViewById(R.id.centre_reason_tv);
        bottom_name = (TextView) findViewById(R.id.bottom_name);
        bottom_service_charge = (TextView) findViewById(R.id.bottom_service_charge);
        bottom_stat_time = (TextView) findViewById(R.id.bottom_stat_time);
        bottom_end_time = (TextView) findViewById(R.id.bottom_end_time);
        bottom_reason = (TextView) findViewById(R.id.bottom_reason);
        rating = (BatingBarView1) findViewById(R.id.rb);
        bottom_rimg = (RoundedImageView) findViewById(R.id.bottom_rimg);


        GridLayoutManager gridLayout1 = new GridLayoutManager(getContext(), 2);
        GridLayoutManager gridLayout2 = new GridLayoutManager(getContext(), 2);
        centre_rv.setLayoutManager(gridLayout1);
        bottom_rv_01.setLayoutManager(gridLayout2);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        bootom_rv_02.setLayoutManager(linearLayoutManager3);
        top_rv.setLayoutManager(linearLayoutManager1);
        centre_rv2.setLayoutManager(linearLayoutManager2);


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
            if (parameters.length() != 0) {
                V8Object object = parameters.getObject(0);
                V8Object senderObj = object.getObject("sender");
                V8Object receiverObj = object.getObject("receiver");
                V8Object taskObj = object.getObject("task");

                if (taskObj != null) {
                    String type = taskObj.getString("type");
                    String style = taskObj.getString("style");
                    top_type_tv.setText("【" + type + "】" + style);
                    V8Object demandObj = taskObj.getObject("demand");
                    String addressA = demandObj.getString("addressA");
                    String addressB = demandObj.getString("addressB");
                    top_startAddress_tv.setText(addressA + "");
                    top_endAddress_tv.setText(addressB + "");
                    String receiverTime = demandObj.getString("receiverTime");
                    bottom_stat_time.setText(receiverTime + "");
                    String endTime = demandObj.getString("endTime");
                    bottom_end_time.setText("" + endTime);
                    String outTime = demandObj.get("outTime").toString();
                    top_time_tv.setText(outTime + "小时");
                    String finishTime = demandObj.getString("finishTime");
                    String description = demandObj.getString("description");
                    String startTime = demandObj.getString("startTime");
                    String createdAt = demandObj.getString("createdAt");
                    centre_release_time.setText(createdAt + "");
                    String reward = demandObj.get("reward").toString() + "";
                    if (reward != null) {
                        centre_service_charge.setText("服务费:" + reward + "元");
                        bottom_service_charge.setText("服务费:" + reward + "元");
                    } else {

                        centre_service_charge.setVisibility(View.INVISIBLE);
                        bottom_service_charge.setVisibility(View.INVISIBLE);
                    }
                    String claims = "";
                    if (!demandObj.getString("sex").equals("all")) {
                        claims = claims + "/" + demandObj.getString("sex");
                    }
                    if (demandObj.getInteger("bargaining") == 1) {
                        claims = claims + "/不可议价";
                    } else {
                        claims = claims + "/可议价";
                    }
                    claims = claims + "/信用等级" + demandObj.get("credit") + "以上";
                    top_claim_tv.setText(claims);
                    top_detail_tv.setText(description + "");
                    V8Object additionalMessage = senderObj.getObject("additionalMessage");
                    if (additionalMessage != null) {
                        String text = additionalMessage.getString("text");
                        String[] text0 = text.split(",");
                        V8Array image = additionalMessage.getArray("image");
                        List<String> imgs = new ArrayList<>();
                        imgs.clear();
                        for (int i = 0; i < image.length(); i++) {
                            String string = image.getString(i);
                            imgs.add(string);
                        }
                        ImageAdapter imageAdapter = new ImageAdapter(getContext(), imgs);
                        top_rv.setAdapter(imageAdapter);
                    }
                }

                if (senderObj != null) {
                    String img1 = senderObj.getString("img");
                    Glide.with(getContext())
                            .load(img1)
                            .into(centre_rimg);
                    String nickname = senderObj.getString("nickname");
                    centre_name.setText(nickname + "");
                    V8Object evaluateObj1 = senderObj.getObject("evaluate");
                    if (evaluateObj1 != null) {
                        String content = evaluateObj1.getString("content");
                        String isGratuity = evaluateObj1.get("isGratuity").toString();

                        centre_reason_tv.setText(content + "");
                        centre_gratuity.setText("打赏金:" + isGratuity + "元");
                    }
                    V8Object additionalMessage = senderObj.getObject("additionalMessage");
                    if (additionalMessage != null) {
                        String text = additionalMessage.getString("text");
                        String[] text1 = text.split(",");
                        V8Array image = additionalMessage.getArray("image");
                        List<String> imgs = new ArrayList<>();
                        imgs.clear();
                        TextAdapter textAdapter = new TextAdapter(getContext(), text1);
                        centre_rv.setAdapter(textAdapter);
                        for (int i = 0; i < image.length(); i++) {
                            String string = image.getString(i);
                            imgs.add(string);
                        }

                        ImageAdapter imageAdapter = new ImageAdapter(getContext(),imgs);
                        centre_rv2.setAdapter(imageAdapter);

                    }
                }
                if (receiverObj != null) {
                    String img2 = receiverObj.getString("img");
                    Glide.with(getContext())
                            .load(img2)
                            .into(bottom_rimg);
                    bottom_name.setText("" + receiverObj.getString("nickname"));
                    V8Object evaluate2 = receiverObj.getObject("evaluate");
                    if (evaluate2 != null) {
                        String content2 = evaluate2.getString("content");
                        bottom_reason.setText("" + content2);
                        int star = evaluate2.getInteger("star");
                        rating.setSelectedNumber(star);
                    }
                    V8Object additionalMessage2 = receiverObj.getObject("additionalMessage");
                    if (additionalMessage2 != null) {
                        List<String> imgs = new ArrayList<>();
                        imgs.clear();
                        String text = additionalMessage2.getString("text");
                        String[] text2 = text.split(",");

                        TextAdapter adapter1 = new TextAdapter(getContext(), text2);
                        bottom_rv_01.setAdapter(adapter1);
                        V8Array image = additionalMessage2.getArray("image");
                        for (int i = 0; i < image.length(); i++) {
                            String imgs1 = image.getString(i);
                            imgs.add(imgs1);
                        }


                        ImageAdapter imageAdapter = new ImageAdapter(getContext(),imgs);
                        bootom_rv_02.setAdapter(imageAdapter);
                    }
                }

            }
            return null;
        }, "set");
    }
}
