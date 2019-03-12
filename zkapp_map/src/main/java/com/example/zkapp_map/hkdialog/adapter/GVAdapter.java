package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zkapp_map.R;

import java.util.List;

public class GVAdapter extends BaseAdapter{

    private List<String> lis;
    private Context context;

    public GVAdapter(List<String> lis,Context context) {
        this.lis = lis;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lis.size();
    }

    @Override
    public Object getItem(int position) {
        return lis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gv_item,null);
        }
        TextView tv = convertView.findViewById(R.id.tv_gv_item);
        tv.setText(lis.get(position));
        return convertView;
    }
}
