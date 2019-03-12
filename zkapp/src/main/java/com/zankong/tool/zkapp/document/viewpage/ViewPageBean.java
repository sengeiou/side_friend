package com.zankong.tool.zkapp.document.viewpage;

import android.support.design.widget.TabLayout;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.fragment.ZKFragment;
import com.zankong.tool.zkapp.util.Util;

import org.dom4j.Element;

/**
 * Created by YF on 2018/6/28.
 */

public class ViewPageBean {
    private ZKFragment mZKFragment;
    private Element mElement;
    private TabLayout.Tab mTab;
    private V8Object data;
    private String src;

    private ViewPageBean(ViewPageBeanBuilder builder){
        this.mElement = builder.mElement;
        this.mTab = builder.mTab;
        this.data = builder.data;
        this.src = builder.src;
    }

    public void setZKFragment(ZKFragment ZKFragment) {
        mZKFragment = ZKFragment;
    }

    public ZKFragment getZKFragment() {
        return mZKFragment;
    }

    public Element getElement() {
        return mElement;
    }

    public TabLayout.Tab getTab() {
        return mTab;
    }

    public V8Object getData() {
        return data;
    }

    public String getSrc() {
        return src;
    }

    public static class ViewPageBeanBuilder{
        private Element mElement;
        private TabLayout.Tab mTab;
        private V8Object data;
        private String src;
        public ViewPageBeanBuilder(){
        }

        public ViewPageBeanBuilder setSrc(String src) {
            this.src = src;
            return this;
        }

        public ViewPageBean builder(){
            return new ViewPageBean(this);
        }

        public ViewPageBeanBuilder setElement(Element element) {
            mElement = element;
            return this;
        }

        public ViewPageBeanBuilder setTab(TabLayout.Tab tab) {
            mTab = tab;
            return this;
        }

        public ViewPageBeanBuilder setData(V8Object data) {
            this.data = data;
            return this;
        }
    }
}
