package com.zankong.tool.side_friend.test_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKViews;

import org.dom4j.Element;

@ZKAppAdapter("list")
public class Item1 extends ZKAdapter {
    public Item1(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new TestHolder(mLayoutInflater.inflate(R.layout.item_test,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class TestHolder extends RecyclerView.ViewHolder implements HolderInit{
        private TextView name;
        private ImageView image;
        public TestHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_test_name);
            image = itemView.findViewById(R.id.item_test_iv);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()){
                switch (key){
                    case "name":
                        name.setText(object.getString(key));
                        break;
                    case "image":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(image);
                        break;
                }
            }
        }
    }
}
