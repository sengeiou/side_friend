package com.example.zkapp_map.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_map.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.zkapp.views.ZKImgView;

import java.util.ArrayList;

public class AdapterMapTask2 extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<V8Object> mLists;
    //普通布局
    private final int TYPE_ITEM = 1;
    //脚布局
    private final int TYPE_FOOTER = 2;
    //当前加载状态，默认为加载完成
    private int loadState = 2;
    //正在加载
    public final int LOADING = 1;
    //加载完成
    public final int LOADING_COMPLETE = 2;
    //加载到底
    public final int LOADING_END = 3;
    public AdapterMapTask2(Context mContext, ArrayList<V8Object> list) {
        this.mContext = mContext;
        this.mLists = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM){
           View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_map_task_item, viewGroup, false);
           return new ViewHolder(view);
        }else if (i == TYPE_FOOTER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_refresh_footer, viewGroup, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder){
            ((ViewHolder) viewHolder).init(i);
        }else if (viewHolder instanceof FootViewHolder){
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            switch (loadState){
                case LOADING://正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;
                    default:
                        break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_nickName;
        private final TextView tv_credit;
        private final TextView tv_distance_top;
        private final TextView tv_credit2;
        private final TextView tv_task_type;
        private final TextView tv_addressA;
        private final TextView tv_distanceA2B;
        private final TextView tv_addressB;
        private final TextView tv_distanceB;
        private final TextView tv_money;
        private final LinearLayout layout_bargain;
        private final LinearLayout layout_apply;
        private final RoundedImageView img;
        private final LinearLayout layout0;

        private final RelativeLayout layout1, layout2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickName = itemView.findViewById(R.id.tv_nickname);
            tv_credit = itemView.findViewById(R.id.tv_credit);
            tv_distance_top = itemView.findViewById(R.id.tv_distance_top);
            tv_credit2 = itemView.findViewById(R.id.tv_credit2);
            tv_task_type = itemView.findViewById(R.id.tv_task_type);
            tv_addressA = itemView.findViewById(R.id.tv_addressA);
            tv_distanceA2B = itemView.findViewById(R.id.tv_distanceA2B);
            tv_addressB = itemView.findViewById(R.id.tv_addressB);
            tv_distanceB = itemView.findViewById(R.id.tv_distanceB);
            tv_money = itemView.findViewById(R.id.tv_money);
            layout_bargain = itemView.findViewById(R.id.layout_bargain);
            layout_apply = itemView.findViewById(R.id.layout_apply);
            img = itemView.findViewById(R.id.img_left);
            layout0 = itemView.findViewById(R.id.layout0);
            layout1 = itemView.findViewById(R.id.layout1);
            layout2 = itemView.findViewById(R.id.layout2);

        }

        public void init(int position) {
            V8Object v8Object = mLists.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        String imgs = v8Object.getString(key);
                        Glide.with(mContext)
                                .load(ZKImgView.getUrl(v8Object.getString(key)))
                                .into(img);
                        break;
                    case "nickname":
                        tv_nickName.setText(v8Object.getString(key) + "");
                        break;
                    case "addressA":
                        layout0.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.VISIBLE);
                        // end_danslayout.setVisibility(View.VISIBLE);
                         tv_distance_top.setText("始发地距您约:" + onM2km(Double.valueOf(v8Object.getObject(key).get("distance")+"")));
                        tv_addressA.setText("始发地:" + v8Object.getObject(key).getString("name") + "");
                        break;
                    case "styleName":
                    case "typeName":
                        tv_task_type.setText("{" + v8Object.getString("styleName") + "}" + v8Object.getString("typeName"));
                        break;
                    case "distanceA2B":
                        tv_distanceA2B.setText("距目的地约:" + onM2km(Double.valueOf(v8Object.get(key) + "")));
                        break;
                    case "addressB":
                        layout2.setVisibility(View.VISIBLE);
                        //     end_danslayout2.setVisibility(View.VISIBLE);
                        tv_addressB.setText("目的地:" + v8Object.getObject(key).getString("name"));
                        tv_distanceB.setText("距您约:" + onM2km(Double.valueOf(v8Object.getObject(key).get("distance") + "")));
                        break;
                    case "reward":
                        tv_money.setText(v8Object.getString(key));
                        break;
                    case "credit":
                        tv_credit.setText("信用等级"+v8Object.get(key)+"");
                        break;
                }
            }
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(mClickMap.containsKey("img")){
//                        invokeWithContext(mClickMap.get("img"),TaskRun.this);
//                    }
//                }
//            });
        }


        @SuppressLint("DefaultLocale")
        public String onM2km(double val) {
            if (val > 1000) {
                String format = String.format("%.1f", (val / 1000));
                return format + "km";
            } else
                return (int) val + "m";
        }
    }


    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
