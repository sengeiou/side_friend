package com.zankong.tool.zkapp.views.hr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.GsonUtils;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.fetch_pck.Fetch;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ServerAgent;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by YF on 2018/7/24.
 */

public class Hr_1 extends ZKViewAgent {

    private TextView mLinearLayout;

    private MyHandler mHandler;
    public Hr_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_line_1);
        mLinearLayout = ((TextView) findViewById(R.id.line));
        mHandler = new MyHandler(((ZKActivity) getContext()));
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "color":
                    mLinearLayout.setBackgroundColor(Color.parseColor(value));
                    break;
            }
        }
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerAgent.invoke("bill-list").then(res -> {
                    Util.log("调用成功", res + "");
                    return null;
                });
                
                Map<String, String> map = new HashMap<>();
                map.put("image", "/storage/emulated/0/0001_1_1_0_00_11.png");
                Fetch.put("http://api.zankong.com.cn:2223/uploadImages", new JSONObject(map)).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("aaaaaaaaaaa2",e.getMessage()+"");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        String string = body.string();
                        Gson gson = new Gson();//创建Gson对象
                        JsonParser jsonParser = new JsonParser();
                        JsonArray jsonElements = jsonParser.parse(string).getAsJsonArray();//获取JsonArray对象
                        ArrayList<Bean> beans = new ArrayList<>();
                        for (JsonElement bean : jsonElements) {
                            Bean bean1 = gson.fromJson(bean, Bean.class);//解析
                            beans.add(bean1);
                        }
                        Log.e("aaaaaaaaaaa000",beans.get(0).url+"==="+beans.get(0).getUrl());
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("url",beans.get(0).getUrl()+"");
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                });
            }
        });
    }

    @Override
    public Object getResult() {
        return null;
    }
    
    
    
    private class Bean{
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }




    static class MyHandler extends Handler {
        WeakReference<ZKActivity> mWeakReference;
        public MyHandler(ZKActivity activity) {
            mWeakReference = new WeakReference<ZKActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if (activity != null) {
                if (msg.what == 0) {
                    Bundle data = msg.getData();
                    String url = data.getString("url");
                    Toast.makeText(activity,url+"",Toast.LENGTH_SHORT).show();
                    Log.e("ddddddddddd3",url+"===");
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
