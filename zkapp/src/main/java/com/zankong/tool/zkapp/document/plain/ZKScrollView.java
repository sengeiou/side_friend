package com.zankong.tool.zkapp.document.plain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.zankong.tool.zkapp.util.pull_refresh_layout.OnPullAble;

public class ZKScrollView extends ScrollView implements OnPullAble {
    public ZKScrollView(Context context) {
        super(context);
    }

    public ZKScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Boolean canPullDown = false,
            canPullUp = false;

    public void setCanPullDown(Boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public void setCanPullUp(Boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0 && canPullDown;
    }

    @Override
    public boolean canPullUp() {
        return getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()) && canPullUp;
    }

    @Override
    public View getView() {
        return this;
    }
}
