package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

@ZKAppAdapter("ApplyFriend")
public class ApplyFriend extends ZKAdapter {
    public ApplyFriend(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new appliesHolder(mLayoutInflater.inflate(R.layout.item_apply_friend,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class appliesHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView nickname,status;
        private ImageView img;
        public appliesHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String s : object.getKeys()) {
                switch (s) {
                    case "nickname":
                        nickname.setText(object.getString(s));
                        break;
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(s))).into(img);
                        break;
                    case "status":
                        status.setText(object.get(s)+"");
                        break;
                }
            }
        }
    }
}
