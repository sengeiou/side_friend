package com.zankong.tool.side_friend.diy_v8fn;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/25.
 */

@ZKAppV8
public class v8test implements ZKV8Fn{
    private V8Object test;
    @Override
    public void addV8Fn() {
        Util.log("自定义v8方法 ,test");
        test = new V8Object(ZKToolApi.runtime);
        test.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Util.log("测试");
                return null;
            }
        },"a")
        ;
        ZKToolApi.runtime.add("test",test);
    }
}
