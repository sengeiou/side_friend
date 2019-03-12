package com.zankong.tool.zkapp.document.fra;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.fragment.ZKFragment;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Created by YF on 2018/6/28.
 */

public class ZKFrameLayout extends FrameLayout {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private ArrayList<ZKFragment> mFragments;
    private ZKDocument mZKDocument;

    public ZKFrameLayout(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public ZKFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public ZKFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(ZKDocument zkDocument){
        mFragmentManager = zkDocument.getFragmentManager();
        mFragments = new ArrayList<>();
        mZKDocument = zkDocument;
    }

    public void fillData(Element fraElement){
        int index = 0;
        for (Element page : fraElement.elements()) {
            ZKFragment.ZKFragmentBuilder builder = new ZKFragment.ZKFragmentBuilder()
                    .setParent(mZKDocument)
                    .setIndex(index++);
            for (Attribute attribute : page.attributes()) {
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
            mFragments.add(builder.builder());
        }
        showFragment(0);
    }

    public void showFragment(int index){
        if (index >= mFragments.size() || index < 0)return;
        for(int i = 0 ; i < mFragments.size() ; i++){
            if (i == index){
                if (mFragments.get(i).isAdded()){
                    mFragmentManager.beginTransaction().show(mFragments.get(i)).commit();
                }else {
                    mFragmentManager.beginTransaction().add(this.getId(),mFragments.get(i)).commit();
                }
            }else {
                if (mFragments.get(i).isAdded()){
                    mFragmentManager.beginTransaction().hide(mFragments.get(i)).commit();
                }
            }
        }
    }
}
