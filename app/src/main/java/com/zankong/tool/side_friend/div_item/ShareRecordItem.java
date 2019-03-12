package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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

import org.dom4j.Element;

@ZKAppAdapter("share_record")
public class ShareRecordItem extends ZKAdapter {
    public ShareRecordItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        View inflate = mLayoutInflater.inflate(R.layout.share_record_item_layout, viewGroup, false);
        return new RecordViewHordler(inflate);
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof RecordViewHordler) {
            ((RecordViewHordler) viewHolder).init(position);
        }
    }


    private class RecordViewHordler extends RecyclerView.ViewHolder implements HolderInit {

        private final RoundedImageView img;
        private final TextView tv_nickname;
        private final TextView tv_profit;

        public RecordViewHordler(@NonNull View itemView) {
            super(itemView);
            tv_profit = itemView.findViewById(R.id.tv_profit);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            img = itemView.findViewById(R.id.img_left);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "nickname":
                        tv_nickname.setText(object.get(key)+"");
                        break;
                    case "img":
                        Glide.with(getContext())
                                .load(object.get(key)+"")
                                .into(img);
                        break;
                    case "profit":
                        tv_profit.setText(object.get(key)+"");
                        break;

                }
            }
        }


    }

}
    
            
