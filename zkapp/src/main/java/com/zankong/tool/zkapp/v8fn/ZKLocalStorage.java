package com.zankong.tool.zkapp.v8fn;

import android.content.Context;
import android.content.SharedPreferences;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.util.Map;

/**
 * Created by YF on 2018/6/29.
 */

@ZKAppV8
public class ZKLocalStorage implements ZKV8Fn {
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mEditor;
    private Context mContext;
    private V8Object localStorage;

    public ZKLocalStorage() {
        mContext = ZKToolApi.getInstance().getContext();
        mSharedPreferences = mContext.getSharedPreferences("ZK",Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        localStorage = new V8Object(ZKToolApi.runtime);
    }

    @Override
    public void addV8Fn() {
        localStorage.registerJavaMethod((receiver, parameters) -> {
            if (parameters.get(0) instanceof V8Array){
                V8Object result = new V8Object(ZKToolApi.runtime);
                V8Array array = parameters.getArray(0);
                for(int i = 0 ; i < array.length() ; i++){
                    String key = array.getString(i);
                    Object value = get(key,"");
                    result.add(key,value);
                }
                return result;
            }
            String key = null;
            Object defaultValue = "";
            if (parameters.length() >= 1) {
                key = parameters.getString(0);
            }
            if (parameters.length() >= 2){
                defaultValue = parameters.get(1);
            }
            return get(key,defaultValue);
        },"get");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            V8Object object = parameters.getObject(0);
            for (String s : object.getKeys()) {
                put(s,object.get(s));
            }
            return true;
        },"set");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            V8Object object = parameters.getObject(0);
            for (String s : object.getKeys()) {
                put(s,object.get(s));
            }
            return true;
        },"put");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            V8Array array = parameters.getArray(0);
            for(int i = 0 ; i < array.length() ; i++){
                mEditor.remove(array.getString(i));
            }
            return mEditor.commit();
        },"remove");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            String key = null;
            Object defaultValue = "";
            if (parameters.length() >= 1) {
                key = parameters.getString(0);
            }
            if (parameters.length() >= 2){
                defaultValue = parameters.get(1);
            }


            return get(key,defaultValue);
        },"getItem");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            return put(parameters.getString(0),parameters.get(1));
        },"setItem");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            mEditor.remove(parameters.getString(0));
            return mEditor.commit();
        },"removeItem");
        localStorage.registerJavaMethod((receiver, parameters) -> {
            mEditor.clear();
            return mEditor.commit();
        },"clear");
        ZKToolApi.runtime.add("localStorage",localStorage);
    }

    /**
     * 存
     */
    public static Boolean put(String key,Object obj){
        if (obj instanceof V8Object){
            mEditor.putString(key, V8Utils.js2string(obj));
        }else if (obj instanceof Integer){
            mEditor.putInt(key, (Integer) obj);
        }else if (obj instanceof Boolean){
            mEditor.putBoolean(key, (Boolean) obj);
        }else if (obj instanceof Double){
            try{
                mEditor.putFloat(key, (Float) obj);
            }catch (Exception e){e.printStackTrace();}
        }else if (obj instanceof String){
            mEditor.putString(key, (String) obj);
        }
        return mEditor.commit();
    }

    /**
     * 取
     */
    public static Object get(String key,Object defaultValue){
        Map<String, ?> all = mSharedPreferences.getAll();
        if (all.containsKey(key)) {
            return all.get(key);
        }
        Util.log(key,all.toString());
        return defaultValue;
    }
}
