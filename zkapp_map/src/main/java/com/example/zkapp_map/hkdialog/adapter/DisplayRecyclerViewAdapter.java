package com.example.zkapp_map.hkdialog.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.zankong.tool.zkapp.views.layout.Layout;

import java.util.List;

public class DisplayRecyclerViewAdapter extends RecyclerView.Adapter {

    public class DisplayRecyclerVH extends RecyclerView.ViewHolder{
        public TextView tvDisplayRecyclerViewItem;

        public DisplayRecyclerVH(@NonNull View itemView) {
            super(itemView);
            tvDisplayRecyclerViewItem = itemView.findViewById(R.id.tv_display_recycler_view_item);
        }
    }

    private List<String> list;

    public DisplayRecyclerViewAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_recycler_view_item,viewGroup,false);
        return new DisplayRecyclerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DisplayRecyclerVH vh = (DisplayRecyclerVH) viewHolder;
        vh.tvDisplayRecyclerViewItem.setText(list.get(i));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vh.tvDisplayRecyclerViewItem.setTextColor(Color.parseColor("#20D0F6"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
