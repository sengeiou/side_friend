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
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

/**
 * Created by YF on 2018/9/17.
 */

@ZKAppAdapter("credit")
public class Credit extends ZKAdapter {
    public Credit(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new CreditHolder(mLayoutInflater.inflate(R.layout.item_credit,viewGroup,false));
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



    private class CreditHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView img;
        private TextView star,createTime,taskType,content;
        public CreditHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            star = itemView.findViewById(R.id.star);
            createTime = itemView.findViewById(R.id.createTime);
            taskType = itemView.findViewById(R.id.taskType);
            content = itemView.findViewById(R.id.content);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "star":
                        star.setText(object.getInteger(key)+"星");
                        break;
                    case "createTime":
                        break;
                    case "taskType":
                        taskType.setText(V8Utils.js2string(object.get(key)));
                        break;
                    case "content":
                        content.setText(V8Utils.js2string(object.get(key)));
                        break;
                }
            }
        }
    }
}
