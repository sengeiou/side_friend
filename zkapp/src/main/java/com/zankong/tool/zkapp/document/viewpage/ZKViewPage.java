package com.zankong.tool.zkapp.document.viewpage;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;

/**
 * Created by YF on 2018/6/22.
 */

@ZKAppDocument("viewpage")
public class ZKViewPage extends ZKDocument {
    private ZKViewPageLayout         mViewPage;               //viewPage布局
    private LinearLayout             mHeader;                 //头布局
    private LinearLayout             mFooter;                 //尾布局
    private LinearLayout             mPins;
    private ZKTabLayout              mTabLayout;              //和viewPage相关联的title
    private AppBarLayout             mAppBarLayout;
    private CollapsingToolbarLayout mCollapsing;


    public ZKViewPage(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKViewPage(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }
    @Override
    protected void initView() {
        setContextView(R.layout.doc_viewpage);
        analysisDoc(mElement,findViewById(R.id.back_layout),null);
        mAppBarLayout = ((AppBarLayout) findViewById(R.id.app_bar_layout));
        mViewPage = ((ZKViewPageLayout) findViewById(R.id.zk_view_page));
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mFooter = ((LinearLayout) findViewById(R.id.footer));
        mPins = ((LinearLayout) findViewById(R.id.pins));
        mTabLayout = (ZKTabLayout) findViewById(R.id.title);
        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);

    }

    @Override
    protected void fillData(Element docElement) {
        for (Element element : docElement.elements()) {
            switch (element.getName()) {
                case "appbar":
//                    mPins.setPadding(0,getStatusBarHeight(),0,0);
                    setAppBar(element,mCollapsing,mAppBarLayout,mPins,findViewById(R.id.toolbar));
                    break;
                case "header":
                    setXml(element,mHeader);
                    break;
                case "body":
                    Util.log("createViewBean",(mTabLayout == null) +"");
                    mViewPage.fillData(element,mTabLayout,this);
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
            }
        }
    }

    @Override
    protected void initV8This() {
        super.initV8This();
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                Util.showT(parameters.getInteger(0)+"");
                return null;
            }
        },"showFragment");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.get(0) instanceof Integer){
                    return V8Utils.createFrom(mViewPage.getFragments().get(parameters.getInteger(0)).getZKFragment().getZKDocument().getDocument());
                }else if (parameters.get(0) instanceof String){
                    for (ViewPageBean viewPageBean : mViewPage.getFragments()) {
                        if (parameters.getString(0).equals(viewPageBean.getElement().getTextTrim())) {
                            return V8Utils.createFrom(viewPageBean.getZKFragment().getZKDocument().getDocument());
                        }
                    }
                }
                return null;
            }
        },"getChild");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object data = null;
                Element page = DocumentHelper.createElement("page");
                if (parameters.get(0) instanceof V8Object) {
                    V8Object object = parameters.getObject(0);
                    for (String name : object.getKeys()) {
                        switch (name){
                            case "src":
                                page.addAttribute("src",object.getString("src"));
                                break;
                            case "data":
                                data = object.getObject("data");
                                break;
                            case "title":
                                page.addText(object.getString("title"));
                                break;
                        }
                    }
                    object.release();
                }
                Util.log("pageElement",page.asXML());
                mViewPage.addPage(page,data);
                return null;
            }
        },"addPage");
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mViewPage.delete(parameters.getString(0));
                return null;
            }
        },"delete");
    }

    @Override
    public void onPause() {
        super.onPause();
//        for (Fragment fragment : getFragmentManager().getFragments()) {
//            fragment.setUserVisibleHint(false);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        for (Fragment fragment : getFragmentManager().getFragments()) {
//            fragment.setUserVisibleHint(true);
//        }
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
