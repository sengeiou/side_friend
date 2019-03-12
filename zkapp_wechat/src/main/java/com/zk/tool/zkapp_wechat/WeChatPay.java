package com.zk.tool.zkapp_wechat;

import android.content.Context;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

@ZKAppV8
public class WeChatPay implements ZKV8Fn {
    private V8Object weChatPay = new V8Object(ZKToolApi.runtime);
    private Context context = ZKToolApi.getInstance().getContext();
    private IWXAPI msgApi;

    public WeChatPay() {
    }

    @Override
    public void addV8Fn() {
        weChatPay.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                msgApi = WXAPIFactory.createWXAPI(ZKToolApi.getInstance().getTopActivity(), "wx00757650701cb696");
                msgApi.registerApp("wx00757650701cb696");
                V8Object object = parameters.getObject(0);
                PayReq request = new PayReq();
                request.appId = object.getString("appid");
                request.partnerId = object.getString("partnerid");
                request.prepayId= object.getString("prepayid");
                request.packageValue = object.getString("package");
                request.nonceStr= object.getString("noncestr");
                request.timeStamp= object.getString("timestamp");
                request.sign= object.getString("sign");
                msgApi.sendReq(request);

                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        resolve.call(null);
                    }
                });
            }
        },"pay");
        weChatPay.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {

                return null;
            }
        },"login");
        ZKToolApi.runtime.add("weChatPay",weChatPay);
    }

}
