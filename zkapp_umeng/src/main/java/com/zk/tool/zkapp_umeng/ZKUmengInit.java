package com.zk.tool.zkapp_umeng;


import com.umeng.commonsdk.UMConfigure;

public class ZKUmengInit {
    public static void init(){
        UMConfigure.setLogEnabled(true);
        new ZKUmengShare().addV8Fn();
    }
}
