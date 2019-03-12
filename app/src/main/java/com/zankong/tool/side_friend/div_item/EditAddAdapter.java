package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.util.transformations.ZKCircleTransform;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

import java.util.HashMap;

@ZKAppAdapter("editAdd")
public class EditAddAdapter extends ZKAdapter {
    private final int IMAGE = 0;
    private final int AUDIO = 1;
    private final int VIDEO = 2;
    private final int BUTTON = 3;
    private RequestOptions options = new RequestOptions().skipMemoryCache(false).dontAnimate().transforms(
            new ZKCircleTransform(0,20,0)
    );

    private HashMap<String , Integer> typeMap = new HashMap<String, Integer>(){{
        put("image",IMAGE);
        put("audio",AUDIO);
        put("video",VIDEO);
        put("button",BUTTON);
    }};

    public EditAddAdapter(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i){
            case IMAGE:
                viewHolder = new ImageHolder(mLayoutInflater.inflate(R.layout.item_edit_add_image,viewGroup,false));
                break;
            case AUDIO:
                viewHolder = new AudioHolder(mLayoutInflater.inflate(R.layout.item_edit_add_audio,viewGroup,false));
                break;
            case VIDEO:
                viewHolder = new VideoHolder(mLayoutInflater.inflate(R.layout.item_edit_add_video,viewGroup,false));
                break;
            case BUTTON:
                viewHolder = new ButtonHolder(mLayoutInflater.inflate(R.layout.item_edit_add_button,viewGroup,false));
                break;
        }
        return viewHolder;
    }

    @Override
    protected int getViewType(int position) {
        return typeMap.get(mList.get(position).getString("type"));
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }
    }


    private class ImageHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView image;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            Util.log("ImageHolder----------1");
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            Util.log("ImageHolder----------1", V8Utils.js2string(object));
            for (String key : object.getKeys()) {
                switch (key) {
                    case "url":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).apply(options).into(image);
                        Util.log("ImageHolder----------1",object.getString(key));
                        break;
                }
            }
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) image.getLayoutParams();
            layoutParams.setMargins(Util.px2dip(position == 0 ?20:13),0,Util.px2dip(position == mList.size()-1?20:13),0);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.showT("点击弹大图");
                }
            });
        }
    }
    private class AudioHolder extends RecyclerView.ViewHolder {
        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private class VideoHolder extends RecyclerView.ViewHolder {
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private class ButtonHolder extends RecyclerView.ViewHolder {
        public ButtonHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
