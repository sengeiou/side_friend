package com.zankong.tool.zkapp.views.recycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zankong.tool.zkapp.util.Util;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by YF on 2018/7/10.
 */

public class RecycleBean {
    private RecyclerView.LayoutManager mLayoutManager;
    private int num;
    private String type;
    private String orientation;
    private boolean reverse;
    private Context mContext;
    private RecycleBean(Builder builder){
        this.num = builder.num;
        this.type = builder.type;
        this.orientation = builder.orientation;
        this.mContext = builder.mContext;
        this.reverse = builder.reverse;
    }
    public RecyclerView.LayoutManager getLayoutManager(){
        switch (type){
            case "grid":
                int gridOrientation;
                switch (orientation.toLowerCase()) {
                    case "horizontal":
                        gridOrientation = GridLayoutManager.HORIZONTAL;
                        break;
                    case "vertical":
                    default:
                        gridOrientation = GridLayoutManager.VERTICAL;
                }
                mLayoutManager = new GridLayoutManager(mContext,num,gridOrientation,reverse);
                break;
            case "stagger":
                int staggerOrientation;
                switch (orientation.toLowerCase()) {
                    case "horizontal":
                        staggerOrientation = StaggeredGridLayoutManager.HORIZONTAL;
                        break;
                    case "vertical":
                    default:
                        staggerOrientation = StaggeredGridLayoutManager.VERTICAL;
                }
                mLayoutManager = new StaggeredGridLayoutManager(num,staggerOrientation);
                ((StaggeredGridLayoutManager)mLayoutManager).setReverseLayout(reverse);
                break;
            case "list":
            default:
                int linearOrientation;
                switch (orientation.toLowerCase()) {
                    case "horizontal":
                        linearOrientation = LinearLayoutManager.HORIZONTAL;
                        break;
                    case "vertical":
                    default:
                        linearOrientation = LinearLayoutManager.VERTICAL;
                }
                mLayoutManager =  new LinearLayoutManager(mContext,linearOrientation,reverse);
        }
        return mLayoutManager;
    }


    public static class Builder{
        private int num = 1;
        private String type = "list";
        private String orientation = "vertical";
        private Context mContext;
        private boolean reverse = false;
        public Builder (Context context){
            mContext = context;
        }

        public Builder setNum(int num) {
            this.num = num;
            return this;
        }

        public Builder setType(@NonNull String type) {
            this.type = type;
            return this;
        }

        public Builder setOrientation(String orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setReverse(boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public RecycleBean builder(){
            return new RecycleBean(this);
        }
    }
}
