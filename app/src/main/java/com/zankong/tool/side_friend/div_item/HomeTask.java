package com.zankong.tool.side_friend.div_item;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/9/14.
 */
@ZKAppAdapter("homeTask")
public class HomeTask extends ZKAdapter {
    private HashMap<String,String> mClickMap = new HashMap<>();

    public HomeTask(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        for (Attribute attribute : element.elements().get(0).attributes()) {
            mClickMap.put(attribute.getName(),attribute.getValue());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new TaskRun(mLayoutInflater.inflate(R.layout.item_task_home, viewGroup, false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof TaskRun) {
            ((TaskRun) viewHolder).init(position);
            viewHolder.setIsRecyclable(false);
        }
    }

    private class TaskRun extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nickname, credit, distance, type, bargaining,distanceB, addressA, addressB, distanceA2B, tv_requirement;
        private final LinearLayout layout1,end_danslayout,end_danslayout2,layout2,layout0;

        public TaskRun(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            nickname = itemView.findViewById(R.id.nickname);
            credit = itemView.findViewById(R.id.credit);
            distance = itemView.findViewById(R.id.distance);
            type = itemView.findViewById(R.id.type);
            bargaining = itemView.findViewById(R.id.bargaining);
            addressA = itemView.findViewById(R.id.addressA);
            addressB = itemView.findViewById(R.id.addressB);
            distanceB = itemView.findViewById(R.id.distanceB);
            distanceA2B = itemView.findViewById(R.id.distanceA2B);
            tv_requirement = itemView.findViewById(R.id.tv_requirement);
            layout1 = itemView.findViewById(R.id.layout1);
            layout2 = itemView.findViewById(R.id.layout2);
            end_danslayout = itemView.findViewById(R.id.end_danslayout);
            end_danslayout2 = itemView.findViewById(R.id.end_danslayout2);
            layout0 = itemView.findViewById(R.id.layout0);


        }
        private void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(img);
                        break;
                    case "nickname":
                        nickname.setText(v8Object.getString(key)+"");
                        break;
                    case "addressA":
                        layout0.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.VISIBLE);
                        end_danslayout.setVisibility(View.VISIBLE);
                        distance.setText("始发地距您约:" + onM2km(Double.valueOf(v8Object.getObject(key).get("distance")+"")));
                        addressA.setText("始发地:"+v8Object.getObject(key).getString("name")+"");
                        break;
                    case "styleName":
                    case "typeName":
                        type.setText("{" + v8Object.getString("styleName") + "}" + v8Object.getString("typeName"));
                        break;
                    case "distanceA2B":
                        distanceA2B.setText("距目的地约:"+onM2km(Double.valueOf(v8Object.get(key)+"")));
                        break;
                    case "addressB":
                        layout2.setVisibility(View.VISIBLE);
                        end_danslayout2.setVisibility(View.VISIBLE);
                        addressB.setText("目的地:"+v8Object.getObject(key).getString("name"));
                        distanceB.setText("距您约:"+onM2km(Double.valueOf(v8Object.getObject(key).get("distance")+"")));
                        break;
                    case "reward":
                        bargaining.setText("悬赏金:"+v8Object.getString(key));
                        break;
                }
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("img")){
                        invokeWithContext(mClickMap.get("img"),TaskRun.this);
                    }
                }
            });
        }
        @SuppressLint("DefaultLocale")
        public String onM2km(double val){
            if (val > 1000){
                String format = String.format("%.1f", (val / 1000));
                return format+"km";
            }
            else
                return (int)val+"m";
        }
    }




}
