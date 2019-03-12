package com.zankong.tool.zkapp.views.viewpage;

import android.support.v4.view.PagerAdapter;
import android.widget.LinearLayout;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/26.
 */

public class PageBean {
    private Element mElement;
    private LinearLayout mLinearLayout;

    private PageBean(PageBeanBuilder pageBeanBuilder){
        this.mElement = pageBeanBuilder.mElement;
        this.mLinearLayout = pageBeanBuilder.mLinearLayout;
    }

    public Element getElement() {
        return mElement;
    }

    public LinearLayout getLinearLayout() {
        return mLinearLayout;
    }

    public static class PageBeanBuilder{
        private Element mElement;
        private LinearLayout mLinearLayout;

        public PageBeanBuilder() {
        }

        public PageBeanBuilder setElement(Element element) {
            mElement = element;
            return this;
        }

        public PageBeanBuilder setLinearLayout(LinearLayout linearLayout) {
            mLinearLayout = linearLayout;
            return this;
        }
        public PageBean builder(){
            return new PageBean(this);
        }
    }
}
