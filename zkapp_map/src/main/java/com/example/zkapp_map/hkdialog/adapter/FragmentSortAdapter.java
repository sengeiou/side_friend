package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zkapp_map.R;

import java.util.List;

public class FragmentSortAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private TextView tvSort;
    private ImageView ivSort;
    private int lastPosition;

    public FragmentSortAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setSeclection(int position) {
        lastPosition = position;
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
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sort_adapter,null);
        }
        tvSort = convertView.findViewById(R.id.tv_sort);
        ivSort = convertView.findViewById(R.id.iv_sort);
        ivSort.setVisibility(View.GONE);
        tvSort.setText(list.get(position));
        if (lastPosition == position){
            tvSort.setTextColor(Color.parseColor("#5c9afd"));
            ivSort.setVisibility(View.VISIBLE);
        }else {
            tvSort.setTextColor(Color.GRAY);
            ivSort.setVisibility(View.GONE);
        }
        return convertView;
    }
}
