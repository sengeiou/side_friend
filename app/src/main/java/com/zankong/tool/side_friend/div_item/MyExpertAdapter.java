package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyExpertAdapter extends BaseAdapter{
    private ArrayList<Map<String,String>> list;
    private Context context;
    private String key;
    private TextView textView;

    public MyExpertAdapter(ArrayList<Map<String, String>> list, Context context,String key) {
        this.list = list;
        this.context = context;
        this.key = key;
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
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_popwin,null);
        }
        textView = convertView.findViewById(R.id.listview_popwind_tv);

        textView.setText(list.get(position).get(key));

        return convertView;
    }
}
