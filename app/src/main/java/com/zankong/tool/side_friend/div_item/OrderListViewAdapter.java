package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.ProvinceBean;

import java.util.List;

public class OrderListViewAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private int lastPosition;

    public OrderListViewAdapter(List<String> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.popwin_order_list_item,null);
        }
        TextView textView =  convertView.findViewById(R.id.tv_order_list_item);
        ImageView iv = convertView.findViewById(R.id.iv_order_list_item);
        textView.setText(list.get(position));
        iv.setVisibility(View.GONE);
        if (lastPosition == position){
            textView.setTextColor(Color.parseColor("#5c9afd"));
            iv.setVisibility(View.VISIBLE);
        }else {
            textView.setTextColor(Color.BLACK);
            iv.setVisibility(View.GONE);
        }
        return convertView;
    }
}
