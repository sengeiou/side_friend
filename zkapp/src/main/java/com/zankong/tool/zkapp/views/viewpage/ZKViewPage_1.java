package com.zankong.tool.zkapp.views.viewpage;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YF on 2018/7/26.
 */

public class ZKViewPage_1 extends ZKViewAgent {
    private ArrayList<Element> mPageElements;
    private ViewPager mViewPager;
    private LinearLayout mPoints;

    public ZKViewPage_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mPageElements = new ArrayList<>();
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_zkviewpage_1);
        mViewPager = ((ViewPager) findViewById(R.id.view_pager));
        mPoints = ((LinearLayout) findViewById(R.id.point));
    }

    @Override
    public void fillData(Element selfElement) {
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "page":
                    mPageElements.add(element);
                    ImageView imageView = new ImageView(getContext());
                    mPoints.addView(imageView);
                    imageView.setBackgroundResource(R.drawable.point_on);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    layoutParams.setMargins(5,5,5,5);
                    imageView.setLayoutParams(layoutParams);
                    break;
            }
        }
        if (mPoints.getChildCount() >= 0){
            View childAt = mPoints.getChildAt(0);
            childAt.setBackgroundResource(R.drawable.point_off);
        }
        mViewPager.setAdapter(new ZKViewPageAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int page) {
                for (int i = 0; i < mPoints.getChildCount(); i++) {
                    mPoints.getChildAt(i).setBackgroundResource(i == page ? R.drawable.point_off:R.drawable.point_on);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public Object getResult() {
        return null;
    }




    //自定义pagerAdapter
    private class ZKViewPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPageElements.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            getZKDocument().setXml(mPageElements.get(position),linearLayout);
            container.addView(linearLayout);
            return linearLayout;
        }
    }
}
