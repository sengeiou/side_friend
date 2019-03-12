package com.zankong.tool.side_friend.diy_view.index_user;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zankong.tool.side_friend.R;

public class LookBigImgDialog extends Dialog {
    
    private Bitmap bitmap;
    public LookBigImgDialog(Context context) {
        super(context);
    }
    public LookBigImgDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LookBigImgDialog(Context context, int themeResId, Bitmap bitmap){
        super(context, themeResId);
        this.bitmap = bitmap;
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_img_dialog);
        ImageView img = findViewById(R.id.img);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        Glide.with(getContext())
                .asBitmap()
                .load(bitmap)
                .into(img);
    }
}
