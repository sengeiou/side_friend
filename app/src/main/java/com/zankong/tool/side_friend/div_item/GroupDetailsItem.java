package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/11/20 0020 18:45
 */
@ZKAppAdapter("groupdetails")
public class GroupDetailsItem extends ZKAdapter {
    public GroupDetailsItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }
    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.group_item, viewGroup, false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof MyViewHolder) {
                if (position == mList.size()-1){
                    ((MyViewHolder) viewHolder).img.setImageResource(R.drawable.boy);
                }else {
                    ((MyViewHolder) viewHolder).init(position);
                    viewHolder.setIsRecyclable(false);
                }
        }
    }



    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
        }
        private void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(img);
                        break;

                }
            }
        }

    }

}
