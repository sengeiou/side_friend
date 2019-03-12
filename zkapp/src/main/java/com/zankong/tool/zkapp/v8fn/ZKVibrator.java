package com.zankong.tool.zkapp.v8fn;
import android.app.Service;
import android.os.Vibrator;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKVibrator implements ZKV8Fn {
    private Vibrator mVibrator;
    private V8Object vibrator;

    public ZKVibrator() {
        vibrator = new V8Object(ZKToolApi.runtime);
        mVibrator = (Vibrator) ZKToolApi.getInstance().getContext().getSystemService(Service.VIBRATOR_SERVICE);

    }

    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            if (!mVibrator.hasVibrator())return null;
            if (parameters.get(0) instanceof Integer) {
                mVibrator.vibrate(parameters.getInteger(0));
            }else {
                if (parameters.get(0) instanceof V8Array) {
                    V8Array array = parameters.getArray(0);
                    long[] patternLongs = new long[array.length()];
                    for (int i  = 0 ; i < array.length() ; i++){
                        patternLongs[i] = Long.valueOf(array.getInteger(i)+"");
                    }
                    int repeat = -1;
                    if (parameters.length() >= 2 && parameters.get(1) instanceof Integer){
                        repeat = parameters.getInteger(1);
                    }
                    mVibrator.vibrate(patternLongs,repeat);
                }
            }
            return null;
        },"vibrator");
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            if (!mVibrator.hasVibrator())return null;
            mVibrator.cancel();
            return null;
        },"cancel");
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            return mVibrator.hasVibrator();
        },"hasVibrator");
        ZKToolApi.runtime.add("vibrator",vibrator);
    }
}
