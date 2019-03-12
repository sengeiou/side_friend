package com.zankong.tool.zkapp.v8fn.fetch_pck;

import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Promise;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.v8fn.socket_pck.ZKSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ServerAgent {
    public static String baseApi = ZKToolApi.baseApi;
    public static Promise invoke(String api, JSONObject data){
        return invoke(api,data.toString());
    }
    public static Promise invoke(String api, String data){
        return new Promise((resolve, reject) -> {
            interactCallServer("logic",api, data).then(res -> {
                resolve.success(res);
                return null;
            });
        });
    }
    public static Promise invoke(String api){
        return invoke(api,"{}");
    }

    private static Promise interactCallServer(String type, String api, String data){
        return new Promise(new Promise.Pro() {
            @Override
            public void invoke(Promise.Resolve resolve, Promise.Reject reject) {
                Fetch.post(baseApi + "/" + type + "/" + api ,data).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Util.log("请求结果:",string);
                        ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject result = new JSONObject(string);
                                    String error = result.optString("error");
                                    if(!"".equals(error)){
                                        Util.showT(error);
                                        resolve.success(result);
                                        return;
                                    }
                                    if ("finish".equals(result.optString("verb"))) {
                                        resolve.success(result.get("args"));
                                    }else{
                                        TypeInvokes verb = interactionTypes.get(result.optString("verb"));
                                        if(verb != null){
                                            verb.call(result.getString("next"), result.get("args")).then(res -> {
                                                resolve.success(res);
                                                return null;
                                            });
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    private interface TypeInvokes{
        Promise call(String next,Object pck);
    }

    private static HashMap<String,TypeInvokes> interactionTypes = new HashMap<String,TypeInvokes>(){{
        put("redirect", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                String url = ((JSONObject) pck).optString("url");
                ZKToolApi.runtime.executeVoidScript("stack.get().open('"+url+"')");
                return interactCallServer("interact",next,"{}");
            }
        });
        put("popup", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                return interactCallServer("interact",next,"{}");
            }
        });
        put("retrieve", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                JSONArray array = (JSONArray) pck;
                JSONObject result = new JSONObject();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        Object o = ZKLocalStorage.get(array.getString(i), "");
                        result.put(array.getString(i),o);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return interactCallServer("interact",next,result.toString());
            }
        });
        put("info", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                return interactCallServer("interact",next,"{}");
            }
        });
        put("store", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                return interactCallServer("interact",next,"{}");
            }
        });
        put("wsconnect", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                ZKSocket.emit("tokenify",pck+"");
                return interactCallServer("interact",next,"{}");
            }
        });
        put("input", new TypeInvokes() {
            @Override
            public Promise call(String next, Object pck) {
                return interactCallServer("interact",next,"{}");
            }
        });
    }};
}
