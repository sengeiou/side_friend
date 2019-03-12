package com.zankong.tool.side_friend.div_item;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

@ZKAppAdapter("recruit")
public class RecruitItem extends ZKAdapter {
    public RecruitItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new RecruitHolder(mLayoutInflater.inflate(R.layout.item_recruit,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class RecruitHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RoundedImageView img;
        private TextView nickname,userCreditGrade,userCharmGrade,recruitNum,typeName,styleName,reward,
                workAddressName,distance,apply,bargaining,line;
        public RecruitHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            nickname = itemView.findViewById(R.id.nickname);
            userCreditGrade = itemView.findViewById(R.id.userCreditGrade);
            userCharmGrade = itemView.findViewById(R.id.userCharmGrade);
            recruitNum = itemView.findViewById(R.id.recruitNum);
            typeName = itemView.findViewById(R.id.typeName);
            styleName = itemView.findViewById(R.id.styleName);
            reward = itemView.findViewById(R.id.reward);
            workAddressName = itemView.findViewById(R.id.workAddressName);
            distance = itemView.findViewById(R.id.distance);
            apply = itemView.findViewById(R.id.apply);
            bargaining = itemView.findViewById(R.id.bargaining);
            line = itemView.findViewById(R.id.line);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "nickname":
                        nickname.setText(object.getString(key));
                        break;
                    case "userCreditGrade":
                        userCreditGrade.setText("(信用等级"+object.get(key)+"分)");
                        break;
                    case "userCharmGrade":
                        userCharmGrade.setText("下单评分"+object.get(key)+"分");
                        break;
                    case "recruitNum":
                        recruitNum.setText(object.get(key)+"");
                        break;
                    case "reward":
                        reward.setText("¥"+object.get(key));
                        break;
                    case "workAddress":
                        V8Object address = object.getObject(key);
                        workAddressName.setText(address.getString("name"));
                        break;
                    case "distance":
                        int dis = object.getInteger(key);
                        float disF = dis;
                        if (dis >= 1000){
                            disF = dis*0.001f;
                        }
                        distance.setText(disF+"m");
                        break;
                    case "typeName":
                        typeName.setText(object.getString(key));
                        break;
                    case "styleName":
                        styleName.setText(object.getString(key));
                        break;
                    case "bargaining":
                        GradientDrawable background = (GradientDrawable) bargaining.getBackground();
                        background.setColor(Color.parseColor(object.getBoolean(key)?"#e5effb":"#cccccc"));
                        bargaining.setBackground(background);
                        bargaining.setTextColor(Color.parseColor(object.getBoolean(key)?"#5c9afd":"#ffffff"));
                        break;
                }
            }
            line.setVisibility(position==0?View.GONE:View.VISIBLE);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("apply")) {
                        invokeWithContext(getClickMap().get("apply"),RecruitHolder.this);
                    }
                }
            });
            bargaining.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("bargaining")) {
                        invokeWithContext(getClickMap().get("bargaining"),RecruitHolder.this);
                    }
                }
            });
        }
    }
}
