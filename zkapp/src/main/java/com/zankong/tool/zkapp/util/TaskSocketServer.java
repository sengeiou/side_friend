package com.zankong.tool.zkapp.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by YF on 2018/8/7.
 */

public class TaskSocketServer extends Service{
    Socket mSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mSocket = IO.socket("http://zankong.com.cn:2222");
            mSocket.connect();
            mSocket.on("showPage", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (args.length > 0){
                        if (args[0] instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) args[0];
                            ZKActivity zkActivity = ZKToolApi.getInstance().getActivities().get(ZKToolApi.getInstance().getActivities().size());
                            Intent i = new Intent(zkActivity,ZKActivity.class);
                            try {
                                i.putExtra(ZKDocument.ARGUMENTS,jsonObject.getJSONObject("pck").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            i.putExtra("page",jsonObject.optString("url"));
                            zkActivity.startActivity(i);
                        }
                    }

                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
