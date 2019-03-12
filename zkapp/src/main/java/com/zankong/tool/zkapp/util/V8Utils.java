package com.zankong.tool.zkapp.util;

import android.util.Log;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ResultUndefined;
import com.zankong.tool.zkapp.ZKToolApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YF on 2017/3/8.
 */
public class V8Utils {

    /**
     * 使js返回V8Object对象时,V8Object对象不被回收掉
     */
    public static V8Object createFrom(V8Object object) {
        if (object == null) {
            return null;
        }

        V8Object ret = new V8Object(object.getRuntime());
        ret.setPrototype(object);
        return ret;
    }

    /**
     * Promise接口
     */
    public interface promiseHandler {
        void procedure(V8Function resolve, V8Function reject);
    }

    /**
     * 释放V8Array
     */
    public static void releaseV8array(V8Array array) {
        int length = array.length();
        for (int i = 0; i < length; i++) {
            if (array.get(i) instanceof Releasable) {
                ((Releasable) array.get(i)).release();
            }
        }
    }

    /**
     * 吧jsObject转换为string
     */
    public static String js2string(Object obj) {
        String s = "";
        if (obj instanceof V8Object) {
            if (((V8Object) obj).isUndefined()) {
                s = "undefined";
            } else {
                try {
                    s = (String) ZKToolApi.getInstance().stringify.call(null, (V8Object) obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    s = "Object object";
                }
            }
        } else if (obj instanceof String) {
            s = (String) obj;
        } else if (obj instanceof Boolean) {
            s = obj + "";
        } else if (obj instanceof Double) {
            s = obj + "";
        } else if (obj instanceof Integer) {
            s = obj + "";
        }
        return s;
    }

    /**
     * Object.assign();
     */
    public static V8Object mixV8Object(V8Object obj1, V8Object obj2) {
        return (V8Object) ZKToolApi.getInstance().mixObject.call(null, obj1, obj2);
    }

    /**
     * JSON.parse(s);
     */
    public static V8Object parse(String s) {
        return (V8Object) ZKToolApi.getInstance().parse.call(null, s);
    }
    public static V8Array parseArray(String s) {
        return (V8Array) ZKToolApi.getInstance().parse.call(null, s);
    }

    /**
     * v8转jsonObject
     */
    public static JSONObject V82JSON(V8Object object) throws JSONException {
        String json = (String) ZKToolApi.getInstance().stringify.call(null, object);
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
    }

    /**
     * JSON.stringify(obj);
     */
    public static String stringify(V8Object object) {
        return (String) ZKToolApi.getInstance().stringify.call(null, object);
    }

    /**
     * instanceOf
     */
    public static Boolean instanceOf(V8Object a, V8Object b) {
        return (Boolean) ZKToolApi.getInstance().instanceOf.call(null, a, b);
    }


    /**
     * promise的java实现
     */
    public static V8Object Promise(final promiseHandler handler) {
        final promiseHandler[] tmpHandler = {handler};
        V8Function promiseFn = new V8Function(ZKToolApi.runtime, new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (tmpHandler[0] != null) {
                    V8Function resolve = (V8Function) parameters.get(0);
                    V8Function reject = (V8Function) parameters.get(1);
                    tmpHandler[0].procedure(resolve, reject);
                    tmpHandler[0] = null;
                }
                return null;
            }
        });
        V8Object promiseObj = (V8Object) ZKToolApi.getInstance().promiseJSCreator.call(null, promiseFn);
        //        promiseFn.release();
        return promiseObj;
    }


    /**
     * @author Fsnzzz
     * @time 2018/11/29 0029  12:20
     * @describe 时间转换
     */

    public static String onTimeChange(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Log.e("ppppppppp",time);
        Date date = null;
        try {
            date = formatter.parse(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sDate = sdf.format(date);

            return sDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
