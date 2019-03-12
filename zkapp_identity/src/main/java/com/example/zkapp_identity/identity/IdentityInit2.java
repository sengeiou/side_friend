package com.example.zkapp_identity.identity;

import android.content.Context;

import com.zankong.tool.zkapp.views.ZKViews;

public class IdentityInit2 {
    public static void initIdentity2(Context context){
        ZKViews.ViewMap.put("identity",IdentityView.class);
        try {
            ZKIdentityFn.class.newInstance().addV8Fn();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}