package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.SieveBean;

import java.util.List;

public class FragmentSieveAdapter extends RecyclerView.Adapter {

    private Context context;

    public class SieveVH extends RecyclerView.ViewHolder{
        private final TextView tvItem;
        private final RecyclerView itemRecyclerView;
        public SieveVH(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_recycler_view_item);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private List<SieveBean> list;

    public FragmentSieveAdapter(List<SieveBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item,viewGroup,false);
        return new SieveVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SieveVH vh = (SieveVH) viewHolder;

        vh.tvItem.setText(list.get(i).getName());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        gridLayoutManager.setOrientation(OrientationHelper. VERTICAL);
        vh.itemRecyclerView.setLayoutManager(gridLayoutManager);
        vh.itemRecyclerView.setAdapter(new FragmentSieveItemAdapter(list.get(i).getListBeans()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
