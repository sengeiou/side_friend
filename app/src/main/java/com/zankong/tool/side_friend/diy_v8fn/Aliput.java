package com.zankong.tool.side_friend.diy_v8fn;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;
import com.zankong.tool.zkapp_alipay.OSSFiles;
import com.zankong.tool.zkapp_alipay.OSSHelper;

import java.util.ArrayList;

@ZKAppV8
public class Aliput implements ZKV8Fn {
    private V8Object Aliput = new V8Object(ZKToolApi.runtime);
    public static OSSHelper ossHelper = new OSSHelper();
    @Override
    public void addV8Fn() {
        Aliput.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String key = parameters.getString(0);
                String file = parameters.getString(1);
                ossHelper.put(key,file);
                return null;
            }
        },"putFile");
        Aliput.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        ArrayList<OSSFiles> ossFiles = new ArrayList<>();
                        V8Array array = parameters.getArray(0);
                        for (int i = 0; i < array.length(); i++) {
                            OSSFiles ossFile = new OSSFiles();
                            V8Object object = array.getObject(i);
                            ossFile.setKey(object.getString("key"));
                            ossFile.setFile(object.getString("file"));
                            ossFiles.add(ossFile);
                        }
                        ossHelper.putFiles(ossFiles, new OSSHelper.PutResult() {
                            @Override
                            public void success(ArrayList<OSSFiles> resultFiles) {
                                ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        V8Array files = new V8Array(ZKToolApi.runtime);
                                        for (OSSFiles resultFile : resultFiles) {
                                            files.push(resultFile.getResultFile());
                                        }
                                        resolve.call(null,files,null);
                                        files.release();
                                    }
                                });
                            }
                            @Override
                            public void failure() {
                                reject.call(null);
                            }
                        });
                    }
                });
            }
        },"putFiles");
        ZKToolApi.runtime.add("Aliput",Aliput);
    }
}
