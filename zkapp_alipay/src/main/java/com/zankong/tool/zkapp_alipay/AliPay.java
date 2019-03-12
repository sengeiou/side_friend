package com.zankong.tool.zkapp_alipay;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YF on 2018/7/3.
 */

@ZKAppV8
public class AliPay implements ZKV8Fn {
    private ZKActivity mActivity;
    private final static int SDK_PAY_FLAG = 1;
    private final static int SDK_AUTH_FLAG = 2;
    private V8 runtime;
    private V8Object AliPay;
    public static final String ALIPAY = "aliPay";

    public AliPay() {
        EventBus.getDefault().register(this);
        runtime = ZKToolApi.runtime;
        AliPay = new V8Object(runtime);
    }



    @Override
    public void addV8Fn() {
        Util.log("apiPay addV8Fn");
        AliPay.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object doc = parameters.getObject(0);
                for (ZKActivity activity : ZKToolApi.getInstance().getActivities()) {
                    if (activity.page.equals(doc.getString("page"))) {
                        mActivity = activity;
                        break;
                    }
                }
                final String orderInfo = parameters.getString(1);

                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(final V8Function resolve, V8Function reject) {

                        /**
                         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
                         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
                         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
                         *
                         * orderInfo的获取必须来自服务端；
                         */

                        Thread payThread = new Thread(()->{
                            PayTask alipay = new PayTask(mActivity);
                            Map<String, String> result = alipay.payV2(orderInfo,true);
                            Map<String,Object> res = new HashMap<String, Object>();
                            res.put("result",result);
                            res.put("aliFn",resolve);
                            Log.d("aliyunPay",result.toString());
                            Message message = new Message();
                            message.what =  SDK_PAY_FLAG;
                            message.obj = res;
                            EventBus.getDefault().post(new MessageEvent(message,ALIPAY));
                        });
                        payThread.start();
                    }
                });
            }
        },"pay");
        AliPay.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String info = parameters.getString(0);
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Util.log("开始登陆");
                                    AuthTask authTask = new AuthTask(ZKToolApi.getInstance().getTopActivity());
                                    Map<String, String> result = authTask.authV2(info, true);
                                    Map<String,Object> res = new HashMap<String, Object>();
                                    res.put("result",result);
                                    res.put("aliFn",resolve);
                                    Message message = new Message();
                                    message.what =  SDK_AUTH_FLAG;
                                    message.obj = res;
                                    EventBus.getDefault().post(new MessageEvent(message,ALIPAY));
                                    Util.log("登陆结束");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
        },"login");
        runtime.add("AliPay",AliPay);
    }

    private class MessageEvent {
        private String message;
        private String tag;
        private Object obj;
        public  MessageEvent(String message,String tag){
            this.message=message;
            this.tag = tag;
        }
        public MessageEvent(Object obj,String tag){
            this.obj = obj;
            this.tag = tag;
        }

        public Object getObj() {
            return obj;
        }

        public String getMessage() {
            return message;
        }
        public String getTag(){
            return tag;
        }

        public void setMessage(String message,String tag) {
            this.message = message;
            this.tag = tag;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void aliPay(MessageEvent event){
        if (!ALIPAY.equals(event.getTag()))return;
        Message msg = (Message) event.getObj();
        switch (msg.what) {
            case SDK_PAY_FLAG: {
                HashMap<String,Object> res = (HashMap<String, Object>) msg.obj;
                V8Function aliFn = (V8Function) res.get("aliFn");
                PayResult payResult = new PayResult((Map<String, String>) res.get("result"));
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Dom.showT(mActivity,"支付成功");
                    if (aliFn != null){
                        aliFn.call(null);
                        aliFn.release();
                    }
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Util.log("取消支付");
                }
                break;
            }
            case SDK_AUTH_FLAG: {
                HashMap<String,Object> res = (HashMap<String, Object>) msg.obj;
                V8Function aliFn = (V8Function) res.get("aliFn");
                PayResult payResult = new PayResult((Map<String, String>) res.get("result"));
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();

                Util.log("asdfasfd",resultInfo);
                if (TextUtils.equals(resultStatus, "9000")) {
                    if (aliFn != null){
                        V8Object aliMessage = new V8Object(ZKToolApi.runtime);
                        String[] split = resultInfo.split("&");
                        for (String s : split) {
                            aliMessage.add(s.split("=")[0],s.split("=")[1]);
                        }
                        aliFn.call(null,aliMessage);
                        aliMessage.release();
                        aliFn.release();
                    }
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Util.log("取消授权");
                }
                break;
            }
            default:
                break;
        }
        mActivity = null;
    }

}
