package com.example.zkapp_identity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.util.Util;

import org.dom4j.Element;

public class IdentityActivity extends ZKActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Element elementByPath = Util.getElementByPath("config.xml");
        for (Element element : elementByPath.elements()) {
            switch (element.getName()) {
                case "identity":
                    page = element.getTextTrim();
                    break;
            }
        }
        arguments = new V8Object(ZKToolApi.runtime);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            //完整的url信息
            String url = uri.toString();
            //scheme部分
            String schemes = uri.getScheme();
            //host部分
            String host = uri.getHost();
            //port部分
            int port = uri.getPort();
            //访问路径
            String path = uri.getPath();
            //编码路径
            String path1 = uri.getEncodedPath();
            //query部分
            String queryString = uri.getQuery();
            //获取参数值
            String systemInfo = uri.getQueryParameter("system");
            String id = uri.getQueryParameter("id");
            Log.e("ttttttttt","url:"+url);
            Log.e("ttttttttt","schemes:"+schemes);
            Log.e("ttttttttt","host:"+host);
            Log.e("ttttttttt","port:"+port);
            Log.e("ttttttttt","path:"+path);
            Log.e("ttttttttt","path1:"+path1);
            Log.e("ttttttttt","queryString:"+queryString);
            Log.e("ttttttttt","systemInfo:"+systemInfo);
            Log.e("ttttttttt","id:"+id);
            arguments.add("queryString",queryString);
            String[] list = queryString.split("&");
            for (String str : list) {
                arguments.add(str.split("=")[0],str.split("=")[1]);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public V8Object getArguments() {
        return arguments;
    }
}
