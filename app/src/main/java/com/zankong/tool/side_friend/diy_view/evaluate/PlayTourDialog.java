package com.zankong.tool.side_friend.diy_view.evaluate;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;

public class PlayTourDialog extends Dialog implements View.OnClickListener{

    private TextView confirm_tv;
    private TextView cancel_tv;
    
    private Context mContext;

    private DialogOnClickListener onClickListener;
    private EditText money_ev;
    private RelativeLayout arrow_layout;


    public PlayTourDialog(Context context) {
        super(context);
    }
    public PlayTourDialog( Context context, int themeResId) {
        super(context, themeResId);
        
        this.mContext = context;
        
    }
    

    public void onClickListener(DialogOnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_tour_dialog_layout);
        setCancelable(false);


        arrow_layout = findViewById(R.id.top_arrow_layout);
        money_ev = findViewById(R.id.money_ev);
        confirm_tv = findViewById(R.id.confirm_tv);
        cancel_tv = findViewById(R.id.cancel_tv);
        confirm_tv.setOnClickListener(this);
        cancel_tv.setOnClickListener(this);
        arrow_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_tv:
                if (onClickListener == null){
                }else 
                onClickListener.onClick(1,Double.parseDouble((money_ev.getText().toString().equals("") ? "0" : money_ev.getText().toString())));
                break;
            case R.id.cancel_tv:
            case R.id.top_arrow_layout:
                onClickListener.onClick(0,0);
                break;
        }
    }


    public interface DialogOnClickListener{
        void onClick(int position,double money);
        
    }
}
