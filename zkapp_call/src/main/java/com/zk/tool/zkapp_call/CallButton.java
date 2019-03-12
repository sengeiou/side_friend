package com.zk.tool.zkapp_call;//package com.zk.tool.zkapp_call.old;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by YF on 2018/2/27.
 */

public class CallButton extends RelativeLayout {
    private Button mButton;
    private TextView mTextView;
    public CallButton(Context context) {
        super(context);
    }

    private void initView(){
        addView(mButton);
        addView(mTextView);
    }

    public Button getButton() {
        return mButton;
    }

    public void setButton(Button button) {
        mButton = button;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }
}
