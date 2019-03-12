package com.zankong.tool.side_friend.diy_view.button_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;

public class PayDialog extends Dialog implements View.OnClickListener{
    
    private final int  ARROW_LAYOUT = 0;
    private final int  PAY_WAY_LAYOUT = 1;
    private final int  CONFIRM_LAYOUT = 2;
    private DialogItemClick dialogItemClick1;
    private Context context;
    public PayDialog(Context context) {
        super(context);
    }

    public PayDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    public void onItemClick(DialogItemClick dialogItemClick){
       dialogItemClick1 = dialogItemClick;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_dialog_layout);
        initView();
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
    private void initView(){
        LinearLayout layout_arrow = findViewById(R.id.layout_arrow);
        RelativeLayout confirm_layout = findViewById(R.id.confirm_layout);
        RelativeLayout pay_way_layout = findViewById(R.id.pay_way_layout);
        TextView tv_money = findViewById(R.id.tv_money);
        layout_arrow.setOnClickListener(this);
        confirm_layout.setOnClickListener(this);
        pay_way_layout.setOnClickListener(this);
        tv_money.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_arrow:
                if (dialogItemClick1 != null)
                    dialogItemClick1.onClick(ARROW_LAYOUT);
                break;
            case R.id.pay_way_layout:
                if (dialogItemClick1 != null)
                dialogItemClick1.onClick(PAY_WAY_LAYOUT);
                break;
            case R.id.confirm_layout:
                if (dialogItemClick1 != null)
                dialogItemClick1.onClick(CONFIRM_LAYOUT);
                break;
        }
    }
    public interface DialogItemClick{
        void onClick(int position);
    }
}
