package com.example.zkapp_identity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.net.URLEncoder;
import java.util.List;

@ZKAppV8
public class IdentityInto implements ZKV8Fn{
    private V8Object identity;
    private ZKActivity zkActivity;

    public IdentityInto(){
        identity = new V8Object(ZKToolApi.runtime);
    }

    @Override
    public void addV8Fn() {
        identity.registerJavaMethod((receiver, parameters) -> {
            zkActivity = ZKToolApi.getInstance().getActivities().get(ZKToolApi.getInstance().getActivities().size()-1);
            doVerify(parameters.getString(0));
        },"startIdentity");
        identity.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                zkActivity = ZKToolApi.getInstance().getActivities().get(ZKToolApi.getInstance().getActivities().size()-1);
                zkActivity.startActivity(new Intent(zkActivity,IdentityActivity.class));
                return null;
            }
        },"open");
        ZKToolApi.runtime.add("Identity",identity);
    }



    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            zkActivity.startActivity(action);
        } else {
            //处理没有安装支付宝的情况
            new AlertDialog.Builder(zkActivity)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            zkActivity.startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = zkActivity.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }
}
