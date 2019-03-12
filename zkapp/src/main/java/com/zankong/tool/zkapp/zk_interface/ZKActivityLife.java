package com.zankong.tool.zkapp.zk_interface;

/**
 * Created by YF on 2018/6/23.
 */

public interface ZKActivityLife {
    void onCreate();
    void onPause();
    void onStart();
    void onResume();
    void onStop();
    void onDestroy();
    void onRestart();
    boolean onBack();
}
