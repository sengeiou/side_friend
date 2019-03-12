package com.zankong.tool.zkapp.v8fn;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKStack implements ZKV8Fn {
    private V8Object Stack;

    public ZKStack() {
        Stack = new V8Object(ZKToolApi.runtime);
    }

    @Override
    public void addV8Fn() {
        Stack.registerJavaMethod((receiver, parameters) -> {
            int index = ZKToolApi.getInstance().getActivities().size() - 1;
            if (parameters.length() > 0 && parameters.get(0) instanceof Integer){
                int len = parameters.getInteger(0);
                if (len >= 0 && len <= index){
                    index = len;
                }
            }
            return V8Utils.createFrom(ZKToolApi.getInstance().getActivities().get(index).getZKDocument().getDocument());
        },"get");
        Stack.registerJavaMethod((receiver, parameters) -> {
            return ZKToolApi.getInstance().getActivities().size();
        },"size");
        ZKToolApi.runtime.add("stack",Stack);
    }
}
