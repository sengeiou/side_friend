package com.example.zkapp_identity.identity;

import android.content.Intent;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_identity.identity_face.FaceLivenessExpActivity;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;


public class ZKIdentityFn implements ZKV8Fn {
    private V8Object Identity = new V8Object(ZKToolApi.runtime);

    @Override
    public void addV8Fn() {
        Identity.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                ZKActivity topActivity = ZKToolApi.getInstance().getTopActivity();
                Intent intent = new Intent(topActivity, FaceLivenessExpActivity.class);
                V8Object object = parameters.getObject(0);
                intent.putExtra("name",object.getString("name"));
                intent.putExtra("id",object.getString("id"));
                object.release();
                topActivity.startActivity(intent);
                return null;
            }
        },"compare");
        ZKToolApi.runtime.add("Identity",Identity);
    }
}
