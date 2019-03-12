package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.credit_manage.TextAdapter;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 17:35
 */
@ZKAppAdapter("credititem")
public class CreditItemNew extends ZKAdapter {
    private TextAdapter textAdapter;
    private HashMap<String,String> list;
    public CreditItemNew(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }
    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHorlder(mLayoutInflater.inflate(R.layout.credit_item_new, viewGroup, false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }
    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHorlder) {
            ((ViewHorlder) viewHolder).init(position);
        }
    }

    public class ViewHorlder extends RecyclerView.ViewHolder {
        private final RoundedImageView right_img;
        private final RelativeLayout layout_01;
        private final TextView time_tv;
        private final TextView name;
        private final TextView reward;
        private final TextView gratuity;
        private final TextView type;
        private final TextView time_start;
        private final TextView reason_tv;
        private final RecyclerView rv_01;
        public ViewHorlder(@NonNull View itemView) {
            super(itemView);
            layout_01 = (RelativeLayout) itemView.findViewById(R.id.layout_01);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            name = (TextView) itemView.findViewById(R.id.name);
            reward = (TextView) itemView.findViewById(R.id.reward);
            gratuity = (TextView) itemView.findViewById(R.id.gratuity);
            type = (TextView) itemView.findViewById(R.id.type);
            time_start = (TextView) itemView.findViewById(R.id.time_start);
            reason_tv = (TextView) itemView.findViewById(R.id.reason_tv);
            rv_01 = (RecyclerView) itemView.findViewById(R.id.rv_01);

            right_img = (RoundedImageView) itemView.findViewById(R.id.right_img);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            rv_01.setLayoutManager(gridLayoutManager);
        }

        private void init(int position) {
            V8Object v8Object = mList.get(position);
            gratuity.setVisibility(View.GONE);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(right_img);
                        break;
                    case "nickname":
                        name.setText(v8Object.getString(key));
                        break;
                    case "title":
                        type.setText(v8Object.getString(key));
                        break;
                    case "content":
                        reason_tv.setText(v8Object.getString(key));
                        break;
                    case "gratuity":
                        float v = Float.parseFloat(v8Object.get(key) + "");
                        if (v != 0){
                            gratuity.setText("打赏金额:" + v);
                            gratuity.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "reward":
                        reward.setText("服务费:" + v8Object.getString(key));
                        break;
                    case "additionalMessage":
                        try {
                            V8Object object = v8Object.getObject(key);
                            String text = object.getString("text");
                            String[] split = text.split(",");
                            textAdapter = new TextAdapter(getContext(), split);
                            rv_01.setAdapter(textAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        break;
                    case "createTime":
                        time_start.setText(v8Object.getString(key));
                        String s = addStringTime(v8Object.getString(key));
                        time_tv.setText(s);
                        layout_01.setVisibility("".equals(s)?View.GONE:View.VISIBLE);
                        break;
                }
            }
        }
        private String addStringTime(String time) {
            if (list == null) {
                list = new HashMap<>();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String currentFormat = simpleDateFormat.format(date);
            String createdAt = time.split(" ")[0];
            if (list.containsKey(createdAt)){
                if (!time.equals(list.get(createdAt)))return "";
            }else {
                list.put(createdAt,time);
            }
            return currentFormat.equals(createdAt)?"今天":createdAt;
        }
    }
}
