package com.zankong.tool.zkapp.v8fn;

import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

/**
 * Created by YF on 2018/6/28.
 */

@ZKAppV8
public class ZKAlert implements ZKV8Fn {
    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            try{
                if (parameters.length() > 0){
                    Util.showT(V8Utils.js2string(parameters.get(0)));
                }else {
                    Util.showT("null");
                }
            }catch (Exception e){
                e.printStackTrace();
                Util.showT("err");
            }
            return null;
        },"alert");
    }
}
