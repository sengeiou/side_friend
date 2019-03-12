package com.zankong.tool.side_friend.diy_view.evaluate;

import android.app.Notification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8Function;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class SenderPlayTourView extends ZKViewAgent implements View.OnClickListener {

    private RelativeLayout bottom_layout;
    private TextView number_tv;
    private LinearLayout layout_03;
    private LinearLayout layout_02;
    private LinearLayout layout_01;
    private LinearLayout layout_00;
    private TextView type_tv;
    private TextView day_tv;
    private TextView month_tv;
    private TextView orders_give;
    private TextView order_down;
    private TextView credit_tv;
    private TextView name;
    private LinearLayout img_layout;
    private PlayTourDialog dialog;
    private TextView money_tv0;
    private TextView money_tv1;
    private TextView money_tv2;

    public SenderPlayTourView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.sender_play_tour_layout);
        img_layout = (LinearLayout) findViewById(R.id.top_img_layout);
        name = (TextView) findViewById(R.id.name);
        credit_tv = (TextView) findViewById(R.id.credit_tv);
        order_down = (TextView) findViewById(R.id.order_down);
        orders_give = (TextView) findViewById(R.id.orders_give);
        month_tv = (TextView) findViewById(R.id.month_tv);
        day_tv = (TextView) findViewById(R.id.day_tv);
        type_tv = (TextView) findViewById(R.id.type_tv);


        layout_00 = (LinearLayout) findViewById(R.id.month_layout0);
        layout_01 = (LinearLayout) findViewById(R.id.month_layout1);
        layout_02 = (LinearLayout) findViewById(R.id.month_layout2);
        layout_03 = (LinearLayout) findViewById(R.id.month_layout3);


        money_tv0 = (TextView) findViewById(R.id.money_tv0);
        money_tv1 = (TextView) findViewById(R.id.money_tv1);
        money_tv2 = (TextView) findViewById(R.id.money_tv2);
        TextView money_tv3 = (TextView) findViewById(R.id.money_tv3);


        number_tv = (TextView) findViewById(R.id.number_tv);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        img_layout.setOnClickListener(this);
        layout_00.setOnClickListener(this);
        layout_01.setOnClickListener(this);
        layout_02.setOnClickListener(this);
        layout_03.setOnClickListener(this);
        bottom_layout.setOnClickListener(this);

        if (dialog == null){
            dialog = new PlayTourDialog(getContext(),R.style.dialog);
        }
        
        
        dialog.onClickListener(new PlayTourDialog.DialogOnClickListener() {
            @Override
            public void onClick(int position,double money) {
                if (position == 1){
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                        if (money != 0){
                            Log.e("dddddd",money+"");
                        }else {
                            Log.e("ddddddd","没钱");
                        }
                    }
                }else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
        
       

    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_img_layout:
                V8Function v8Function = getZKDocument().genContext("document.finish()");
                getZKDocument().invokeWithContext(v8Function);
                v8Function.release();
                break;
            case R.id.month_layout0:
                break;
            case R.id.month_layout1:
                break;
            case R.id.month_layout2:
                break;
            case R.id.month_layout3:
                if (!dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.bottom_layout:
                V8Function v8Function1 = getZKDocument().genContext("document.open('pangyou/task_sender_play_item.xml')");
                getZKDocument().invokeWithContext(v8Function1);
                v8Function1.release();
                break;
        }
    }
}
