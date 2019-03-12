package com.zk.tool.zkapp_update;

import android.app.Application;

import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;

public class ZKUpdateInit {
    public static void init(Application application){
        XUpdate.get().debug(true).isWifiOnly(true).isGet(true).isAutoMode(true).param("","").setOnUpdateFailureListener(new OnUpdateFailureListener() {
            @Override
            public void onFailure(UpdateError error) {

            }
        }).init(application);
        try {
            ZKUpdate.class.newInstance().addV8Fn();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
