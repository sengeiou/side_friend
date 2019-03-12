//package com.zk.tool.zkapp_call.old;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.net.wifi.WifiManager;
//import android.util.Log;
//
//import com.eclipsesource.v8.JavaCallback;
//import com.eclipsesource.v8.V8;
//import com.eclipsesource.v8.V8Array;
//import com.eclipsesource.v8.V8Function;
//import com.eclipsesource.v8.V8Object;
//import com.tencent.ilivesdk.ILiveCallBack;
//import com.tencent.ilivesdk.ILiveSDK;
//import com.tencent.ilivesdk.core.ILiveLoginManager;
//import com.zankong.tool.zkapp.ZKToolApi;
//import com.zankong.tool.zkapp.util.Util;
//import com.zankong.tool.zkapp.util.V8Utils;
//import com.zankong.tool.zkapp.util.ZKAppV8;
//import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;
//
//import org.dom4j.Attribute;
//import org.dom4j.Element;
//import org.json.JSONException;
//
//import java.util.ArrayList;
//
///**
// * Created by YF on 2018/2/24.
// */
//
//@ZKAppV8
//public class CallManager implements ZKV8Fn{
//    private Context mContext = ZKToolApi.getInstance().getContext();
//    private V8 mV8 = ZKToolApi.runtime;
//    private int mCurIncomingId;//来电id
//    private ILVCallConfig mILVCallConfig;
//    private V8Object callLive;
//    private ILVIncomingListener mILVIncomingListener;
//    private String id,userSig;
//    private int appId,accountType;
//    private boolean isLogin = false,isInit = false;
//    private NetworkConnectChangedReceiver mReceiver;
//    public static final String CALL_RESOLVE = "callResolve";
//    public static final String CALL_REJECT = "callReject";
//    public static Boolean isReceiverRegister = false;
//
//
//    private void acceptCall(String hostId, int callType,String userInfo){
//        Intent intent = new Intent();
//        Activity activity = ZKToolApi.getInstance().getTopActivity();
//        intent.setClass(activity, CallActivity.class);
//        intent.putExtra("HostId", hostId);
//        intent.putExtra("CallId", mCurIncomingId);
//        intent.putExtra("CallType", callType);
//        intent.putExtra("userInfo",userInfo);
//        activity.startActivity(intent);
//    }
//    private void makeCall(int callType, ArrayList<String> nums,String userInfo){
//        Intent intent = new Intent();
//        Activity activity = ZKToolApi.getInstance().getTopActivity();
//        intent.setClass(activity, CallActivity.class);
//        intent.putExtra("HostId", ILiveLoginManager.getInstance().getMyUserId());
//        intent.putExtra("CallId", 0);
//        intent.putExtra("CallType", callType);
//        intent.putExtra("userInfo",userInfo);
//        intent.putStringArrayListExtra("CallNumbers", nums);
//        Log.d("call",intent.toString());
//        activity.startActivity(intent);
//    }
//
//
//
//
//
//
//
//
//
//    private void registerNetworkCallBack(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        mReceiver = new NetworkConnectChangedReceiver();
//        mReceiver.setOnNetworkChangeListener(new NetworkConnectChangedReceiver.OnNetworkChangeListener() {
//            @Override
//            public void wifi() {
//                if (isLogin) {
//                    mContext.unregisterReceiver(mReceiver);
//                    return;
//                }
//                ILiveLoginManager.getInstance().iLiveLogin(id, userSig, new ILiveCallBack() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Log.d("callLive","login success wifi");
//                        isLogin = true;
//                    }
//
//                    @Override
//                    public void onError(String s, int i, String s1) {
//                        Log.d("callLive","login err wifi");
//                    }
//                });
//            }
//
//            @Override
//            public void mobile() {
//                if (isLogin) {
//                    mContext.unregisterReceiver(mReceiver);
//                    return;
//                }
//                ILiveLoginManager.getInstance().iLiveLogin(id, userSig, new ILiveCallBack() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Log.d("callLive","login success mobile");
//                        isLogin = true;
//                        mContext.unregisterReceiver(mReceiver);
//                    }
//
//                    @Override
//                    public void onError(String s, int i, String s1) {
//                        Log.d("callLive","login err  mobile");
//                    }
//                });
//            }
//
//            @Override
//            public void err() {
//
//            }
//        });
//        if(!isReceiverRegister) {
//            mContext.registerReceiver(mReceiver, filter);
//            isReceiverRegister = true;
//        }
//    }
//
//    @Override
//    public void addV8Fn() {
//        Element config = Util.getElementByPath("config.xml");
//        for (Attribute callLive : config.element("callLive").attributes()) {
//            switch (callLive.getName().toLowerCase()) {
//                case "appId":
//                    appId = Integer.parseInt(callLive.getValue());
//                    break;
//                case "accountType":
//                    accountType = Integer.parseInt(callLive.getValue());
//                    break;
//            }
//        }
//        ILiveSDK.getInstance().initSdk(mContext, appId, accountType);
//        mILVCallConfig = new ILVCallConfig();
//        ILVCallManager.getInstance().init(mILVCallConfig);
//        isInit = true;
//
//        callLive = new V8Object(mV8);
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
////                if (parameters.get(0) instanceof V8Object){
////                    appId = parameters.getObject(0).getInteger("appId");
////                    accountType = parameters.getObject(0).getInteger("accountType");
////                }
////                if (parameters.length()>1){
////                    V8Object config = parameters.getObject(1);
////                    for (String s : config.getKeys()) {
////                        switch (s){
////                            case "autoBusy":
////                                mILVCallConfig.setAutoBusy(config.getBoolean(s));
////                                break;
////                            case "notificationListener":
////                                mILVCallConfig.setNotificationListener(new ILVCallNotificationListener() {
////                                    @Override
////                                    public void onRecvNotification(int i, ILVCallNotification ilvCallNotification) {
////                                        Log.d("onCallEnd","notificationListener"+ilvCallNotification.toString());
////                                        V8Object ILVCallNotificationJS = new V8Object(mV8);
////                                        ((V8Function) config.getObject(s)).call(null,ILVCallNotificationJS);
////                                    }
////                                });
////                                break;
////                        }
////                    }
////                }else {
////                    mILVCallConfig.setAutoBusy(true);
////                    mILVCallConfig.setNotificationListener(new ILVCallNotificationListener() {
////                        @Override
////                        public void onRecvNotification(int i, ILVCallNotification ilvCallNotification) {
////                            Log.d("setNotificationListener",i+ilvCallNotification.toString());
////                        }
////                    });
////                }
////                ILVCallManager.getInstance().init(mILVCallConfig);
////                isInit = true;
////                return V8Utils.createFrom(callLive);
//                return null;
//            }
//        },"init");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                if (!isInit){
//                    Log.d("callManager","callLive 没有初始化");
//                    return false;
//                }
//                if (parameters.length()>0){
//                    V8Function listener = (V8Function) parameters.getObject(0);
//                    mILVIncomingListener = new ILVIncomingListener() {
//                        @Override
//                        public void onNewIncomingCall(final int callId, final int callType, final ILVIncomingNotification notification) {
//                            V8Object obj = new V8Object(mV8);
//                            obj.add("callId",callId);
//                            obj.add("callType",callType);
//                            V8Object notificationFn = new V8Object(mV8);
//                            notificationFn.registerJavaMethod(new JavaCallback() {
//                                @Override
//                                public Object invoke(V8Object receiver, V8Array parameters) {
//                                    return notification.getSponsorId();
//                                }
//                            },"getSponsorId");
//                            notificationFn.registerJavaMethod(new JavaCallback() {
//                                @Override
//                                public Object invoke(V8Object receiver, V8Array parameters) {
//                                    return notification.getSender();
//                                }
//                            },"getSender");
//                            notificationFn.registerJavaMethod(new JavaCallback() {
//                                @Override
//                                public Object invoke(V8Object receiver, V8Array parameters) {
//                                    return notification.getMembers();
//                                }
//                            },"getMembers");
//                            notificationFn.registerJavaMethod(new JavaCallback() {
//                                @Override
//                                public Object invoke(V8Object receiver, V8Array parameters) {
//                                    return notification.getMembersString();
//                                }
//                            },"getMembersString");
//                            notificationFn.registerJavaMethod(new JavaCallback() {
//                                @Override
//                                public Object invoke(V8Object receiver, V8Array parameters) {
//                                    return notification.setSponsorId(parameters.getString(0));
//                                }
//                            },"setSponsorId");
//                            obj.add("notification",notificationFn);
//                            listener.call(null,obj);
//                        }
//                    };
//                }else {
//                    mILVIncomingListener = new ILVIncomingListener() {
//                        @Override
//                        public void onNewIncomingCall(final int callId, final int callType, final ILVIncomingNotification notification) {
//                            mCurIncomingId = callId;
//                            acceptCall(notification.getSponsorId(),callType,notification.getUserInfo());
//                        }
//                    };
//                }
//                ILVCallManager.getInstance().addIncomingListener(mILVIncomingListener);
//                return null;
//            }
//        },"setComingCallBack");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                Log.d("callLive","login");
//                if (parameters.length()>0){
//                    id = parameters.getString(0);
//                    userSig = parameters.getString(1);
//                }
//                return V8Utils.Promise(new V8Utils.promiseHandler() {
//                    @Override
//                    public void procedure(V8Function resolve, V8Function reject) {
//                        Util.log("登录callLive,,,,,");
//                        if (isLogin){
//                            if (isReceiverRegister && mReceiver != null){
//                                isReceiverRegister = false;
//                                mContext.unregisterReceiver(mReceiver);
//                            }
//                            resolve.call(null,ILiveLoginManager.getInstance().getMyUserId());
//                            return;
//                        }
//                        ILiveLoginManager.getInstance().iLiveLogin(id, userSig, new ILiveCallBack() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                Log.d("callLive","login success login");
//                                isLogin = true;
//                                if (isReceiverRegister && mReceiver != null){
//                                    isReceiverRegister = false;
//                                    mContext.unregisterReceiver(mReceiver);
//                                }
//                                resolve.call(null,ILiveLoginManager.getInstance().getMyUserId());
//                            }
//
//                            @Override
//                            public void onError(String s, int i, String s1) {
//                                Log.d("callLive","login err login"+"s:"+s+"i:"+i+"s1:"+s1);
//                                registerNetworkCallBack();
//                                reject.call(null);
//                            }
//                        });
//                    }
//                });
//            }
//        },"login");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                return V8Utils.Promise(new V8Utils.promiseHandler() {
//                    @Override
//                    public void procedure(V8Function resolve, V8Function reject) {
//                        ILiveLoginManager.getInstance().iLiveLogout(new ILiveCallBack() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                isLogin = false;
////                                Util.log("友盟登录",o+"");
////                                V8Object message = new V8Object(ZKToolApi.runtime);
////                                message.add("uid",o+"");
//                                resolve.call(null);
//                            }
//
//                            @Override
//                            public void onError(String s, int i, String s1) {
//                                isLogin = false;
//                                reject.call(null);
//                            }
//                        });
//                    }
//                });
//            }
//        },"logout");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                return null;
//            }
//        },"accept");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                Util.log("callLive call1");
//                int callType = ILVCallConstants.CALL_TYPE_VIDEO;
//                String userInfo = "";
//                ArrayList<String> nums = new ArrayList<>();
//                V8Object object = parameters.getObject(0);
//                Util.log("callLive call2");
//                for (String key : object.getKeys()) {
//                    switch (key){
//                        case "nums":
//                            V8Array arr = object.getArray("nums");
//                            for (int i = 0 ; i < arr.length() ; i++){
//                                nums.add(arr.getString(i));
//                            }
//                            arr.release();
//                            break;
//                        case "type":
//                            switch (object.getString("type")){
//                                case "audio":
//                                    callType = ILVCallConstants.CALL_TYPE_AUDIO;
//                                    break;
//                                case "video":
//                                    callType = ILVCallConstants.CALL_TYPE_VIDEO;
//                                    break;
//                            }
//                            break;
//                        case "info":
//                            V8Object infoObj = object.getObject("info");
//                            try {
//                                userInfo = V8Utils.V82JSON(infoObj).toString();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            infoObj.release();
//                            break;
//                    }
//                }
//                Util.log("callLive call3");
//                object.release();
//                Util.log("callLive call4");
//                makeCall(callType,nums,userInfo);
//                Util.log("callLive call5");
//                return V8Utils.Promise(new V8Utils.promiseHandler() {
//                    @Override
//                    public void procedure(V8Function resolve, V8Function reject) {
//                        Util.log("callLive call6");
//                        ZKToolApi.getInstance().getCallBacks().put(CALL_RESOLVE,resolve);
//                        ZKToolApi.getInstance().getCallBacks().put(CALL_REJECT,reject);
//                    }
//                });
//            }
//        },"call");
//        callLive.registerJavaMethod(new JavaCallback() {
//            @Override
//            public Object invoke(V8Object receiver, V8Array parameters) {
//                return ILiveLoginManager.getInstance().isLogin();
//            }
//        },"isLogin");
//        mV8.add("callLive",callLive);
//    }
//}
