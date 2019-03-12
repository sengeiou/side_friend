package com.zankong.tool.zkapp.document.coordinator;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.ZKAppDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/3.
 */

@ZKAppDocument("coordinator")
public class ZKCoordinator extends ZKDocument {


    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mCollapsing;
    private LinearLayout mBody;
    private LinearLayout mFooter;
    private LinearLayout mHeader;
    private LinearLayout mPins;

    public ZKCoordinator(Context context, Element root, Window window) {
        super(context, root, window);
    }

    public ZKCoordinator(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }

    @Override
    protected void initView() {
        setContextView(R.layout.doc_coordinator);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        mBody = ((LinearLayout) findViewById(R.id.body));
        mFooter = ((LinearLayout) findViewById(R.id.footer));
        mHeader = ((LinearLayout) findViewById(R.id.header));
        mPins = ((LinearLayout) findViewById(R.id.pins));
    }

    @Override
    protected void fillData(Element docElement) {
        analysisDoc(docElement,findViewById(R.id.back_layout),null);
        for (Element element : docElement.elements()) {
            switch (element.getName().toLowerCase()) {
                case "appbar":
//                    mPins.setPadding(0,getStatusBarHeight(),0,0);
                    setAppBar(element,mCollapsing,mAppBar,mPins,findViewById(R.id.toolbar));
                    break;
                case "toolbar":
                    break;
                case "header":
                    setXml(element,mHeader);
                    break;
                case "body":
                    setXml(element,mBody);
                    break;
                case "footer":
                    setXml(element,mFooter);
                    break;
            }
        }
    }

}
