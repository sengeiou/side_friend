package com.zankong.tool.zkapp.document.viewpage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.fragment.ZKFragment;
import com.zankong.tool.zkapp.util.Util;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Created by YF on 2018/6/28.
 */

public class ZKViewPageLayout extends ViewPager {
    private PagerAdapter mPagerAdapter;
    private ArrayList<ViewPageBean> mFragments;
    private ZKDocument mZKDocument;
    private ZKTabLayout mZKTabLayout;
    private boolean canChange = false;
    public ZKViewPageLayout(@NonNull Context context) {
        super(context);
        init();
    }
    public ZKViewPageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mFragments = new ArrayList<>();
    }

    /**
     * 解析xml
     */
    public void fillData(Element viewPageElement, ZKTabLayout tabLayout, ZKDocument zkDocument){
        mZKDocument = zkDocument;
        mZKTabLayout = tabLayout;
        for (Element element : viewPageElement.elements()) {
            mFragments.add(createViewBean(element).builder());
        }
        for (Attribute attribute : viewPageElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "change":
                    if ("true".equals(value)){
                        canChange = true;
                        mPagerAdapter = new ZKFragmentStatePagerAdapter(zkDocument.getFragmentManager());
                    }else {
                        mPagerAdapter = new ZKFragmentAdapter(zkDocument.getFragmentManager());
                    }
                    break;
            }
        }
        if (mPagerAdapter == null){
            mPagerAdapter = new ZKFragmentAdapter(zkDocument.getFragmentManager());
        }
        setAdapter(mPagerAdapter);
        mZKTabLayout.setupWithViewPager(this);
    }

    private ViewPageBean.ViewPageBeanBuilder createViewBean(Element element){
        ViewPageBean.ViewPageBeanBuilder beanBuilder = new ViewPageBean.ViewPageBeanBuilder()
                .setTab(mZKTabLayout.newTab().setText(element.getTextTrim()))
                .setElement(element);
        for (Attribute attribute : element.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "src":
                    beanBuilder.setSrc(value);
                    break;
                case "data":
                    V8Function v8Function = mZKDocument.genContextVal(value);
                    beanBuilder.setData((V8Object) mZKDocument.invokeWithContext(v8Function));
                    v8Function.release();
                    break;
            }
        }
        return beanBuilder;
    }

    public void addPage(Element element,@Nullable V8Object object){
        if (!canChange)return;
        ViewPageBean viewPageBean = createViewBean(element).setData(object).builder();
        mFragments.add(viewPageBean);
        mZKTabLayout.addTab(viewPageBean.getTab());
        mPagerAdapter.notifyDataSetChanged();
    }
    public void delete(@NonNull String title){
        if (!canChange)return;
        int index = 0;
        for (ViewPageBean viewPageBean : mFragments) {
            if (title.equals(viewPageBean.getTab().getText())) {
                mZKTabLayout.removeTabAt(index);
                mFragments.remove(index);
                mPagerAdapter.notifyDataSetChanged();
                break;
            }
            index ++;
        }
    }

    public PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    public ArrayList<ViewPageBean> getFragments() {
        return mFragments;
    }

    public ZKDocument getZKDocument() {
        return mZKDocument;
    }

    private class ZKFragmentAdapter extends FragmentPagerAdapter {

        public ZKFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Util.log("getItem",i+"");
            Element element = mFragments.get(i).getElement();
            ZKFragment.ZKFragmentBuilder builder = new ZKFragment.ZKFragmentBuilder()
                    .setIndex(i)
                    .setParent(mZKDocument);
            for (Attribute attribute : element.attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName()) {
                    case "src":
                        builder.setPage(value);
                        break;
                    case "data":
                        V8Function v8Function = mZKDocument.genContextVal(value);
                        builder.setData((V8Object) mZKDocument.invokeWithContext(v8Function));
                        v8Function.release();
                        break;
                }
            }
            ZKFragment zkFragment = builder.builder();
            mFragments.get(i).setZKFragment(zkFragment);
            return zkFragment;
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTab().getText();
        }
    }
    private class ZKFragmentStatePagerAdapter extends FragmentStatePagerAdapter{
        private ZKFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            ViewPageBean viewPageBean = mFragments.get(i);
            ZKFragment.ZKFragmentBuilder builder = new ZKFragment.ZKFragmentBuilder()
                    .setIndex(i)
                    .setParent(mZKDocument)
                    .setPage(viewPageBean.getSrc())
                    .setData(viewPageBean.getData());
            ZKFragment zkFragment = builder.builder();
            viewPageBean.setZKFragment(zkFragment);
            return zkFragment;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getElement().getText();
        }
    }
}
