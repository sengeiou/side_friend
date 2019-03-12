package com.zankong.tool.zkapp.v8fn.fetch_pck;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Fetch {
    private static OkHttpClient client = new OkHttpClient();
    public static Call get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        return client.newCall(request);
    }



    public static Call post(String url, String body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.addHeader("Accept", "application/json");
        builder.addHeader("Content-Type", "text/plain");
        builder.addHeader("Accept-Charset", "UTF-8");
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody;
        requestBody = RequestBody.create(mediaType, body);
        Request request = builder.post(requestBody).build();
        return client.newCall(request);
    }

    public static Call put(String url, JSONObject data) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        Iterator<String> keys = data.keys();
        try {
            while (keys.hasNext()) {
                String key = keys.next();
                File file = new File(data.getString(key));
                builder.addFormDataPart(key, file.getAbsolutePath(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody mRequestBody = builder.setType(MultipartBody.FORM).build();
        Request mRequest = new Request.Builder()
                .url(url)
                .post(mRequestBody)
                .build();
        return client.newCall(mRequest);
    }
}
