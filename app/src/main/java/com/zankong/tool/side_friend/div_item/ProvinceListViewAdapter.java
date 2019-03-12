package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.ProvinceBean;

import java.util.List;

public class ProvinceListViewAdapter extends BaseAdapter {

    private List<ProvinceBean> provinceBeanList;
    private Context context;
    private int lastPosition;

    public ProvinceListViewAdapter(List<ProvinceBean> provinceBeanList, Context context) {
        this.provinceBeanList = provinceBeanList;
        this.context = context;
    }
    public void setSeclection(int position) {
        lastPosition = position;
    }

    @Override
    public int getCount() {
        return provinceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.popwin_province_list_item, null);
        }
        TextView tvProvince =  convertView.findViewById(R.id.tv_province);
        LinearLayout ll = convertView.findViewById(R.id.ll);

        tvProvince.setText(provinceBeanList.get(position).getProvinceName());
        if (lastPosition == position){
            ll.setBackgroundColor(Color.WHITE);
            tvProvince.setTextColor(Color.parseColor("#5c9afd"));
        }else {
            ll.setBackgroundColor(Color.parseColor("#f2f2f2"));
            tvProvince.setTextColor(Color.parseColor("#7b7b7b"));
        }
        return convertView;
    }
}
