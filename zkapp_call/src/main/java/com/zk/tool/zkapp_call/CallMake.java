package com.zk.tool.zkapp_call;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zankong.tool.zkapp.util.Util;


/**
 * Created by YF on 2018/2/27.
 */

public class CallMake {
    private RelativeLayout mRelativeLayout;
    private View mView;
    private ZKCallActivity mZKCallActivity;
    private Button cancel;
    private TextView cancel_text;
    private boolean invalid = false;
    private float allDownY,allDownX;

    public CallMake(ZKCallActivity zkCallActivity){
        this.mZKCallActivity = zkCallActivity;
        initView();
        setOnClick();
    }


    private void initView(){
        mView = LayoutInflater.from(mZKCallActivity).inflate(R.layout.call_make, null);
        mRelativeLayout = (RelativeLayout) mView.findViewById(R.id.call_make_rel);
        cancel = (Button) mView.findViewById(R.id.cancel);
        cancel_text = (TextView) mView.findViewById(R.id.cancel_text);
    }

    public View getView() {
        return mView;
    }
    private void setOnClick(){
        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        invalid = false;
                        allDownY = event.getY();
                        allDownX = event.getX();
                        cancel_text.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case MotionEvent.ACTION_UP:
                        cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
                        if (invalid) {
                            break;
                        }
//                        ILVCallManager.getInstance().endCall(callId);
                        mZKCallActivity.cancel();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY() - allDownY;
                        float moveX= event.getX() - allDownX;
                        if ((moveY > Util.dip2px(30) || moveY < -(Util.dip2px(30)))) {
                            cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
                            invalid = true;
                        }
                        if (moveX > Util.dip2px(30) || moveX < - (Util.dip2px(30))){
                            cancel_text.setTextColor(Color.parseColor("#d6d6d6"));
                            invalid = true;
                        }
                        break;
                }
                return false;
            }
        });
    }
}
