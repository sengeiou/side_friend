package com.zankong.tool.side_friend.diy_view.toolbar;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;


public class ZKToolbar_2 extends ZKViewAgent implements AppBarLayout.OnOffsetChangedListener {

    private TextView address;
    private LinearLayout mAddressBtn;
    private LinearLayout mSearchBtn;
    private int statusBarHeight = getZKDocument().getStatusBarHeight();

    public ZKToolbar_2(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_toolbar_2);
    }

    @Override
    public void fillData(Element selfElement) {
        ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
        layoutParams.height = statusBarHeight;
        getView().setLayoutParams(layoutParams);
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
    }

    @Override
    public Object getResult() {
        return null;
    }


    private int bannerH = getZKDocument().getDisplayMetrics().widthPixels * 9 / 16;
    private int toolbarH = Util.px2px("144");
    private int h = bannerH - toolbarH - getZKDocument().getStatusBarHeight();
    private int bColor = Color.parseColor("#5c9afd");
    private int red = (bColor & 0xff0000)>>16,green = (bColor & 0x00ff00)>>8,blue = bColor & 0x0000ff;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int backColor = Color.argb(255*-i/h,red,green,blue);
        int c = (h+i)*255/h;
        getView().setBackgroundColor(backColor);
    }
}
