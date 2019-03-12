package com.zankong.tool.zkapp.v8fn.socket_pck;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.MessageEvent;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;

import org.dom4j.Element;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by YF on 2018/6/30.
 */

public class ZKServiceSocket extends Service {
    public static Socket mSocket;
    private String tokenify;
    public static boolean isStart = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Util.log("socket","onCreate");
        Element elementByPath = Util.getElementByPath("config.xml");
        for (Element element : elementByPath.elements()) {
            switch (element.getName()) {
                case "service":
                    String url = element.element("http").getTextTrim();
                    ZKLocalStorage.mEditor.putString("socketUrl",url);
                    ZKLocalStorage.mEditor.commit();
                    break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isStart)return super.onStartCommand(intent, flags, startId);
        isStart = true;
        Util.log("socket","onStartCommand");
        String url = ZKLocalStorage.mSharedPreferences.getString("socketUrl","");
        tokenify = ZKLocalStorage.mSharedPreferences.getString("socketId","");
        if("".equals(url)){
            Util.log("socket链接地址不存在");
            return super.onStartCommand(intent, flags, startId);
        }
        if ("".equals(tokenify)){
            tokenify = Math.random()+"";
        }
        createSocket(url);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createSocket(String url){
        try {
            mSocket = IO.socket(url);
            mSocket.connect();
            mSocket.emit("tokenify", tokenify);
            mSocket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject jsonObject = null;
                    if(args[0] instanceof String){
                        try {
                            jsonObject = new JSONObject((String) args[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (args[0] instanceof JSONObject) {
                        jsonObject = (JSONObject) args[0];
                    }
                    if(jsonObject != null){
                        Log.d("socket",jsonObject.toString()+"");
                        String event = jsonObject.optString("event");
                        JSONObject data = jsonObject.optJSONObject("data");
                        ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                MessageEvent messageEvent = new MessageEvent(event,data.toString());
                                EventBus.getDefault().post(messageEvent);
                                setSocketOnFn(event,data);
                            }
                        });
                    }
                }
            });
            mSocket.on("notification", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setSocketOnFn(String event,JSONObject data){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStart = false;
    }
}
