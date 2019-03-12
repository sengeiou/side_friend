/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.zkapp_identity.identity_face.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.example.zkapp_identity.identity_face.APIService;
import com.example.zkapp_identity.identity_face.OnResultListener;
import com.example.zkapp_identity.identity_face.exception.FaceException;
import com.example.zkapp_identity.identity_face.model.AccessToken;
import com.example.zkapp_identity.identity_face.model.RequestParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用okhttp请求tokeh和调用服务
 */
public class HttpUtil {

    private OkHttpClient client;
    private Handler handler;
    private static volatile HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public void init() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public <T> void post(String path, RequestParams params, final Parser<T> parser, final OnResultListener<T>
            listener) {
        post(path, "images", params, parser, listener);
    }

    public <T> void post(String path, String key, RequestParams params,
                         final Parser<T> parser, final OnResultListener<T> listener) {
        Base64RequestBody body = new Base64RequestBody();

        body.setKey(key);
        body.setFileParams(params.getFileParams());
        body.setStringParams(params.getStringParams());
        body.setJsonParams(params.getJsonParams());
        final Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        // liujinhui 经常client为空指针 ？
        if (client == null) {
            HttpUtil.getInstance().release();
            HttpUtil.getInstance().init();
            if (client == null) {
                throwError(listener, -999, "okhttp inner error");
                return;
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                throwError(listener, FaceException.ErrorCode.NETWORK_REQUEST_ERROR, "network request error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                final T result;
                try {
                    result = parser.parse(responseString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResult(result);
                        }
                    });
                } catch (FaceException e) {
                    e.printStackTrace();
                }
             
            }
        });
    }

    public void getAccessToken(final OnResultListener<AccessToken> listener, String url, String param) {

        final Parser<AccessToken> accessTokenParser = new AccessTokenParser();
        RequestBody body = RequestBody.create(MediaType.parse("text/html"), param);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                throwError(listener, FaceException.ErrorCode.NETWORK_REQUEST_ERROR, "network request error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || response.body() == null || TextUtils.isEmpty(response.toString())) {
                    throwError(listener, FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                            "token is parse error, please rerequest token");
                }
                try {
                    final AccessToken accessToken = accessTokenParser.parse(response.body().string());
                    if (accessToken != null) {
                        APIService.getInstance().setAccessToken(accessToken.getAccessToken());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onResult(accessToken);
                            }
                        });
                    } else {
                        throwError(listener,FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                                "token is parse error, please rerequest token");
                    }
                } catch (FaceException error) {
                    error.printStackTrace();
                    throwError(listener, FaceException.ErrorCode.ACCESS_TOKEN_PARSE_ERROR,
                            "token is parse error, please rerequest token");
                }
            }
        });

    }

    /**
     * throw error
     *
     * @param errorCode
     * @param msg
     * @param listener
     */
    private void throwError(final OnResultListener listener, int errorCode, String msg) {
        final FaceException error = new FaceException(errorCode, msg);
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onError(error);
            }
        });
    }

    /**
     * 释放资源
     */
    public void release() {
        client = null;
        handler = null;
    }

    

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
    
}
