package com.example.zkapp_map.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_map.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.zkapp.views.ZKImgView;

import java.util.ArrayList;
import java.util.List;

public class AdapterMapTaskProtogenesis extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<V8Object> list;

    public AdapterMapTaskProtogenesis(Context mContext, ArrayList<V8Object> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void setList(ArrayList<V8Object> list) {
        Log.e("fffffffff9000",list.size()+"");
        this.list.clear();
        Log.e("fffffffff90002222",list.size()+"");
        this.list.addAll(list);

        Log.e("fffffffff900033333",this.list.size()+"");
        
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_map_task_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).init(i);
        }
    }

    @Override
    public int getItemCount() {


        Log.e("ffffffffffffff", list.size() + "");
        return list == null ? 0 : list.size();
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
        private final LinearLayout layout0, layout1, layout2;

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
            V8Object v8Object = list.get(position);
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
                        // distance.setText("始发地距您约:" + onM2km(Double.valueOf(v8Object.getObject(key).get("distance")+"")));
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


}
