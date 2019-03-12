package com.zankong.tool.zkapp.document.listview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by YF on 2018/6/27.
 */

public class ZKRefreshView extends SwipeRefreshLayout {
    private final int mScaledTouchSlop;// 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
    private ZKRecycleView mRecyclerView;
    private OnLoadListener mOnLoadListener;//上拉加载事件监听
    private boolean isLoading, //是否正在加载
            isLoadFinish = true;//是否一次加载已经结束

    public ZKRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRecyclerView == null) {
            if (getChildCount() > 0) {
                if (getChildAt(0) instanceof ZKRecycleView) {
                    mRecyclerView = (ZKRecycleView) getChildAt(0);
                }
            }
        }
    }

    private float mDownY;
    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLoadFinish){
                    if (canLoad(mDownY,ev.getY())){
                        isLoading = true;
                        mRecyclerView.addFooterView();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬手时判断前一次的加载事件是否已经结束
                if (isLoadFinish){
                    if (mDownY - ev.getY() >= mScaledTouchSlop && isLoading){
                        mOnLoadListener.onLoad();
                        isLoadFinish = false;
                    }else {
                        mRecyclerView.removeFooterView();
                        isLoading = false;
                        isLoadFinish = true;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断上拉状态
    private boolean canLoad(float oldY,float newY){
        //是否向上滑
        boolean isUp = (oldY - newY) >= mScaledTouchSlop;
        assert mRecyclerView != null;
        //是否滑到底
        boolean isEndItem = !mRecyclerView.canScrollVertically(1);
        //是否item总高度比recycleView小
        boolean isMore = mRecyclerView.canScrollVertically(1) || mRecyclerView.canScrollVertically(-1);
        //是否有load属性
        boolean canLoad = mOnLoadListener!=null;
        return isUp && isEndItem && isMore && !isLoading && canLoad;
    }


    /**
     * 上拉加载的接口回调
     */

    public void stopLoad(){
        mRecyclerView.removeFooterView();
        isLoading = false;
        isLoadFinish = true;
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }
}
