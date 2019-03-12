package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zkapp_map.bean.TypesNameBean;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.domain.TypeJSONBean;

import java.util.List;

public class TypeGrid2Adapter extends BaseAdapter {
    private List<TypesNameBean> list;
    private Context context;
    private  List<TypeJSONBean.TypesBean> types;
    private TextView tv;

    public TypeGrid2Adapter(List<TypeJSONBean.TypesBean> list, Context context) {
        this.types = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.type_gv_adapter_2,null);
        tv = inflate.findViewById(R.id.tv);
        types.get(position).getName();
        tv.setText(types.get(position).getName());
        return inflate;
    }
}
