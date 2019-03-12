package com.zk.tool.zkapp_update;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.xuexiang.xupdate.XUpdate;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

@ZKAppV8()
public class ZKUpdate implements ZKV8Fn {
    private V8Object Update = new V8Object(ZKToolApi.runtime);

    public ZKUpdate(){

    }

    @Override
    public void addV8Fn() {
        Update.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                XUpdate.newBuild(ZKToolApi.getInstance().getActivities().get(ZKToolApi.getInstance().getActivities().size() -1))
                        .updateUrl(parameters.getString(0))
                        .update();
                return null;
            }
        },"load");
        ZKToolApi.runtime.add("Update",Update);
    }
}
