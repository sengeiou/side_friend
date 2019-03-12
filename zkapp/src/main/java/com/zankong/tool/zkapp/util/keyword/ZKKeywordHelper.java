package com.zankong.tool.zkapp.util.keyword;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.zankong.tool.zkapp.ZKToolApi;

/**
 * Created by YF on 2018/8/31.
 */

public class ZKKeywordHelper {
    private Context mContext;
    public static int keywordHeight = 0;

    public ZKKeywordHelper(Context context) {
        mContext = context;
        SoftKeyBoardListener();
    }

    public Context getContext() {
        return mContext;
    }

    private View rootView;//activity的根视图
    int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    private void SoftKeyBoardListener() {
        //获取activity的根视图
        rootView = ((Activity) getContext()).getWindow().getDecorView();

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int visibleHeight = r.height();
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        if (keywordHeight != rootViewVisibleHeight - visibleHeight){
                            keywordHeight = rootViewVisibleHeight - visibleHeight;
                            ZKToolApi.runtime.executeVoidScript("localStorage.put({keywordHeight:"+keywordHeight+"})");
                        }
                        onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (onSoftKeyBoardChangeListener != null) {
                        if (keywordHeight != visibleHeight - rootViewVisibleHeight){
                            keywordHeight = visibleHeight - rootViewVisibleHeight;
                            ZKToolApi.runtime.executeVoidScript("localStorage.put({keywordHeight:"+keywordHeight+"})");
                        }
                        onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }

    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener;
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }
}
