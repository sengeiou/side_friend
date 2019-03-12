package com.zankong.tool.zkapp.v8fn;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import okhttp3.FormBody;


/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKFormData implements ZKV8Fn {
    private  V8Function FormData;

    public ZKFormData() {
        FormData = new V8Function(ZKToolApi.runtime, new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                receiver.registerJavaMethod((receiver2, parameters2) -> {
                    try{
                        Util.log(receiver2.getKeys().toString());
                        for (String s : receiver2.getKeys()) {
                            Util.log(s);
                        }
                        V8Object data = new V8Object(ZKToolApi.runtime);
                        data.add(parameters2.getString(0),parameters2.getString(1));
                        receiver2.getArray("value").push(data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return receiver2;
                },"append");
                receiver.add("value",new V8Array(ZKToolApi.runtime));
                return null;
            }
        });
    }

    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.add("FormData",FormData);
    }


}
