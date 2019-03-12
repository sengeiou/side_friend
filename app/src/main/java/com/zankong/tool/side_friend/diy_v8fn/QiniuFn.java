package com.zankong.tool.side_friend.diy_v8fn;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

@ZKAppV8
public class QiniuFn implements ZKV8Fn {
    V8Object Qiniu = new V8Object(ZKToolApi.runtime);
    @Override
    public void addV8Fn() {
        Qiniu.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String file = parameters.getString(0);

                return null;
            }
        }, "putFile");

        Qiniu.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String file = parameters.getString(0);

                return null;
            }
        }, "putFiles");
        ZKToolApi.runtime.add("Qiniu",Qiniu);
    }
}
