package com.zankong.tool.zkapp.document.plain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.pull_refresh_layout.OnRefreshStateChangeListener;
import com.zankong.tool.zkapp.util.pull_refresh_layout.ZKPullRefreshLayout;

public class ZKScrollFooter extends RelativeLayout implements OnRefreshStateChangeListener {
    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    private View noMore;

    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    public ZKScrollFooter(Context context) {
        super(context);
        initView();
    }

    public ZKScrollFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZKScrollFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void init() {
        // 上拉布局初始状态
        loadStateImageView.setVisibility(View.GONE);
        loadStateTextView.setText(R.string.pullup_to_load);
        pullUpView.clearAnimation();
        pullUpView.setVisibility(View.VISIBLE);
    }

    @Override
    public void loading() {
        // 正在加载状态
        pullUpView.clearAnimation();
        loadingView.setVisibility(View.VISIBLE);
        pullUpView.setVisibility(View.INVISIBLE);
        loadingView.startAnimation(refreshingAnimation);
        loadStateTextView.setText(R.string.loading);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 初始化上拉布局
        pullUpView = findViewById(R.id.pullup_icon);
        loadStateTextView = findViewById(R.id.loadstate_tv);
        loadingView = findViewById(R.id.loading_icon);
        loadStateImageView = findViewById(R.id.loadstate_iv);
        noMore = findViewById(R.id.noMore);
    }

    private void initView(){
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }


    @Override
    public void refreshing() {

    }

    @Override
    public void releaseRefresh() {

    }

    @Override
    public void releaseLoad() {
        Util.log("releaseLoad");
        // 释放加载
        loadStateTextView.setText(R.string.release_to_load);
        pullUpView.startAnimation(rotateAnimation);
    }

    @Override
    public void end(int refreshResult) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case ZKPullRefreshLayout.SUCCEED:
                // 加载成功
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_succeed);
                loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
                break;
            case ZKPullRefreshLayout.FAIL:
            default:
                // 加载失败
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_fail);
                loadStateImageView.setBackgroundResource(R.drawable.load_failed);
                break;
        }
    }

    @Override
    public float getScrollDist() {
//        return  120;
        return findViewById(R.id.firstChild).getMeasuredHeight();
    }

    @Override
    public View getView() {
        return this;
    }
}
