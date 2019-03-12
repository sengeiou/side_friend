package com.zankong.tool.zkapp.util.pull_refresh_layout;

import android.view.View;

public interface OnRefreshStateChangeListener {
    void init();
    void loading();
    void refreshing();
    void releaseRefresh();
    void releaseLoad();
    void end(int state);
    float getScrollDist();
    View getView();
}
