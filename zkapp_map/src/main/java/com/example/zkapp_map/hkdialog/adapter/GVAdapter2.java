package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.TypesNameBean;

import java.util.List;

public class GVAdapter2 extends BaseAdapter {
    private List<TypesNameBean> list;
    private Context context;
    private int listPosition;
    private final List<JSONBean.TypesBean> typesBeans;
    private TextView tv;

    public GVAdapter2(List<TypesNameBean> list, Context context, int listPosition) {
        this.list = list;
        this.context = context;
        this.listPosition = listPosition;
        typesBeans = list.get(listPosition).getTypesBeans();
    }

    @Override
    public int getCount() {
        return typesBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return typesBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.gv_adapter_2,null);
        tv = inflate.findViewById(R.id.tv);
        typesBeans.get(position).getName();
        tv.setText(typesBeans.get(position).getName());
        return inflate;
    }
}
