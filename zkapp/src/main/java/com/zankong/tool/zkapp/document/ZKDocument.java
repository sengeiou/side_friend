package com.zankong.tool.zkapp.document;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.code.CodeDoc;
import com.zankong.tool.zkapp.document.coordinator.ZKCoordinator;
import com.zankong.tool.zkapp.document.dialog.ZKDialog;
import com.zankong.tool.zkapp.document.fra.ZKFra;
import com.zankong.tool.zkapp.document.listview.ZKListView;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.document.plain.ZKPlain;
import com.zankong.tool.zkapp.document.popup.ZKPopup;
import com.zankong.tool.zkapp.document.popup.ZKPopupWindow;
import com.zankong.tool.zkapp.document.viewpage.ZKViewPage;
import com.zankong.tool.zkapp.fragment.ZKDialogFragment;
import com.zankong.tool.zkapp.item.NullAdapter;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.MessageEvent;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.keyword.ZKKeywordHelper;
import com.zankong.tool.zkapp.util.statusbar.StatusBarUtil;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKViews;
import com.zankong.tool.zkapp.views.recycle.RecycleBean;
import com.zankong.tool.zkapp.zk_interface.ZKActivityLife;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by YF on 2018/6/21.
 */

public abstract class ZKDocument implements Releasable,ZKActivityLife {
    /**
     * xml根标签集合
     */
    public static HashMap<String,Class<?>>    docMap = new HashMap<String, Class<?>>(){{
        put("plain",                   ZKPlain.class);
        put("viewPage".toLowerCase(),  ZKViewPage.class);
        put("listView".toLowerCase(),  ZKListView.class);
        put("code",                     CodeDoc.class);
        put("coordinator",             ZKCoordinator.class);
        put("dialog",                  ZKDialog.class);
        put("frame",                   ZKFra.class);
        put("popup",                   ZKPopup.class);
    }};
    protected Element                         mElement;                           //整个xml的元素
    private Window                            mWindow;                            //activity的window窗口
    private View                              mView;                              //布局
    private ViewGroup                         mViewGroup;                         //布局容器
    protected V8Object                        document;                           //v8里的上下文,对应整个xml页面
    protected Context                         mContext;
    protected ZKActivity                      mActivity;
    protected ArrayList<ZKViews>              mZKViewsList = new ArrayList<>();   //控件列表,document.$()专用
    protected ArrayList<ZKAdapter>            adapterMap = new ArrayList<>();     //本页面适配器集合
    private Fragment                          mFragment;                        //
    public static final String               NEXT = "next",
                                               ARGUMENTS = "arguments";
    private V8Object                          keyword;                             //软键盘
    public InputMethodManager                 imm;                                 //输入法管理器
    private ArrayList<AlbumFile>              mAlbumFiles;                        //相册选择
    public SparseArray<Bitmap>                mBitmapHashMap = new SparseArray<>();  //图片
    private ZKDialogFragment                  mZkDialogFragment;
    private ZKPopupWindow                     mZkPopupWindow;
    private HashMap<String,V8Function>       eventBusFn  = new HashMap<>();
    private String StatusBar = "";

    public DisplayMetrics getDisplayMetrics(){
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public ZKDocument(Context context,Element root,Window window){
        mContext = context;
        mElement = root;
        mWindow = window;
        mActivity = (ZKActivity) context;
        initImm();
        initV8This();
        initView();
        fillData(root);
    }

    public ZKDocument(Context context, Element root, ViewGroup viewGroup){
        mContext = context;
        mElement = root;
        mActivity = (ZKActivity) context;
        mViewGroup = viewGroup;
        initImm();
        initV8This();
        initView();
    }

    //判断是否是activity还是fragment,返回不同的fragmentManager
    public FragmentManager getFragmentManager() {
        if (mFragment != null) {
            return mFragment.getChildFragmentManager();
        }else {
            return mActivity.getSupportFragmentManager();
        }
    }

    //设置布局
    protected void setContextView(int layout){
        setContextView(LayoutInflater.from(mContext).inflate(layout,mViewGroup,false));
    }
    protected void setContextView(View view){
        if (mWindow != null){
            mWindow.setContentView(view);
        } else{
            mView = view;
        }
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);//得到软键盘
        mAlbumFiles = new ArrayList<>();
    }

    //自定义findViewById
    protected View findViewById(int id){
        if (mWindow != null)
            return mWindow.findViewById(id);
        else
            return mView.findViewById(id);
    }

    //若是fragment,则得到view
    public View getView(Fragment fragment){
        mFragment = fragment;
        fillData(mElement);
        return mView;
    }

    //得到document
    public V8Object getDocument() {
        return document;
    }

    //初始化document的js方法
    protected void initV8This(){
        document = new V8Object(ZKToolApi.runtime);
        document.addNull("onBack");
        document.addNull("onResume");
        document.addNull("onPause");
        document.addNull("onStop");
        document.addNull("onDestroy");
        keyword = new V8Object(ZKToolApi.runtime);
        keyword.registerJavaMethod((receiver, parameters) -> {
            boolean isInput = false;
            if (parameters.length() > 0){
                isInput = parameters.getBoolean(0);
            }
            if (!isInput){
                imm.toggleSoftInput(0,0);
                imm.showSoftInputFromInputMethod(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            return V8Utils.createFrom(keyword);
        },"show");
        keyword.registerJavaMethod((receiver, parameters) -> {
            (mActivity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//软键盘设为占屏幕布局
            return V8Utils.createFrom(keyword);
        },"resize");
        keyword.registerJavaMethod((receiver, parameters) -> {
            if (imm.isActive()){
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return V8Utils.createFrom(keyword);
        },"hide");
        keyword.registerJavaMethod((receiver, parameters) -> {
            (mActivity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);//软键盘设为不占屏幕布局
            return V8Utils.createFrom(keyword);
        },"nothing");
        keyword.addNull("showListener");
        keyword.addNull("hideListener");
        document.add("keyword",keyword);
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.createFrom(mActivity.getArguments());
        },"arguments");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.createFrom(document);
        },"getParent");
        document.registerJavaMethod((receiver, parameters) -> {
            String page = parameters.getString(0);
            page = Util.getPath(page);
            Intent intent = new Intent();
            intent.setClass(mContext, ZKActivity.class);
            intent.putExtra("page", page);
            if (parameters.length() > 1) {
                V8Object args = parameters.getObject(1);
                try {
                    intent.putExtra(ARGUMENTS, V8Utils.V82JSON(args).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                args.release();
            }
            return V8Utils.Promise((resolve, reject) -> mActivity.openForResult(intent, (requestCode, resultCode, data) -> {
                if (data != null){
                    resolve.call(null,V8Utils.parse(data.getStringExtra(NEXT)));
                }else {
                    reject.call(null);
                }
            }));
        },"open");
        document.registerJavaMethod((receiver, parameters) -> {
            Intent intent = new Intent();
            if (parameters.length() > 0){
                String data = V8Utils.js2string(parameters.get(0));
                intent.putExtra(NEXT,data);
            }
            mActivity.setResult(ZKNaiveActivity.requestCode100,intent);
            mActivity.finish();
            return null;
        },"next");
        document.registerJavaMethod((receiver, parameters) -> {
            String id = parameters.getString(0);
            for (ZKViews zkViews : mZKViewsList) {
                if (id.equals(zkViews.getId())) {
                    return V8Utils.createFrom(zkViews.getThisV8());
                }
            }
            return null;
        },"$");
        document.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() == 0){
                mActivity.finish();
                return null;
            }
            switch (parameters.getString(0)) {
                case "all":
                    for (ZKActivity zkActivity : ZKToolApi.getInstance().getActivities()) {
                        zkActivity.finish();
                    }
                    break;
                case "home":
                    break;
                case "other":
                    break;
            }
            return null;
        },"finish");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise((resolve, reject) -> {
                mZkPopupWindow = new ZKPopupWindow(mContext,parameters.getString(0));
                if (parameters.length() >= 3){
                    mZkPopupWindow.setArguments(parameters.getObject(2));
                }
                mZkPopupWindow.setContentView(mZkPopupWindow.getZKDocument().getView(null));
                mZkPopupWindow.showAsDropDown(mActivity.findViewById(parameters.getInteger(1)));
                mZkPopupWindow.setDismiss(obj -> {
                    if (obj != null){
                        resolve.call(null,obj);
                    }else {
                        reject.call(null);
                    }
                });
            });
        },"popup");
        document.registerJavaMethod((receiver, parameters) -> {
            if(parameters.length() == 0){
                if(mZkDialogFragment != null){
                    return V8Utils.createFrom(mZkDialogFragment.getZKDocument().getDocument());
                }else {
                    return new V8Object(ZKToolApi.runtime);
                }
            }
            return V8Utils.Promise((resolve, reject) -> {
                mZkDialogFragment = ZKDialogFragment.newInstance(parameters.getString(0),ZKDocument.this);
                if (parameters.length() >= 2){
                    mZkDialogFragment.setArguments(parameters.getObject(1));
                }
                mZkDialogFragment.show(getFragmentManager(),"zkDialog");
                mZkDialogFragment.setOnDismiss(obj -> {
                    if (obj != null){
                        resolve.call(null,obj);
                    }else {
                        reject.call(null);
                    }
                });
            });
        },"dialog");
        document.registerJavaMethod((receiver, parameters) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + parameters.getString(0)));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return V8Utils.Promise(new V8Utils.promiseHandler() {
                @Override
                public void procedure(V8Function resolve, V8Function reject) {
                    mActivity.openForResult(intent, new ZKNaiveActivity.OnForResult() {
                        @Override
                        public void result(int requestCode, int resultCode, @Nullable Intent data) {
                            resolve.call(null);
                        }
                    });
                }
            });
        },"phone");
        document.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() == 0){
                return mElement.asXML();
            }
            if (parameters.get(0) instanceof String){
                try {
                    Element newDoc = DocumentHelper.parseText(parameters.getString(0)).getRootElement();
                    for (ZKViews zkViews : loadControlsByNode(newDoc)) {
                        try {
                            zkViews.init(null);
                            mZKViewsList.add(zkViews);
                            return V8Utils.createFrom(zkViews.getThisV8());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            return null;
        },"element");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise((resolve, reject) -> {
                int column = 2;
                int select = 9;
                boolean camera = true;
                String title = "相册";
                if (parameters.length() > 0){
                    V8Object object = parameters.getObject(0);
                    for (String s : object.getKeys()) {
                        switch (s) {
                            case "column":
                                column = object.getInteger(s);
                                break;
                            case "select":
                                select = object.getInteger(s);
                                break;
                            case "camera":
                                camera = object.getBoolean(s);
                                break;
                            case "title":
                                title = object.getString(s);
                                break;
                            case "clear":
                                if (object.getBoolean(s)) {
                                    mAlbumFiles.clear();
                                }
                                break;
                        }
                    }
                }
                Album.image(mContext)
                        .multipleChoice()
                        .camera(camera)
                        .columnCount(column)
                        .selectCount(select)
                        .checkedList(mAlbumFiles)
                        .widget(Widget.newDarkBuilder(mContext).title(title).build())
                        .onResult(result -> {
                            mAlbumFiles = result;
                            V8Array array = new V8Array(ZKToolApi.runtime);
                            for (AlbumFile albumFile : result) {
                                array.push(albumFile.getPath());
                                Util.log("album",albumFile.getPath());
                            }
                            V8Array par = new V8Array(ZKToolApi.runtime);
                            par.push(array);
                            resolve.call(null,par);
                            array.release();
                            par.release();
                        })
                        .onCancel(result -> reject.call(null,result))
                        .start();
            });
        },"album");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise(new V8Utils.promiseHandler() {
                @Override
                public void procedure(V8Function resolve, V8Function reject) {
                    Album.camera(mContext)
                            .video()
                            .quality(1)
                            .limitDuration(Integer.MAX_VALUE)
                            .limitBytes(Integer.MAX_VALUE)
                            .onResult(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                    resolve.call(null,result);
                                    Util.log("video:",result);
                                }
                            })
                            .onCancel(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                    reject.call(null,result);
                                }
                            })
                            .start();
                }
            });
        },"video");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise(new V8Utils.promiseHandler() {
                @Override
                public void procedure(V8Function resolve, V8Function reject) {
                    Album.camera(mContext)
                            .image()
                            .onResult(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                   resolve.call(null,result);
                                   Util.log("camera:",result);
                                }
                            })
                            .onCancel(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                    reject.call(null,result);
                                }
                            })
                            .start();
                }
            });
        },"camera");
        document.registerJavaMethod((receiver, parameters) -> {
            String xml = parameters.getString(0);
            V8Object par = parameters.getObject(1);
            return null;
        },"showPage");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise((resolve, reject) -> {
                int key = (int) (Math.random() * 10000000);
                String message = parameters.getString(0);
                boolean hasLogo = false;
                Object url = null;
                HashMap<String ,String> type = new HashMap<>();
                if (parameters.length() >= 2){
                    V8Object options = parameters.getObject(1);
                    for (String s : options.getKeys()) {
                        switch (s) {
                            case "logo":
                                hasLogo = true;
                                url = options.get(s);
                                break;
                            case "type":
                                V8Object object = options.getObject(s);
                                for (String s1 : object.getKeys()) {
                                    type.put(s1,object.getString(s1));
                                }
                                break;
                        }
                    }
                }
                if (!hasLogo){
                    mBitmapHashMap.put(key, CodeUtils.createQRCode(message));
                    resolve.call(null,key);
                }else {
                    if (url instanceof Integer){
                        mBitmapHashMap.put(key,CodeUtils.createQRCodeWithLogo(message,mBitmapHashMap.get(key)));
                        resolve.call(null,key);
                    }else if (url instanceof String){
                        Glide.with(mContext).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mBitmapHashMap.put(key,CodeUtils.createQRCodeWithLogo(message,resource));
                                resolve.call(null,key);
                            }
                        });
                    }
                }
            });
        },"createCode");
        document.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise((resolve, reject) -> {
                CodeUtils.AnalyzeCallback callback = new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        resolve.call(null,result);
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        reject.call(null,"解析二维码出错");
                    }
                };
                if (parameters.get(0) instanceof Integer) {
                    Bitmap bitmap = mBitmapHashMap.get(parameters.getInteger(0));
                    CodeUtils.analyzeBitmap(bitmap, callback);
                }else if (parameters.get(0) instanceof String){
                    Glide.with(mContext).asBitmap().load(parameters.getString(0)).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            CodeUtils.analyzeBitmap(resource, callback);
                        }
                    });
                }
            });
        },"analyzeCode");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                eventBusFn.put(parameters.getString(0), (V8Function) parameters.getObject(1));
                Log.d("socket","eventBusFn");
                Log.d("socket",eventBusFn.keySet().toString());
                return null;
            }
        },"addEvent");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String event = parameters.getString(0);
                if (eventBusFn.containsKey(event)) {
                    V8Function v8Function = eventBusFn.get(event);
                    if (v8Function != null) {
                        v8Function.release();
                    }
                    eventBusFn.remove(event);
                }
                return null;
            }
        },"removeEvent");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        String event = messageEvent.getEvent();
        if(!messageEvent.isValid())return;
        if (eventBusFn.containsKey(event)) {
            Object call = Objects.requireNonNull(eventBusFn.get(event)).call(null, V8Utils.parse((String) messageEvent.getData()));
            if (call instanceof Boolean) {
                messageEvent.setValid(!(Boolean) call);
            }else {
                messageEvent.setValid(false);
            }
        }
    }

    //初始化Activity的布局
    protected abstract void initView();

    //释放V8
    @Override
    public void release(){
        if (document != null && !document.isReleased()) document.release();
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.release();
        }
        if (mBitmapHashMap != null){
            for (int i = 0; i < mBitmapHashMap.size(); i++) {
                mBitmapHashMap.valueAt(i).recycle();
            }
        }
    }

    public ScrollView getScrollView(){
        return null;
    }
    public RecyclerView getRecycleView(){
        return  null;
    }

    //设置appbar布局
    public void setAppBar(Element element, CollapsingToolbarLayout collapsing, AppBarLayout appBar, LinearLayout pins, View toolbar){
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "height":
                    ViewGroup.LayoutParams appbarLP = collapsing.getLayoutParams();
                    appbarLP.height = Util.px2px(value,getDisplayMetrics().heightPixels)+getStatusBarHeight();
                    collapsing.setLayoutParams(appbarLP);
                    break;
                case "toolbarheight":
                    ViewGroup.LayoutParams toolBarLP = toolbar.getLayoutParams();
                    toolBarLP.height = Util.px2px(value,getDisplayMetrics().heightPixels)+getStatusBarHeight();
                    toolbar.setLayoutParams(toolBarLP);
                    break;
            }
        }
        for (Element appBarElement : element.elements()) {
            ZKViews zkView = getZKViewByElement(appBarElement);
            if (zkView == null) continue;
            try {
                zkView.init(collapsing);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mZKViewsList.add(zkView);
            zkView.fillData();
            String collapseMode = appBarElement.attributeValue("collapseMode", "");
            CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) zkView.getView().getLayoutParams();
            switch (collapseMode) {
                case "pin":
//                    layoutParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
                    pins.addView(zkView.getView());
                    break;
                case "parallax":
                    layoutParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
                    collapsing.addView(zkView.getView(),collapsing.getChildCount()-2);
                    break;
            }
            if (zkView instanceof AppBarLayout.OnOffsetChangedListener){
                appBar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) zkView);
            }
        }
    }

    //给header,body,footer,设置布局
    public void setXml(Element element, ViewGroup view) {
        for (Attribute attribute : element.attributes()) {
            switch (attribute.getName().toLowerCase()){
                case "background":
                    view.setBackgroundColor(Color.parseColor(attribute.getValue()));
                    break;
                case "height":
                    try {
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        layoutParams.height = Util.px2px(attribute.getValue(),getDisplayMetrics().heightPixels);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
        for (ZKViews zkView : loadControlsByNode(element)) {
            try {
                zkView.init(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mZKViewsList.add(zkView);
            view.addView(zkView.getView());
            zkView.fillData();
        }
    }

    //解析float标签
    protected void analysisFloatView(Element element,ViewGroup view){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "margin":
                    String[] margin = value.split(",");
                    if (margin.length >= 1){
                        int h = Util.px2px(margin[0]);
                        if (h < 0){
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            layoutParams.rightMargin = -h;
                        }else {
                            layoutParams.leftMargin = h;
                        }
                    }
                    if (margin.length == 2){
                        int v = Util.px2px(margin[1]);
                        if (v < 0){
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            layoutParams.bottomMargin = -v;
                        }else {
                            layoutParams.topMargin = v;
                        }
                    }
                    break;
                case "left":
                    layoutParams.leftMargin = Util.px2px(value);
                    break;
                case "right":
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.rightMargin = Util.px2px(value);
                    break;
                case "top":
                    layoutParams.topMargin = Util.px2px(value);
                    break;
                case "bottom":
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.bottomMargin = Util.px2px(value);
                    break;
            }
        }
        view.setLayoutParams(layoutParams);
    }

    //document.append()的封装
    protected void appendElement(){

    }

    //解析根标签的属性
    protected void analysisDoc(Element element, View view, ImageView backgroundImg){
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()){
                case "clip":
                    StatusBar = value;
                    break;
                case "background":
                    break;
                case "background-img":
                    Glide.with(getContext()).load(ZKImgView.getUrl(value)).into(backgroundImg);
                    break;
                case "sroll":
                    break;
                case "padding":
                    String[] padding = value.split(",");
                    if (padding.length == 1) {
                        view.setPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0])
                        );
                    } else {
                        view.setPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[1]),
                                Util.px2px(padding[2]),
                                Util.px2px(padding[3])
                        );
                    }
                    break;
            }
        }
    }

    //设置状态栏
    protected void setClip(String value){
        String[] clips = value.split(",");
        for (String clip : clips) {
            if (clip.contains("#")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mActivity.getWindow().setStatusBarColor(Color.parseColor(clip));
                } else {
                    ViewGroup systemContent = (ViewGroup) mActivity.findViewById(android.R.id.content);
                    View statusBarView = new View(mContext);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight());
                    statusBarView.setBackgroundColor(Color.parseColor(clip));
                    systemContent.getChildAt(0).setFitsSystemWindows(true);
                    systemContent.addView(statusBarView, 0, lp);
                }
            } else if (clip.equals("1")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//状态栏字体---->>>>深色
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }else if (clip.equals("2")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//状态栏字体---->>>>浅色
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }
        }
    }

    //解析有item标签的xml
    protected void setAdapterXml(Element element, ZKRecycleView recyclerView) throws Exception {
        for (Attribute attribute : element.attributes()) {
            switch (attribute.getName().toLowerCase()) {
                case "background":
                    recyclerView.setBackgroundColor(Color.parseColor(attribute.getValue()));
                    break;
            }
        }
        int itemIndex = -1;
        for (Element childElement : element.elements()) {
            itemIndex++;
            if ("item".equals(childElement.getName())){
                break;
            }
        }
        ZKAdapter zkAdapter;
        if (element.element("item") == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            zkAdapter = new NullAdapter(this,null);
        }else {
            RecycleBean.Builder builder = new RecycleBean.Builder(mContext);
            for (Attribute attribute : element.element("item").attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName()) {
                    case "num":
                        builder.setNum(Util.getInt(value));
                        break;
                    case "orientation":
                        builder.setOrientation(value);
                        break;
                    case "padding":
                        String[] padding = value.split(",");
                        if (padding.length == 1) {
                            recyclerView.setPadding(
                                    Util.px2px(padding[0]),
                                    Util.px2px(padding[0]),
                                    Util.px2px(padding[0]),
                                    Util.px2px(padding[0])
                            );
                        } else {
                            recyclerView.setPadding(
                                    Util.px2px(padding[0]),
                                    Util.px2px(padding[1]),
                                    Util.px2px(padding[2]),
                                    Util.px2px(padding[3])
                            );
                        }
                        break;
                    case "type":
                        builder.setType(value);
                        break;
                    case "reverse":
                        builder.setReverse(Boolean.parseBoolean(value));
                        break;
                }
            }
            recyclerView.setLayoutManager(builder.builder().getLayoutManager());
            zkAdapter = (ZKAdapter) ZKAdapter.adapterMap.get(element.element("item").elements().get(0).getName().toLowerCase()).getConstructor(ZKDocument.class, Element.class).newInstance(this, element.element("item"));
        }
        recyclerView.setAdapter(zkAdapter);
        adapterMap.add(zkAdapter);
        int i = 0;
        for (ZKViews zkViews : loadControlsByNode(element)) {
            zkViews.init(recyclerView);
            if (i < itemIndex)  zkAdapter.addHeaderView(zkViews.getView());
            else                zkAdapter.addFooterView(zkViews.getView());
            mZKViewsList.add(zkViews);
            zkViews.fillData();
            i++;
        }
        zkAdapter.notifyDataSetChanged();
    }

    public ZKViews getZKViewByElement(Element viewElement){
        String name = viewElement.getName().toLowerCase();
        if (!ZKViews.ViewMap.containsKey(name)){
            return null;
        }
        try{
            return (ZKViews) ZKViews.ViewMap.get(name).getConstructor(ZKDocument.class,Element.class).newInstance(this,viewElement);
        }catch (Exception e){
            Util.log("错误标签:"+name);
            e.printStackTrace();
        }
        return null;
    }

    //解析二级标签,得到三级标签(ZNView);
    public ArrayList<ZKViews> loadControlsByNode(Element body){
        ArrayList<ZKViews> viewList = new ArrayList<>();
        for (Element element : body.elements()) {
            String name = element.getName().toLowerCase();
            if (!ZKViews.ViewMap.containsKey(name)){
                Util.log("没有此标签:",name);
                continue;
            }
            try{
                ZKViews one = (ZKViews) ZKViews.ViewMap.get(name).getConstructor(ZKDocument.class,Element.class).newInstance(this,element);
                viewList.add(one);
            }catch (Exception e){
                Util.log("错误标签:"+name);
                e.printStackTrace();
            }
        }
        return viewList;
    }

    ///解析页面布局,子控件
    protected abstract void fillData(Element docElement);

    public Context getContext() {
        return mContext;
    }

    public ZKActivity getActivity() {
        return mActivity;
    }

    //通过scriptJava字符串得到一个js方法
    public V8Function genContext(String script) {
        return (V8Function) ZKToolApi.runtime.executeObjectScript(
                String.format("fn = function(){let document = this;let window = this;%s}",script));
    }
    public V8Function genContextFn(String script) {
        return (V8Function) ZKToolApi.runtime.executeObjectScript(
                String.format("fn = function(...obj){let document = this;let window = this;(%s)(...obj)}",script));
    }
    public V8Function genContextVal(String script){
        return (V8Function) ZKToolApi.runtime.executeObjectScript(
                String.format("fn = function(){let document = this;let window = this;return %s}",script));
    };

    //执行js方法,document专用;
    public Object invokeWithContext(V8Function cb) {
        if (cb != null)
            return cb.call(document);
        return null;
    }
    public Object invokeWithContext(V8Function cb,Object ...args) {
        if (cb != null)
            return cb.call(document,args);
        return null;
    }


    //在生命周期里改变状态栏
    protected void changeStatusBar(){
        if("".equals(StatusBar))return;
        StatusBarUtil.setTranslucentStatus(getActivity());
        if (!"true".equals(StatusBar)){
            setClip(StatusBar);
        }
    }

    //获得状态栏高度
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        Class<?> c;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = mActivity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    //以下为生命周期
    @Override
    public void onPause() {
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onPause();
        }
        V8Function pause = (V8Function) document.getObject("onPause");
        if (pause != null) {
            pause.call(null);
            pause.release();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onPause();
        }
    }

    @Override
    public void onStart() {
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onStart();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onStart();
        }
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onCreate();
        }
    }

    @Override
    public void onResume() {
        changeStatusBar();
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onResume();
        }
        V8Function onResume = (V8Function) document.getObject("onResume");
        if (onResume != null) {
            onResume.call(null);
            onResume.release();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onStart();
        }
    }

    @Override
    public void onStop() {
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onStop();
        }
        V8Function onStop = (V8Function) document.getObject("onStop");
        if (onStop != null) {
            onStop.call(null);
            onStop.release();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onDestroy();
        }
        V8Function onDestroy = (V8Function) document.getObject("onDestroy");
        if (onDestroy != null) {
            onDestroy.call(null);
            onDestroy.release();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRestart() {
        for (ZKViews zkViews : mZKViewsList) {
            zkViews.onRestart();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            zkAdapter.onRestart();
        }
    }

    //若有一个返回值为false,则不会关闭当前页面
    @Override
    public boolean onBack() {
        boolean isBack = true;
        for (ZKViews zkViews : mZKViewsList) {
            isBack &= zkViews.onBack();
        }
        V8Function onBack = (V8Function) document.getObject("onBack");
        if (onBack != null) {
            isBack &= (Boolean) onBack.call(null);
            onBack.release();
        }
        for (ZKAdapter zkAdapter : adapterMap) {
            isBack &= zkAdapter.onBack();
        }
        return isBack;
    }

    private void initImm(){
        ZKKeywordHelper zkKeywordHelper = new ZKKeywordHelper(mContext);
        zkKeywordHelper.setOnSoftKeyBoardChangeListener(new ZKKeywordHelper.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                invokeWithContext((V8Function) keyword.getObject("showListener"),height);
            }

            @Override
            public void keyBoardHide(int height) {
                invokeWithContext((V8Function) keyword.getObject("hideListener"),height);
            }
        });
    }


}
