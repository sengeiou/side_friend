package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;

import java.util.ArrayList;

/**
 * Created by YF on 2018/8/7.
 */

public class Step extends RecyclerView.Adapter {

    private ArrayList<V8Object> mList;
    private Context mContext;



    public Step(Context context,ArrayList<V8Object> v8Array){
        mContext = context;
        mList = v8Array;
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
    private void itemClick(View view,int position){
        if (mOnItemClickListener != null)mOnItemClickListener.onItemClick(view,position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StepHolder(LayoutInflater.from(mContext).inflate(R.layout.item_process_step, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof StepHolder) {
            ((StepHolder) viewHolder).init(i);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class StepHolder extends RecyclerView.ViewHolder{

        private TextView title,status,time;
        private CardView mCardView;

        public StepHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            mCardView = itemView.findViewById(R.id.card);
        }

        private void init(int position){
            V8Object object = mList.get(position);
            status.setText("状态:待办");
            boolean isEnd = false;
            for (String key : object.getKeys()) {
                switch (key){
                    case "title":
                        title.setText("标题:"+object.getString(key));
                        break;
                    case "status":
                        String statusText = "未知";
                        String statusColor = "#ffffff";
                        switch (object.getString(key)) {
                            case "finish":
                                statusText = "结束";
                                statusColor = "#d4d4d4";
                                break;
                            case "await":
                                statusText = "待办";
                                statusColor = "#00ff00";
                                break;
                            case "cancel":
                                statusText = "取消";
                                statusColor = "#ff0000";
                                break;
                            case "timeout":
                                statusText = "超时";
                                statusColor = "#FFDD00";
                                break;
                        }
                        mCardView.setCardBackgroundColor(Color.parseColor(statusColor));
                        status.setText(statusText);
                        break;
                    case "startTime":
                        if (!isEnd) time.setText("触发时间:"+object.getString(key));
                        break;
                    case "endTime":
                        String endTime = object.getString(key);
                        if (!"".equals(endTime)){
                            isEnd = true;
                            time.setText("结束时间:"+endTime);
                        }
                        break;
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick(view,position);
                }
            });
        }
    }
}
