package com.example.zkapp_identity.http;

import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Fsnzzz
 * @Created on 2018/10/23 0023 17:26
 */
public class OkHttpUtils {
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    private OkHttpUtils() {
        okhttp3.OkHttpClient.Builder ClientBuilder=new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        mOkHttpClient=ClientBuilder.build();
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }



    /**
     * 设置请求头
     * @param headersParams
     * @return
     */
    private Headers setHeaders(Map<String, String> headersParams){
        Headers headers=null;
        okhttp3.Headers.Builder headersbuilder=new okhttp3.Headers.Builder();

        if(headersParams != null)
        {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                Log.d("get http", "get_headers==="+key+"===="+headersParams.get(key));
            }
        }
        headers=headersbuilder.build();

        return headers;
    }

    /**
     * post请求参数
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
                Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }
    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public  void postDataAsynToNet(String url, String path,Map<String,String> bodyParams, Map<String,String> headerParams,final MyNetCall myNetCall) {
        //1构造RequestBody、RequestHeader
        RequestBody body=setRequestBody(bodyParams);
        Headers headers = setHeaders(headerParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url+path)
                .headers(headers)
                .build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call,response);

            }
        });
    }


    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall{
        void success(Call call, Response response) throws IOException;
        void failed(Call call, IOException e);
    }
}
