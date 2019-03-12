package com.zankong.tool.side_friend.div_item;

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

/**
 * Created by YF on 2018/7/31.
 */

@ZKAppAdapter("recruitApply")
public class RecruitApplyItem extends ZKAdapter{
    public RecruitApplyItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new RecruitApplyHolder(mLayoutInflater.inflate(R.layout.item_recruit_apply,viewGroup,false));
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

    private class RecruitApplyHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RoundedImageView img;
        private TextView line,nickname,credit;
        public RecruitApplyHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            line = itemView.findViewById(R.id.line);
            nickname = itemView.findViewById(R.id.nickname);
            credit = itemView.findViewById(R.id.credit);
        }

        @Override
        public void init(int position) {
            line.setVisibility(position == 0?View.GONE:View.VISIBLE);
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "nickname":
                        nickname.setText(object.getString(key));
                        break;
                    case "credit":
                        credit.setText(object.get(key)+"分");
                        break;
                }
            }
        }
    }
}
