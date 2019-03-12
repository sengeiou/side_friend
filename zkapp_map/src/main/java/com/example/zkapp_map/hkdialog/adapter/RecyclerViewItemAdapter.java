package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zkapp_map.R;

import java.util.List;

public class RecyclerViewItemAdapter extends BaseAdapter {

    List<RecyclerView> list;
    Context context;

    public RecyclerViewItemAdapter(List<RecyclerView> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_adapter, null);
        RecyclerView rv = inflate.findViewById(R.id.rv_recycler_view_item);
        rv = list.get(position);
        return inflate;
    }
}
