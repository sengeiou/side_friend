package com.example.zkapp_map.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkapp_map.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fsnzzz
 * @date 2019/1/18
 * @month 01
 * @package com.example.zkapp_map.adapter
 */
public class TestAdapter extends RecyclerView.Adapter {
    
    private Context mContext;
    private ArrayList<String> list;
    public TestAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        this.list = (ArrayList<String>) list.clone();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.test_title_item, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof ViewHolder){
            ((ViewHolder) viewHolder).tv_title.setText(list.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
    
    
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
