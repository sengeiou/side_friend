package com.zankong.tool.side_friend.diy_view.credit_manage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.views.text.Text;

import java.util.List;

/**
 * @author Fsnzzz
 * @Created on 2018/11/28 0028 17:51
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {


    private Context mContext;
    private String[] strs;

    public TextAdapter(Context context,String[] strs){
        this.mContext = context;
        this.strs = strs;
    }

    public void setAdapterData(String[] strs){
        this.strs = strs;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.text_adater_layout, viewGroup, false);
        return new TextViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, int i) {
        if (textViewHolder instanceof TextViewHolder){
            textViewHolder.text_tv.setText(""+strs[i]);
        }
    }
    @Override
    public int getItemCount() {
        return strs == null ? 0 : strs.length;
    }
    public class TextViewHolder extends RecyclerView.ViewHolder{
        private final TextView text_tv;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            text_tv = (TextView)itemView.findViewById(R.id.text_tv);
        }
    }
}
