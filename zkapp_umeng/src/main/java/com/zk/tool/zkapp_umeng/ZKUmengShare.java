package com.zk.tool.zkapp_umeng;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

public class ZKUmengShare implements ZKV8Fn {
    private V8Object UmengShare = new V8Object(ZKToolApi.runtime);
    private boolean isGetConfig = false;
    private UMShareAPI mShareAPI;
    private Context context = ZKToolApi.getInstance().getContext();
    private boolean isConfig = false;
    public ZKUmengShare(){
        mShareAPI = UMShareAPI.get(context);
        Element elementByPath = Util.getElementByPath("config.xml");
        for (Element element : elementByPath.elements()) {
            switch (element.getName().toLowerCase()){
                case "umeng-share":
                    isConfig = true;

                    for (Attribute attribute : element.attributes()) {
                        String value = attribute.getValue();
                        switch (attribute.getName().toLowerCase()){
                            case "appkey":
                                UMConfigure.init(context,value,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
                                break;
                        }
                    }
                    List<Element> items = element.elements();
                    for (Element item : items) {
                        switch (item.getName().toLowerCase()){
                            case "wechat":
                                PlatformConfig.setWeixin(item.attributeValue("appid"),item.attributeValue("secret"));
                                break;
                            case "qq":
                                PlatformConfig.setQQZone(item.attributeValue("appid"),item.attributeValue("appkey"));
                                break;
                            case "alipay":
                                PlatformConfig.setAlipay(item.attributeValue("appid"));
                                break;
                        }
                    }
                    UMShareConfig config = new UMShareConfig();
                    config.isNeedAuthOnGetUserInfo(true);
                    UMShareAPI.get(context).setShareConfig(config);
                    break;
            }
        }
    }
    @Override
    public void addV8Fn() {
        if (!isConfig){
            return;
        }
        UmengShare.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        String type = parameters.getString(0);
                        SHARE_MEDIA shareType = null;
                        ZKActivity activity = ZKToolApi.getInstance().getActivities().get(ZKToolApi.getInstance().getActivities().size() -1);
                        switch (type){
                            case "weChat":
                                if (!mShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                                    Util.showT("没有安装微信");
                                    reject.call(null);
                                    return;
                                }
                                shareType = SHARE_MEDIA.WEIXIN;
                                break;
                            case "qq":
                                if (!mShareAPI.isInstall(activity, SHARE_MEDIA.QQ)) {
                                    Util.showT("没有安装qq");
                                    reject.call(null);
                                    return;
                                }
                                shareType = SHARE_MEDIA.QQ;
                                activity.setResultListener(new ZKNaiveActivity.OnResultListener() {
                                    @Override
                                    public void result(int requestCode, int resultCode, @Nullable Intent data) {
                                        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
                                    }
                                });
                                break;
                            case "alipay":
                                shareType = SHARE_MEDIA.ALIPAY;
                                break;
                        }
                        try{
                            mShareAPI.getPlatformInfo(activity, shareType, new UMAuthListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {
                                    Util.log("开始第三方登录");
                                }
                                @Override
                                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                                    Util.log("第三方登录成功");
                                    V8Object user = new V8Object(ZKToolApi.runtime);
                                    user.add("name", map.get("name"));
                                    user.add("img", map.get("iconurl"));
                                    user.add("uid", map.get("uid"));
                                    resolve.call(null,user);
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                                    reject.call(null);
                                    Util.showT("登录失败");
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media, int i) {
                                    reject.call(null);
                                    Util.log("登录取消");
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        },"login");
        UmengShare.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String type = parameters.getString(0);
                V8Object object = parameters.getObject(1);
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        UMWeb umWeb = new UMWeb(object.getString("url"));
                        for (String key : object.getKeys()) {
                            switch (key) {
                                case "title":
                                    umWeb.setTitle(object.getString(key));
                                    break;
                                case "thumb":
                                    UMImage image = new UMImage(ZKToolApi.getInstance().getTopActivity(),R.drawable.logo );
                                    umWeb.setThumb(image);//网络图片);
                                    break;
                                case "description":
                                    umWeb.setDescription(object.getString(key));
                                    break;
                            }
                        }
                        SHARE_MEDIA shareType = null;
                        switch (type) {
                            case "weChat":
                                shareType = SHARE_MEDIA.WEIXIN;
                                break;
                            case "qq":
                                shareType = SHARE_MEDIA.QQ;
                                break;
                        }
                        new ShareAction(ZKToolApi.getInstance().getTopActivity())
                                .setPlatform(shareType)
                        .withMedia(umWeb).share();
                    }
                });
            }
        },"share");
        ZKToolApi.runtime.add("UmengShare",UmengShare);
    }
}
