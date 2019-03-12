package com.zankong.tool.side_friend.diy_view.wallet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zankong.tool.side_friend.R;

public class BillDialog extends Dialog {

    private LinearLayout layout_finish;
    private RecyclerView rv;

    public BillDialog(Context context) {
        super(context);
    }

    public BillDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bill_dialog_layout);
        initView();
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    private void initView() {
        layout_finish = findViewById(R.id.layout_finish);
        rv = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
        rv.setLayoutManager(gridLayoutManager);
        
        
    }
}
