package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zkapp_map.R;

import java.util.List;

public class FragmentSieveGridViewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private int lastPosition;

    public FragmentSieveGridViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void selectPosition(int position){
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
        convertView = LayoutInflater.from(context).inflate(R.layout.fragment_sieve_item,null);
        TextView tv = convertView.findViewById(R.id.tv_sieve);
        tv.setText(list.get(position));
        if (lastPosition == position){
            tv.setTextColor(Color.parseColor("#5c9afd"));
            tv.setBackgroundResource(R.drawable.btn_bg_blue);
        }else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundResource(R.drawable.btn_bg_black);
        }
        return convertView;
    }
}
