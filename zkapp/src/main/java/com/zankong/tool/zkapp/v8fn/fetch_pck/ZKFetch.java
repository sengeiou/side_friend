package com.zankong.tool.zkapp.v8fn.fetch_pck;

import android.os.Environment;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKFetch implements ZKV8Fn {
    private OkHttpClient client;

    public ZKFetch() {
        client = new OkHttpClient();
    }

    private V8Object fetchThen(Response response) {
        V8Object body = new V8Object(ZKToolApi.runtime);
        body.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        try {
                            resolve.call(null, response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                            reject.call(null, "");
                        }
                    }
                });
            }
        }, "text");
        body.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.Promise(new V8Utils.promiseHandler() {
                    @Override
                    public void procedure(V8Function resolve, V8Function reject) {
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String string = response.body().string();
                                        ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                resolve.call(null, V8Utils.parse(string));
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            reject.call(null, "");
                        }
                    }
                });
            }
        }, "json");
        body.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object buffer = new V8Object(ZKToolApi.runtime);
                buffer.registerJavaMethod((receiver1, parameters1) -> {
                    String saveFilePath = Environment.getExternalStorageState() + parameters1.getString(0);
                    String saveFile = parameters1.getString(1);
                    File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), saveFilePath);
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    final File file = new File(filePath + saveFile);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Util.log("file:",file.getAbsolutePath());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InputStream is = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            try {
                                is = response.body().byteStream();
                                long total = response.body().contentLength();
                                fos = new FileOutputStream(file);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    // 下载中
                                    Util.log("下载中", file.getAbsolutePath());
                                }
                                fos.flush();
                                // 下载完成
                                Util.log("下载完成", file.getAbsolutePath());
                            } catch (Exception e) {
                                Util.log("下载出错");
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (is != null)
                                        is.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null)
                                        fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }).start();
                    return null;
                }, "createFile");
                return buffer;
            }
        }, "blob");
        return body;
    }

    @Override
    public void addV8Fn() {
        ZKToolApi.runtime.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise((resolve, reject) -> {
                String url = parameters.getString(0);
                V8Object param = null;
                if (parameters.length() >= 2 && parameters.get(1) instanceof V8Object) {
                    param = parameters.getObject(1);
                }
                fetch(url, param).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ZKToolApi.getInstance().getJsHandler().post(() -> {
                            Util.showT("网络链接失败,请检查网络");
                            reject.call(null, "");
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ZKToolApi.getInstance().getJsHandler().post(() -> {
                            try {
                                resolve.call(null, fetchThen(response));
                            } catch (Exception e) {
                                e.printStackTrace();
                                reject.call(null, "");
                            }
                        });
                    }
                });
            });
        }, "fetch");
    }

    private Call fetch(String url, V8Object param) {
        try{
            if (param == null || !param.contains("method")) return get(url);
            if (param.contains("method")) {
                String method = param.getString("method").toLowerCase();
                if (method.equals("get")) return get(url);
                if (method.equals("post")) {
                    if (param.get("body") instanceof V8Object) {
                        return put(url, param);
                    } else {
                        return post(url, param);
                    }
                }
                if (method.equals("put")) return put(url, param);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return get(url);
    }

    private Call get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        return client.newCall(request);

    }


    private Call post(String url, V8Object param) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody;
        if (param.contains("headers")) {
            for (String headerKay : param.getObject("headers").getKeys()) {
                builder.addHeader(headerKay, param.getObject("headers").getString(headerKay));
            }
        }
        if (param.contains("body")) {
            requestBody = RequestBody.create(mediaType, param.getString("body"));
        } else {
            requestBody = RequestBody.create(mediaType, "");
        }
        Request request = builder.post(requestBody).build();
        return client.newCall(request);
    }

    private Call put(String url, V8Object formData) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        try {
            V8Array datas = formData.getObject("body").getArray("value");
            for(int i = 0 ; i < datas.length() ; i++){
                for (String s : datas.getObject(i).getKeys()) {
                    File file = new File(datas.getObject(i).getString(s));
                    builder.addFormDataPart(s, file.getAbsolutePath(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                }
            }
            RequestBody mRequestBody = builder.setType(MultipartBody.FORM).build();
            Request mRequest = new Request.Builder()
                    .url(url)
                    .post(mRequestBody)
                    .build();
            return client.newCall(mRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
