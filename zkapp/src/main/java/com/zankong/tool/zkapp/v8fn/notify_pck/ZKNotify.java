package com.zankong.tool.zkapp.v8fn.notify_pck;

import android.app.NotificationManager;
import android.content.Context;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/7/16.
 */

@ZKAppV8
public class ZKNotify implements ZKV8Fn {
    private V8Object NotificationV8;
    private NotificationManager mNotifyMgr;
    private int notifyId = 100;

    public ZKNotify() {
        mNotifyMgr = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationV8 = new V8Object(ZKToolApi.runtime);
    }

    private Context getContext() {
        return ZKToolApi.getInstance().getContext();
    }

    @Override
    public void addV8Fn() {
        NotificationV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object arguments;
                if(parameters.length()>1){
                    arguments = parameters.getObject(1);
                }else {
                    arguments = new V8Object(ZKToolApi.runtime);
                }
                NotifyHelper.notification(parameters.getObject(0),arguments);
                return null;
            }
        }, "notify");
        ZKToolApi.runtime.add("Notification", NotificationV8);
    }
}
