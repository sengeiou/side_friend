package com.zankong.tool.side_friend.diy_view.mail_list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author Fsnzzz
 * @Created on 2018/11/20 0020 17:39
 */
public class MailListLayout extends LinearLayout {
    public MailListLayout(Context context) {
        super(context);
    }

    public MailListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MailListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
