package com.zankong.tool.zkapp.v8fn.record_pck;

import android.media.MediaRecorder;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by YF on 2018/9/12.
 */

@ZKAppV8
public class ZKRecord implements ZKV8Fn {

    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.add("Record",new V8Function(ZKToolApi.runtime,new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String dir = ZKToolApi.getInstance().getContext().getPackageName();
                if (parameters.length() >= 1) {
                    dir = parameters.getString(0);
                }
                RecordHelper zkRecord = RecordHelper.getInstance(dir);
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        zkRecord.prepare();
                        return null;
                    }
                },"prepare");
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        zkRecord.release();
                        return null;
                    }
                },"release");
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        zkRecord.stop();
                        return null;
                    }
                },"stop");
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        zkRecord.cancel();
                        return null;
                    }
                },"cancel");
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        return null;
                    }
                },"setOut");
                receiver.registerJavaMethod(new JavaCallback() {
                    @Override
                    public Object invoke(V8Object receiver, V8Array parameters) {
                        Util.log("url",zkRecord.getCurrentFilePathString());
                        return zkRecord.getCurrentFilePathString();
                    }
                },"getUrl");
                return null;
            }
        }));
    }
}
