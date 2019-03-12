package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.TypeJSONBean;

import java.util.List;

public class TypeGrid1Adapter extends BaseAdapter {
    private List<TypeJSONBean> list;
    private Context context;

    public TypeGrid1Adapter(List<TypeJSONBean> list, Context context) {
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

        View inflate = LayoutInflater.from(context).inflate(R.layout.type_gv_adapter_1,null);
        TextView tv = inflate.findViewById(R.id.tv);
        tv.setText(list.get(position).getStyleName());
        return inflate;
    }
}
