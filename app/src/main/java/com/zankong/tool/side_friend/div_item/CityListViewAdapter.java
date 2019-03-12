package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.CityBean;
import com.zankong.tool.side_friend.domain.ProvinceBean;

import java.util.List;

public class CityListViewAdapter extends BaseAdapter {

    private List<CityBean> cityBeans;
    private Context context;
    private int lastPosition;

    public CityListViewAdapter(List<CityBean> cityBeans, Context context) {
        this.cityBeans = cityBeans;
        this.context = context;
    }

    public void setSeclection(int position) {
        lastPosition = position;
    }

    @Override
    public int getCount() {
        return cityBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return cityBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.popwin_city_list_item,null);
        }
        TextView tvCity = convertView.findViewById(R.id.tv_city);
        tvCity.setText(cityBeans.get(position).getCityName());
        if (lastPosition == position){
            tvCity.setTextColor(Color.parseColor("#5c9afd"));
        }else {
            tvCity.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}
