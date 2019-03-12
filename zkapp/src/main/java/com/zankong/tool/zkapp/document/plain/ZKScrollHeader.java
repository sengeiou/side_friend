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
import com.zankong.tool.zkapp.util.pull_refresh_layout.OnRefreshStateChangeListener;
import com.zankong.tool.zkapp.util.pull_refresh_layout.ZKPullRefreshLayout;

public class ZKScrollHeader extends RelativeLayout implements OnRefreshStateChangeListener {
    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private View refreshingView;
    // 刷新结果图标
    private View refreshStateImageView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;



    public ZKScrollHeader(Context context) {
        super(context);
        initView();
    }

    public ZKScrollHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZKScrollHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 初始化下拉布局
        pullView = findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) findViewById(R.id.state_tv);
        refreshingView = findViewById(R.id.refreshing_icon);
        refreshStateImageView = findViewById(R.id.state_iv);
    }

    private void initView(){
        // 初始化上拉布局
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getContext(), R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getContext(), R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    @Override
    public void init() {
        // 下拉布局初始状态
        refreshStateImageView.setVisibility(View.GONE);
        refreshStateTextView.setText(R.string.pull_to_refresh);
        pullView.clearAnimation();
        pullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void loading() {

    }

    @Override
    public void refreshing() {
// 正在刷新状态
        pullView.clearAnimation();
        refreshingView.setVisibility(View.VISIBLE);
        pullView.setVisibility(View.INVISIBLE);
        refreshingView.startAnimation(refreshingAnimation);
        refreshStateTextView.setText(R.string.refreshing);
    }

    @Override
    public void releaseRefresh() {
// 释放刷新状态
        refreshStateTextView.setText(R.string.release_to_refresh);
        pullView.startAnimation(rotateAnimation);
    }

    @Override
    public void releaseLoad() {

    }

    @Override
    public void end(int refreshResult) {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case ZKPullRefreshLayout.SUCCEED:
                // 刷新成功
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_succeed);
                refreshStateImageView.setBackgroundResource(R.drawable.refresh_succeed);
                break;
            case ZKPullRefreshLayout.FAIL:
            default:
                // 刷新失败
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_fail);
                refreshStateImageView.setBackgroundResource(R.drawable.refresh_failed);
                break;
        }
    }

    @Override
    public float getScrollDist() {
        return findViewById(R.id.firstChild).getMeasuredHeight();
    }

    @Override
    public View getView() {
        return this;
    }
}
