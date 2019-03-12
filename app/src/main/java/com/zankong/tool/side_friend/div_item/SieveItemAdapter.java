package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.SieveListBean;

import java.util.List;

public class SieveItemAdapter extends BaseAdapter{

    private List<SieveListBean> listBeans;
    private Context context;
    private int lastPosition;

    public SieveItemAdapter(List<SieveListBean> listBeans,Context context) {
        this.listBeans = listBeans;
        this.context = context;
    }
    public void setSeclection(int position) {
        lastPosition = position;
    }


    @Override
    public int getCount() {
        return listBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return listBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.type_sieve_item,null);
        }
        TextView tv = convertView.findViewById(R.id.tv_sieve);
        tv.setText(listBeans.get(position).getContent());

        if (lastPosition == position) {//最后选择的位置
            tv.setBackgroundResource(R.drawable.btn_bg_blue);
        } else {
            tv.setBackgroundResource(R.drawable.btn_bg_black);
        }
        return convertView;
    }
}
