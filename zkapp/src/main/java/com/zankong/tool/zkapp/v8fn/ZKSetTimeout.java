package com.zankong.tool.zkapp.v8fn;

import android.os.Handler;
import android.util.SparseArray;

import com.eclipsesource.v8.V8Function;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;


/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKSetTimeout implements ZKV8Fn {
    private int timeoutId = 1;
    private SparseArray<TimeoutBean> timeoutMap;
    private Handler jsHandler;

    public ZKSetTimeout() {
        timeoutMap = new SparseArray<>();
        jsHandler = ZKToolApi.getInstance().getJsHandler();

    }

    /**
     * 延时
     */
    private class TimeoutBean{
        private Runnable mRunnable;
        private V8Function mV8Function;
        private int time;
        public boolean isRun = false;

        private TimeoutBean(V8Function v8Function) {
            mV8Function = v8Function;
            this.time = time;
        }

        private void postDelay(int time){
            mRunnable = () -> {
                mV8Function.call(null);
                mV8Function.release();
                isRun = true;
            };
            jsHandler.postDelayed(mRunnable,time);
        }
        private void clear(){
            if (isRun)return;
            jsHandler.removeCallbacks(mRunnable);
            mV8Function.release();
        }
    }
    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            V8Function fn = (V8Function) parameters.getObject(0);
            int time = 0;
            if (parameters.length() > 1 && parameters.get(1) instanceof Integer){
                time = parameters.getInteger(1);
            }
            TimeoutBean timeoutBean = new TimeoutBean(fn);
            timeoutBean.postDelay(time);
            timeoutId++;
            timeoutMap.append(timeoutId,timeoutBean);
            return timeoutId;
        },"setTimeout");
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() > 0 && parameters.get(0) instanceof Integer){
                int tid = parameters.getInteger(0);
                timeoutMap.get(tid).clear();
                timeoutMap.delete(tid);
            }
        },"clearTimeout");
    }
}
