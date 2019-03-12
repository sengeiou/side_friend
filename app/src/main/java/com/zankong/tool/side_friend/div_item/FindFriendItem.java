package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

@ZKAppAdapter("findFriend")
public class FindFriendItem extends ZKAdapter {
    public FindFriendItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new FindFriendHolder(mLayoutInflater.inflate(R.layout.item_find_friend,viewGroup,false));
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

    public class FindFriendHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView nickname,serviceGrade,creditGrade,distance,tag1,tag2,tag3,release,chat,
                suchTag,reward,rewardCompany;
        private RoundedImageView img;
        private ImageView point3;
        public FindFriendHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            serviceGrade = itemView.findViewById(R.id.serviceGrade);
            creditGrade = itemView.findViewById(R.id.creditGrade);
            distance = itemView.findViewById(R.id.distance);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
            release = itemView.findViewById(R.id.release);
            chat = itemView.findViewById(R.id.chat);
            suchTag = itemView.findViewById(R.id.suchTag);
            reward = itemView.findViewById(R.id.reward);
            rewardCompany = itemView.findViewById(R.id.rewardCompany);
            img = itemView.findViewById(R.id.img);
            point3 = itemView.findViewById(R.id.point3);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "nickname":
                        nickname.setText(object.getString(key));
                        break;
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "distance":
                        int dis = object.getInteger(key);
                        float disF = dis;
                        if (dis >= 1000){
                            disF = dis*0.001f;
                        }
                        distance.setText(disF+"m");
                        break;
                    case "tags":
                        V8Array tags = (V8Array) object.getObject(key);
                        tag1.setVisibility(View.GONE);
                        tag2.setVisibility(View.GONE);
                        tag3.setVisibility(View.GONE);
                        for (int i = 0; i < tags.length(); i++) {
                            if (i == 0){
                                tag1.setVisibility(View.VISIBLE);
                                V8Object tag = tags.getObject(i);
                                tag1.setText(tag.getString("name"));
                                suchTag.setText(tag.getString("name"));
                                reward.setText(tag.get("money")+"");
                                rewardCompany.setText("/"+tag.getString("company"));
                            }else if (i == 1){
                                tag2.setVisibility(View.VISIBLE);
                                tag2.setText(tags.getObject(i).getString("name"));
                            }else if (i == 2){
                                tag3.setVisibility(View.VISIBLE);
                                tag3.setText(tags.getObject(i).getString("name"));
                            }
                        }
                        break;
                    case "serviceGrade":
                        serviceGrade.setText("接单评分"+object.get(key)+"分");
                        break;
                    case "creditGrade":
                        creditGrade.setText("(信用等级"+object.get(key)+"分)");
                        break;
                }
            }
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("chat")) {
                        invokeWithContext(getClickMap().get("chat"),FindFriendHolder.this);
                    }
                }
            });
            release.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("release")) {
                        invokeWithContext(getClickMap().get("release"),FindFriendHolder.this);
                    }
                }
            });
        }
    }
}
