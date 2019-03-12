package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.TypesNameBean;

import java.util.List;

public class GVAdapter1 extends BaseAdapter {
    private List<TypesNameBean> list;
    private Context context;

    public GVAdapter1(List<TypesNameBean> list, Context context) {
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

//        for (int b=0;b<list.size();b++){
//            Log.e("list1",list.get(b));
//        }
        View inflate = LayoutInflater.from(context).inflate(R.layout.gv_adapter_1,null);
        TextView tv = inflate.findViewById(R.id.tv);
        tv.setText(list.get(position).getStyleName());
        return inflate;
    }
}
