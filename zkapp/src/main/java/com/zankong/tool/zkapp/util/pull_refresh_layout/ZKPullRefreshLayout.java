package com.zankong.tool.zkapp.util.pull_refresh_layout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.zankong.tool.zkapp.util.Util;

import java.util.Timer;
import java.util.TimerTask;


public class ZKPullRefreshLayout extends RelativeLayout {


    public static final int STATUS_INIT = 0,                    //初始状态
                            STATUS_REFRESHING = 1,              //正在刷新
                            STATUS_LOADING = 2,                 //正在加载
                            STATUS_RELEASE_REFRESH = 3,        //释放刷新
                            STATUS_RELEASE_LOAD = 4,            //释放加载
                            STATUS_END = 5;                     //操作完毕
    private int currentState = STATUS_INIT;                     //当前状态

    public static final int SUCCEED = 1,        //刷新成功
                              FAIL = 0;            //刷新失败
    public int currentRefreshState = SUCCEED ;  //当前刷新状态

    private float  lastY ,                 //上一次的Y坐标
                    pullDownY,              //下拉距离
                    pullUpY,                //上拉距离
                    refreshDist = 200,     //刷新释放距离
                    loadDist = 200,        //加载释放距离
                    moveSpeed = 8,         //回滚速度
                    radio = 2;             //手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化


    private boolean isTouch,                          //是否是刷新过程中的滑动
                      isFirstLayout = true;           //是否是第一次指定布局


    private boolean isMultipoint,           //是否是多点触碰
                     canPullDown,            //可以下拉
                     canPullUp;               //可以上拉

    private RefreshTimer mRefreshTimer;

    private OnRefreshingStartListener mOnRefreshingStartListener;
    private OnLoadingStartListener mOnLoadingStartListener;



    private OnRefreshStateChangeListener mRefreshLayout;       //上边的布局
    private OnPullAble mPullAble;                              //中间的可滑动布局
    private OnRefreshStateChangeListener mLoadLayout;          //下边的布局

    public ZKPullRefreshLayout(Context context) {
        super(context);
        initView();
    }

    public ZKPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZKPullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }



    //初始化布局
    private void initView(){
        mRefreshTimer = new RefreshTimer(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Util.log("timer ","hander start");
                moveSpeed = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                if (!isTouch){
                    if (currentState == STATUS_REFRESHING && pullDownY <= refreshDist){
                        pullDownY = refreshDist;
                        mRefreshTimer.cancel();
                    }else if (currentState == STATUS_LOADING && -pullUpY <= loadDist){
                        pullUpY = -loadDist;
                        mRefreshTimer.cancel();
                    }
                }
                if (pullDownY > 0)pullDownY -= moveSpeed;
                else if (pullUpY < 0)pullUpY += moveSpeed;
                if (pullDownY < 0){
                    pullDownY = 0;
                    if (currentState != STATUS_REFRESHING && currentState != STATUS_LOADING)
                        changeState(STATUS_INIT);
                    mRefreshTimer.cancel();
                    requestLayout();
                }
                if (pullUpY  > 0){
                    pullUpY = 0;
                    if (currentState == STATUS_REFRESHING && currentState == STATUS_LOADING)
                        changeState(STATUS_INIT);
                    mRefreshTimer.cancel();
                    requestLayout();
                }
                requestLayout();
                // 没有拖拉或者回弹完成
                if (pullDownY + Math.abs(pullUpY) == 0) mRefreshTimer.cancel();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFirstLayout){
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof OnPullAble) {
                    mPullAble = (OnPullAble) getChildAt(i);
                }else if (getChildAt(i) instanceof OnRefreshStateChangeListener){
                    if (mPullAble == null) {
                        mRefreshLayout = (OnRefreshStateChangeListener) getChildAt(i);
                        refreshDist = mRefreshLayout.getScrollDist();
                    }else {
                        mLoadLayout = (OnRefreshStateChangeListener) getChildAt(i);
                        loadDist = mLoadLayout.getScrollDist();
                    }
                }
            }
            isFirstLayout = false;
        }
        mRefreshLayout.getView().layout( 0,      (int) (pullDownY + pullUpY) - mRefreshLayout.getView().getMeasuredHeight(),    mRefreshLayout.getView().getMeasuredWidth(),     (int) (pullDownY + pullUpY));
        mPullAble.getView().layout(      0,         (int) (pullDownY + pullUpY),                                                    mPullAble.getView().getMeasuredWidth(),        (int) (pullDownY + pullUpY) + mPullAble.getView().getMeasuredHeight());
        mLoadLayout.getView().layout(    0,      (int) (pullDownY + pullUpY) + mPullAble.getView().getMeasuredHeight(),          mLoadLayout.getView().getMeasuredWidth(),      (int) (pullDownY + pullUpY) + mPullAble.getView().getMeasuredHeight() + mLoadLayout.getView().getMeasuredHeight());
    }

    //根据状态设置布局
    private void changeState(int state){
        Util.log("releaseLoad:changeState");
        Util.log("releaseLoad:",state+"");

        currentState = state;
        switch (currentState){
            case STATUS_INIT:
                Util.log("刷新初始化");

                mRefreshLayout.init();
                mLoadLayout.init();
                break;
            case STATUS_REFRESHING:            //正在刷新
                Util.log("正在刷新");

                mRefreshLayout.refreshing();
                break;
            case STATUS_LOADING :                //正在加载
                Util.log("正在加载");

                mLoadLayout.loading();
                break;
            case STATUS_RELEASE_REFRESH :        //释放刷新
                Util.log("释放刷新");

                mRefreshLayout.releaseRefresh();
                break;
            case STATUS_RELEASE_LOAD :           //释放加载
                Util.log("释放加载");

                mLoadLayout.releaseLoad();
                break;
            case STATUS_END:                    //操作完毕
                Util.log("刷新结束");
                mRefreshLayout.end(currentRefreshState);
                mLoadLayout.end(currentRefreshState);
                break;
        }
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!mPullAble.canPullDown() && !mPullAble.canPullUp())return super.dispatchTouchEvent(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                mRefreshTimer.cancel();
                isMultipoint = false;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                isMultipoint = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMultipoint){
                    // 可以下拉，正在加载时不能下拉
                    // 对实际滑动距离做缩小，造成用力拉的感觉
                    if (pullDownY > 0 || canPullDown && currentState != STATUS_LOADING && mPullAble.canPullDown()){
                        pullDownY += (ev.getY() - lastY)/radio;
                        if (pullDownY < 0){
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight()) pullDownY = getMeasuredHeight();
                        if (currentState == STATUS_REFRESHING) isTouch = true;
                    }else if (pullUpY < 0 || mPullAble.canPullUp() && canPullUp && currentState != STATUS_REFRESHING){
                        pullUpY += (ev.getY() - lastY)/radio;
                        if (pullUpY > 0){
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight()) pullUpY = - getMeasuredHeight();
                        if (currentState == STATUS_LOADING) isTouch = true;
                    }else
                        releasePull();
                }else
                    isMultipoint = false;
                lastY = ev.getY();
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0) requestLayout();
                if (pullDownY > 0){
                    // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                    if (pullDownY <= refreshDist && (currentState == STATUS_RELEASE_REFRESH || currentState == STATUS_END)) changeState(STATUS_INIT);
                    if (pullDownY >= refreshDist && currentState == STATUS_INIT)changeState(STATUS_RELEASE_REFRESH );
                }else if (pullUpY < 0){
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadDist && (currentState == STATUS_RELEASE_LOAD || currentState == STATUS_END)) changeState(STATUS_INIT);
                    if (-pullUpY >= loadDist && currentState == STATUS_INIT) changeState(STATUS_RELEASE_LOAD);
                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                // 防止下拉过程中误触发长按事件和点击事件
                if ((pullDownY + Math.abs(pullUpY)) > 8) ev.setAction(MotionEvent.ACTION_CANCEL);
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadDist)isTouch = false;
                if (currentState == STATUS_RELEASE_REFRESH){
                    changeState(STATUS_REFRESHING);
                    if (mOnRefreshingStartListener != null)mOnRefreshingStartListener.refresh();
                }else if (currentState == STATUS_RELEASE_LOAD){
                    changeState(STATUS_LOADING);
                    if (mOnLoadingStartListener != null)mOnLoadingStartListener.load();
                }
                hide();
                break;
            default:
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }


    public void stop(int resultState){
        currentRefreshState = resultState;
        changeState(STATUS_END);
        // 刷新结果停留1秒
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                hide();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }




    //延时关闭
    private void hide(){
        mRefreshTimer.schedule(5);
    }





    private class RefreshTimer {
        private Handler mHandler;
        private Timer mTimer;
        private MyTask mTask;

        public RefreshTimer(Handler handler) {
            mHandler = handler;
            mTimer = new Timer();
        }

        public void schedule(long period){
            if (mTask != null){
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(mHandler);
            mTimer.schedule(mTask,0,period);
        }

        public void cancel(){
            if (mTask != null){
                mTask.cancel();
                mTask = null;
            }
        }

        private class MyTask extends TimerTask{
            private Handler mHandler;

            public MyTask(Handler handler) {
                mHandler = handler;
            }

            @Override
            public void run() {
                mHandler.obtainMessage().sendToTarget();
            }
        };
    }




    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }


    public void setOnRefreshingStartListener(OnRefreshingStartListener onRefreshingStartListener) {
        mOnRefreshingStartListener = onRefreshingStartListener;
    }

    public void setOnLoadingStartListener(OnLoadingStartListener onLoadingStartListener) {
        mOnLoadingStartListener = onLoadingStartListener;
    }

    //刷新接口
    public interface OnRefreshingStartListener{
        void refresh();
    }
    //加载接口
    public interface OnLoadingStartListener{
        void load();
    }
}
