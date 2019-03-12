package com.zankong.tool.zkapp.views.banner;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by YF on 2018/7/25.
 */

public class Banner_1 extends ZKViewAgent {
    private ZKPView mTitle;//显示图片介绍
    private LinearLayout mPoints;//图片位置小点
    private ViewPager mViewPager;
    private BannerAdapter mAdapter;
    private BannerHandler mHandler;//定时切换
    private ArrayList<View> mImages;//图片布局集合
    private int delayTime = 5000;//默认两秒的延迟时间
    private boolean isHandlerReleased = false;//是否清空所有的定时切换

    public Banner_1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mImages = new ArrayList<>();
        mHandler = new BannerHandler(this);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_banner_1);
        mTitle = ((ZKPView) findViewById(R.id.title));
        mPoints = ((LinearLayout) findViewById(R.id.point));
        mViewPager = ((ViewPager) findViewById(R.id.banner));
    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "time":
                    delayTime = Util.getInt(value);
                    break;
                case "size":
                    String[] split = value.split(":");
                    int height = getZKDocument().getDisplayMetrics().widthPixels *Integer.parseInt(split[1]) / Integer.parseInt(split[0]);
                    ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
                    layoutParams.height = height;
                    getView().setLayoutParams(layoutParams);
                    break;
            }
        }
        for (Element element : selfElement.elements()) {
            switch (element.getName()) {
                case "img":
                    createImg(element);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setBackgroundResource(R.drawable.point_off);
                    mPoints.addView(imageView);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    layoutParams.setMargins(5,5,5,5);
                    imageView.setLayoutParams(layoutParams);
                    break;
            }
        }
        if (mPoints.getChildCount() >= 1){
            mPoints.getChildAt(0).setBackgroundResource(R.drawable.point_on);
        }
        mViewPager.setAdapter(new BannerAdapter());
        mViewPager.setCurrentItem(mImages.size() * 100000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int page) {
                page = page % mImages.size();
                int childCount = mPoints.getChildCount();
                for (int i = 0 ; i < childCount ; i++){
                    mPoints.getChildAt(i).setBackgroundResource(i == page ? R.drawable.point_on :R.drawable.point_off);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i){
                    case 1:
                        isHandlerReleased = true;
                        releaseHandler();
                        break;
                    case 2:
                        if (isHandlerReleased){
                            sendDelayMessage();
                            isHandlerReleased = false;
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array array = parameters.getArray(0);
                for (int i = 0; i < array.length(); i++) {
                    ZKImgView image = (ZKImgView) ((RelativeLayout) mImages.get(i)).getChildAt(0);
                    Glide.with(getContext()).load(ZKImgView.getUrl(array.getString(i))).into(image);
                }
                return null;
            }
        },"set");
    }

    private void createImg(Element element){
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        ZKImgView imgView = new ZKImgView(getContext());
        relativeLayout.addView(imgView);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = imgView.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        imgView.setLayoutParams(layoutParams);
        imgView.init(getZKDocument(),element);
        mImages.add(relativeLayout);
    }

    @Override
    public Object getResult() {
        return null;
    }

    //布局可见时开始轮播
    @Override
    public void onResume() {
        sendDelayMessage();
        super.onResume();
    }

    //不可见时停止轮播
    @Override
    public void onPause() {
        releaseHandler();
        super.onPause();
    }

    //在fragment或者activity销毁时执行清空handler的方法
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseHandler();
    }


    //轮播图适配器
    private class BannerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            ImageView imageView = mImages.get(position % mImages.size());
//            container.removeView(imageView);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mImages.get(position % mImages.size());
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent!=null)parent.removeView(view);
            container.addView(view);
            return view;
        }
    }

    //自定义消息处理机制,弱引用
    private static class BannerHandler extends Handler{
        WeakReference<Banner_1> mBanner;
        BannerHandler(Banner_1 banner_1){
            mBanner = new WeakReference<>(banner_1);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Banner_1 banner = mBanner.get();
            if (banner == null)return;
            banner.mViewPager.setCurrentItem(banner.mViewPager.getCurrentItem() + 1);
            banner.sendDelayMessage();
        }
    }

    //清楚handler
    private void releaseHandler(){
        mHandler.removeCallbacksAndMessages(null);
    }

    //定时发送轮播消息
    private void sendDelayMessage(){
        if (delayTime >= 0){
            mHandler.sendEmptyMessageDelayed(0,delayTime);
        }
    }


}
