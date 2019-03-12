package com.zk.tool.zkapp_call;

import android.content.Context;
import android.content.Intent;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.data.ILiveMessage;
import com.tencent.ilivesdk.data.msg.ILiveCustomMessage;
import com.tencent.ilivesdk.listener.ILiveEventHandler;
import com.tencent.ilivesdk.listener.ILiveMessageListener;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;
import com.zk.tool.zkapp_call.demo.model.MessageObservable;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

@ZKAppV8
public class ZKCallManager implements ZKV8Fn,ILiveMessageListener {
    private V8Object Call ;
    private boolean isLogin = false;


    private Context getContext(){
        return ZKToolApi.getInstance().getContext();
    };
    public ZKCallManager() {
        Element elementByPath = Util.getElementByPath("config.xml");
        MessageObservable.getInstance().addObserver(this);

        Element call = elementByPath.element("call");
        for (Attribute attribute : call.attributes()) {
            switch (attribute.getName().toLowerCase()) {
                case "sdkappid":
                    initCall(Integer.parseInt(attribute.getValue()));
                    break;
            }
        }
        Call = new V8Object(ZKToolApi.runtime);
    }

    private void initCall(int sdkAppId){
// 初始化ILiveSDK
        ILiveSDK.getInstance().initSdk(getContext(), sdkAppId, 0);
        // 初始化房间模块
        ILiveRoomManager.getInstance().init(new ILiveRoomConfig()
                .setRoomMsgListener(MessageObservable.getInstance()));
    }

    @Override
    public void addV8Fn() {
        Call.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        if (!ILiveLoginManager.getInstance().isLogin()){
                            String useId = parameters.getString(0);
                            String userSig = parameters.getString(1);
                            ILiveSDK.getInstance().addEventHandler(new ILiveEventHandler(){
                                // 登录成功事件
                                @Override
                                public void onLoginSuccess(String userId) {
                                    isLogin = true;
                                    resolve.call(null);
                                    Util.log("实时语音登录成功");
                                }
                                // 登录失败事件
                                @Override
                                public void onLoginFailed(String userId, String module, int errCode, String errMsg) {
                                    reject.call(null);
                                    Util.log("实时语音登录失败");

                                }
                                // 帐号被踢
                                @Override
                                public void onForceOffline(String userId, String module, int errCode, String errMsg) {
                                    isLogin = false;
                                    Util.log("实时语音被踢了");

                                }
                            });
                            // 登录SDK
                            ILiveLoginManager.getInstance().iLiveLogin(useId, userSig, null);
                        }else{
                            Util.log("实时语音已经登录");
                            resolve.call(null);
                        }
                    }
                });
            }
        },"login");
        Call.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String stringify = V8Utils.stringify(parameters.getObject(0));
                Intent intent = new Intent(getContext(), ZKCallActivity.class);
                intent.putExtra("message", stringify);
                ZKToolApi.getInstance().getTopActivity().startActivity(intent);
                return null;
            }
        },"call");
        Call.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"accept");
        Call.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (ILiveLoginManager.getInstance().isLogin())
                    ILiveLoginManager.getInstance().iLiveLogout();
                return null;
            }
        },"logout");
        ZKToolApi.runtime.add("Call",Call);
    }

    @Override
    public void onNewMessage(ILiveMessage message) {
        Util.log("onNewMessage");
        switch (message.getMsgType()) {
            case ILiveMessage.ILIVE_MSG_TYPE_TEXT:
                break;
            case ILiveMessage.ILIVE_MSG_TYPE_CUSTOM:
                try {
                    JSONObject jsonObject = new JSONObject(new String(((ILiveCustomMessage) message).getData()));
                    Util.log("onNewMessage",jsonObject.toString());
                    String event = jsonObject.optString("event");
                    JSONObject data = jsonObject.optJSONObject("data");
                    if(event!=null){
                        switch (event){
                            case ZKCallActivity.CANCEL:
                                Util.showT("对方取消了音频聊天");
                                break;
                            case ZKCallActivity.INVITE:
                                Util.showT("对方邀请你音频聊天");
                                data.put("roomid",data.optInt("houseId"));
                                String stringify = data.toString();
                                Intent intent = new Intent(getContext(), ZKCallActivity.class);
                                intent.putExtra("message", stringify);
                                ZKToolApi.getInstance().getTopActivity().startActivity(intent);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Util.log("onNewMessage-> message type: " + message.getMsgType());
                break;
        }
    }

}
