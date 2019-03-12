package com.zankong.tool.zkapp.document.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.item.ZKAdapter;

/**
 * Created by YF on 2018/6/28.
 */

public class ZKRecycleView extends RecyclerView {
    private View mFooterView;

    public ZKRecycleView(@NonNull Context context) {
        super(context);
    }

    public ZKRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(){
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.item_footer_refresh_view,this,false);
    }


    private boolean isFull = false;
    public void setFull(boolean full){
        isFull = full;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = isFull ? MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST):heightSpec;
        super.onMeasure(widthSpec,expandSpec);
    }

    public void addFooterView(){
        assert getAdapter() != null;
        if (mFooterView != null && mFooterView.getParent() == null)
            ((ZKAdapter) getAdapter()).addFooterView(mFooterView);
        getAdapter().notifyDataSetChanged();
    }

    public void removeFooterView(){
        assert getAdapter() != null;
        if (mFooterView != null &&mFooterView.getParent() != null){
            ((ZKAdapter) getAdapter()).removeFooterView(mFooterView);
            getAdapter().notifyDataSetChanged();
        }
    }
}
