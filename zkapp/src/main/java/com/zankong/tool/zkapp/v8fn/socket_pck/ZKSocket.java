package com.zankong.tool.zkapp.v8fn.socket_pck;

import android.content.Context;
import android.content.Intent;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.MessageEvent;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKSocket implements ZKV8Fn {
    private V8Object v8Socket;
    private Context mContext = ZKToolApi.getInstance().getContext();
    private HashMap<String,V8Function> eventBusFn  = new HashMap<>();

    public ZKSocket() {
        v8Socket = new V8Object(ZKToolApi.runtime);
        Intent intent = new Intent(mContext, ZKServiceSocket.class);
        mContext.startService(intent);
        ZKToolApi.getInstance().getContext().startService(intent);
        EventBus.getDefault().register(ZKSocket.this);
    }

    @Override
    public void addV8Fn() {
        v8Socket.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String event = parameters.getString(0);
                Object obj = parameters.get(1);
                emit(event,obj);
                return null;
            }
        },"emit");
        v8Socket.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                eventBusFn.put(parameters.getString(0), (V8Function) parameters.getObject(1));
                return null;
            }
        },"on");
        ZKToolApi.runtime.add("Socket",v8Socket);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        String event = messageEvent.getEvent();
        if(!messageEvent.isValid())return;
        if (eventBusFn.containsKey(event)) {
            Object call = Objects.requireNonNull(eventBusFn.get(event)).call(null, V8Utils.parse((String) messageEvent.getData()));
            if(call instanceof Boolean){
                messageEvent.setValid(!(Boolean) call);
            }else {
                messageEvent.setValid(false);
            }
        }
    }

    public static void emit(String event,Object obj){
        ZKServiceSocket.mSocket.emit(event,obj);
    }

}
