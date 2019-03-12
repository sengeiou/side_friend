package com.zankong.tool.zkapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;

import org.dom4j.Element;

public class ZKActivity extends ZKNaiveActivity{
    protected ZKDocument            mZKDocument;              //activity对应的document
    public String                    page = "";                //activity使用的xml
    protected V8Object              arguments;
    public int height;
    public int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(page == ""){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            page = getFirstPath();
            if (page.contains("http://") || page.contains("https://")){
                Util.getElementByHttp(page, element -> {
                    if (element == null){
                        setNullView();
                        return;
                    }
                    String args = getIntent().getStringExtra(ZKDocument.ARGUMENTS);
                    if (args != null) arguments = V8Utils.parse(args);
                    try {
                        setZKDocument(element);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                });
                return;
            }else {

            }
            String args = getIntent().getStringExtra(ZKDocument.ARGUMENTS);
            if (args != null) arguments = V8Utils.parse(args);
        }
        Element root = Util.getElementByPath(page);
        if (root == null){
            setNullView();
            return;
        }
        try {
            setZKDocument(root);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    /**
     * 设置document
     */
    private void setZKDocument(Element root) throws Exception{
        mZKDocument = (ZKDocument) ZKDocument.docMap.get(root.getName().toLowerCase()).getConstructor(Context.class,Element.class, Window.class).newInstance(this,root,getWindow());//        for (ZKActivity activity : ZKToolApi.getInstance().getActivities()) {
        //关闭之前的重复的activity
        for (ZKActivity zkActivity : ZKToolApi.getInstance().getActivities()) {
            if (zkActivity.page.equals(page)){
                zkActivity.finish();
            }
        }
        mZKDocument.getDocument().add("page",page);
        mZKDocument.onCreate();
        ZKToolApi.getInstance().getActivities().add(this);
    }

    public V8Object getArguments(){
        if (arguments == null){
            arguments = new V8Object(ZKToolApi.runtime);
        }
        return arguments;
    }

    /**
     * 解析出错,页面设置为默认页面
     */
    private void setNullView(){
        setContentView(R.layout.main_activity_null);
    }

    @Override
    public void release() {
        super.release();
        if (mZKDocument != null)mZKDocument.release();
        if (arguments != null)arguments.release();
    }

    /**
     * 得到第一次启动的路径
     */
    private String getFirstPath(){
        if (getIntent().getStringExtra("page") != null){
            return Util.getPath(getIntent().getStringExtra("page"));
        }
        boolean isFirst = false;
        String firstPage = "index.xml";
        Element config = Util.getElementByPath("config.xml");
        if (config == null){
            return "index.xml";
        }
        for (Element element : config.elements()) {
            if (isFirst)break;
            switch (element.getName()) {
                case "first":
                    SharedPreferences shared = getSharedPreferences("ZKFirstStart", MODE_PRIVATE);
                    boolean isFirstStart = shared.getBoolean("isFirst", true);
                    SharedPreferences.Editor editor = shared.edit();
                    if (isFirstStart){
                        firstPage = element.getText();
                        isFirst = true;
                        editor.putBoolean("isFirst",false);
                        editor.apply();
                    }
                    break;
                case "index":
                    firstPage = element.getText();
                    break;
            }
        }
        return firstPage;
    }

    /**
     * activity回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        boolean isBack = true;
        if (mZKDocument != null) {
            isBack = mZKDocument.onBack();
        }
        if (isBack){
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mZKDocument != null) {
            mZKDocument.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mZKDocument != null) {
            mZKDocument.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mZKDocument != null) {
            mZKDocument.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mZKDocument != null) {
            mZKDocument.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mZKDocument != null) {
            mZKDocument.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mZKDocument != null) {
            mZKDocument.onDestroy();
        }
        ZKToolApi.getInstance().getActivities().remove(this);
    }


}
