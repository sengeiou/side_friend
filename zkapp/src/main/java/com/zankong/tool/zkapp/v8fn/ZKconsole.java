package com.zankong.tool.zkapp.v8fn;

import android.util.Log;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/25.
 */

@ZKAppV8
public class ZKconsole implements ZKV8Fn {
    private V8Object console;

    public ZKconsole() {
        console = new V8Object(ZKToolApi.runtime);
    }

    @Override
    public void addV8Fn() {
        console.registerJavaMethod((receiver, v8Array) -> {
            String tag = "log";
            int dataIndex = 0;
            if (v8Array.length() == 2){
                tag = v8Array.getString(0);
                dataIndex = 1;
            }
            Log.d(tag, V8Utils.js2string(v8Array.get(dataIndex)));
            return null;
        },"log");
        ZKToolApi.runtime.add("console",console);
    }
}
