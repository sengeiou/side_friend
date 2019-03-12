package com.zankong.tool.zkapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/1 0001.
 */

public abstract class  MyBaseAdapter<T> extends BaseAdapter {
    public List<T> datas;
    private LayoutInflater inflater;
    public int lastI;
    public MyBaseAdapter(Context context) {
        this.datas = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        lastI=-1;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        /*if (datas.size() == 0) {
            return 1;
        }*/
        return datas.size();
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void addAll(List<T> dd) {
        datas.addAll(dd);
        lastI=-1;
        notifyDataSetChanged();
    }


    public void refreshAll(List<T> dd) {
        datas.clear();
        datas.addAll(dd);
        lastI=-1;
        notifyDataSetChanged();
    }
    public void clear() {
        datas.clear();
        lastI=-1;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        lastI=-1;
    }
}
