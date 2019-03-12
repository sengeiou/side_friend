package com.zankong.tool.zkapp;

import android.content.Context;
import android.os.Handler;

import com.bumptech.glide.request.target.ViewTarget;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.MediaLoader;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.util.ZKAppDocument;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.util.ZKAppView;
import com.zankong.tool.zkapp.v8fn.ZKAlert;
import com.zankong.tool.zkapp.v8fn.ZKCache;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ZKFetch;
import com.zankong.tool.zkapp.v8fn.ZKFile;
import com.zankong.tool.zkapp.v8fn.ZKFormData;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.v8fn.notify_pck.ZKNotify;
import com.zankong.tool.zkapp.v8fn.ZKSetTimeout;
import com.zankong.tool.zkapp.v8fn.ZKStack;
import com.zankong.tool.zkapp.v8fn.ZKVersion;
import com.zankong.tool.zkapp.v8fn.ZKVibrator;
import com.zankong.tool.zkapp.v8fn.ZKconsole;
import com.zankong.tool.zkapp.v8fn.media_pck.ZKMedia;
import com.zankong.tool.zkapp.v8fn.record_pck.ZKRecord;
import com.zankong.tool.zkapp.v8fn.socket_pck.ZKSocket;
import com.zankong.tool.zkapp.views.ZKViews;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dalvik.system.DexFile;

//import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by YF on 2018/6/21.
 */

public class ZKToolApi{
    public static V8                           runtime = V8.createV8Runtime();      // js运行环境,全局只有这一个
    private static ZKToolApi                   instance;                            //
    private Context                            context;                             //
    private ArrayList<ZKActivity>              activities;                          //activity集合
    private Handler                            jsHandler;                           //js执行线程
    public V8Function                          promiseJSCreator,                    //java版promise
                                                instanceOf,                          //java版instanceOf
                                                stringify,                           //java版JSON.stringify();
                                                parse,                               //java版JSON.parse();
                                                mixObject;                           //java版Object.assign();
    private HashMap<String,V8Object>          callBacks;                           //页面跳转时传递的js对象
    public static String baseApi = "";
    /**
     * 私有构造函数
     */
    private ZKToolApi(Context context){
        this.context = context.getApplicationContext();
    }

    /**
     * zk工具的入口,初始化
     */
    public static void init(Context context) {
        if (instance == null){
            synchronized (ZKToolApi.class){
                if (instance == null){
                    instance = new ZKToolApi(context);
                    instance.initApplication();
                }
            }
        }
    }

    /**
     * 初始化app
     */
    private void initApplication(){
        jsHandler = new Handler();
        activities = new ArrayList<>();
        callBacks = new HashMap<>();
        ZXingLibrary.initDisplayOpinion(this.context);
        try {
            initV8Fn();
        } catch (IOException e) {
            e.printStackTrace();
            Util.log("appV8Fn解析出错");
        }
        initDocument();
        initViews();
        initAdapters();
        initAlbum();
        Element elementByPath = Util.getElementByPath("config.xml");
        for (Element element : elementByPath.elements()) {
            switch (element.getName()) {
                case "service":
                    baseApi = element.element("http").getTextTrim();
                    break;
            }
        }
    }

    public ArrayList<ZKActivity> getActivities() {
        return activities;
    }

    public ZKActivity getTopActivity(){
        return activities.get(activities.size()-1);
    }

    public static ZKToolApi getInstance(){
        return instance;
    }

    public Context getContext(){
        return context;
    }

    public Handler getJsHandler() {
        return jsHandler;
    }

    public HashMap<String, V8Object> getCallBacks() {
        return callBacks;
    }

    private void initAlbum(){
        Album.initialize(AlbumConfig.newBuilder(context)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );
    }

    /**
     * 把所有增加ZKAppDocument注解的document增加到集合里
     */
    private void initDocument(){

        ViewTarget.setTagId(R.id.glide_tag);
        List<String> className = new ArrayList<>();
        //获得工具下view
        className.addAll(getZKClass("com.zankong.tool.zkapp.document"));
        //获得自定义的view
        className.addAll(getZKClass(context.getPackageName()));
        for (String s : className) {
            try{
                Class<?> cla = Class.forName(s);
                if (cla.isAnnotationPresent(ZKAppDocument.class)) {
                    ZKAppDocument annotation = cla.getAnnotation(ZKAppDocument.class);
                    if ("".equals(annotation.value()))continue;
                    ZKDocument.docMap.put(annotation.value().toLowerCase(), cla);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 把所有增加ZKAppV8注解的document增加到集合里
     */
    private void initV8Fn() throws IOException {
        promiseJSCreator = (V8Function) (runtime.executeObjectScript("(fn)=>new Promise((res,rej)=>fn(res,rej))"));
        instanceOf = (V8Function) (runtime.executeObjectScript("(a,b)=>a instanceof b"));
        stringify = (V8Function) runtime.executeObjectScript("(obj)=>JSON.stringify(obj)");
        parse = (V8Function) runtime.executeObjectScript("(str)=>JSON.parse(str)");
        mixObject = (V8Function) runtime.executeObjectScript("(obj1,obj2)=>Object.assign(obj1,obj2)");
        List<String> className = new ArrayList<>();
        //获得工具下jsFn
        className.addAll(getZKClass("com.zankong.tool"));
        //获得自定义的jsFn
//        className.addAll(getZKClass(context.getPackageName()));
        ArrayList<Class> classArrayList = new ArrayList<Class>(){{
            add(ZKMedia.class);
            add(ZKRecord.class);
            add(ZKSocket.class);
            add(ZKAlert.class);
            add(ZKCache.class);
            add(ZKconsole.class);
            add(ZKFetch.class);
            add(ZKFile.class);
            add(ZKFormData.class);
            add(ZKLocalStorage.class);
            add(ZKNotify.class);
            add(ZKSetTimeout.class);
            add(ZKStack.class);
            add(ZKVersion.class);
            add(ZKVibrator.class);
        }};
        try {
            for (String s : className) {
                try{
                    Class<?> cla = Class.forName(s);
                    if (cla.isAnnotationPresent(ZKAppV8.class)) {
                        if (!classArrayList.contains(cla)) {
                            classArrayList.add(cla);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (Class aClass : classArrayList) {
                ((ZKV8Fn) aClass.newInstance()).addV8Fn();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        runtime.executeScript(Util.getFileFromAssets("app.js"));
    }

    /**
     * 把所有增加ZKAppView注解的view增加到ZKViews.ViewMap集合里
     */
    private void initViews(){
        List<String> className = new ArrayList<>();
        //获得工具下view
        className.addAll(getZKClass("com.zankong.tool.zkapp.views"));
        //获得自定义的view
        className.addAll(getZKClass(context.getPackageName()));
        for (String s : className) {
            try{
                Class<?> cla = Class.forName(s);
                if (cla.isAnnotationPresent(ZKAppView.class)) {
                    ZKAppView annotation = cla.getAnnotation(ZKAppView.class);
                    if ("".equals(annotation.value()))continue;
                    ZKViews.ViewMap.put(annotation.value().toLowerCase(), cla);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 把所有增加ZKAppAdapter注解的item增加到ZKAdapter.adapterMap集合里
     */
    private void initAdapters(){
        List<String> className = new ArrayList<>();
        //获得工具下view
        className.addAll(getZKClass("com.zankong.tool.zkapp.item"));
        //获得自定义的view
        className.addAll(getZKClass(context.getPackageName()));
        for (String s : className) {
            try{
                Class<?> cla = Class.forName(s);
                if (cla.isAnnotationPresent(ZKAppAdapter.class)) {
                    ZKAppAdapter annotation = cla.getAnnotation(ZKAppAdapter.class);
                    if ("".equals(annotation.value()))continue;
                    ZKAdapter.adapterMap.put(annotation.value().toLowerCase(), cla);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得指定包名下的所有class的类名
     */
    private List<String> getZKClass(String packageName){
        List<String >classNameList=new ArrayList<String >();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Util.log(packageName,classNameList.toString());
        return  classNameList;
    }
}
