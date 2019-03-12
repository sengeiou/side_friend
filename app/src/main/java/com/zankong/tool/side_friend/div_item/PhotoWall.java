package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

import java.util.ArrayList;

@ZKAppAdapter("photoWall")
public class PhotoWall extends ZKAdapter {
    private final int isAdd = 1;
    private final int isPhoto = 2;
    private boolean isEdit = false;
    private ArrayList<Boolean> deleteList = new ArrayList<>();
    public PhotoWall(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        switch (i){
            case isAdd:
                holder = new AddHolder(mLayoutInflater.inflate(R.layout.item_photo_wall_add,viewGroup,false));
                break;
            case isPhoto:
                holder = new PhotoWallHolder(mLayoutInflater.inflate(R.layout.item_photo_wall,viewGroup,false));
                break;
        }
        return holder;
    }

    @Override
    protected int getViewType(int position) {
        V8Object object = mList.get(position);
        String type = object.getString("type");
        switch (type){
            case "image":
                return isPhoto;
            default:
                return isAdd;
        }
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }

    }

    private class PhotoWallHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView img,edit;
        public PhotoWallHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            edit = itemView.findViewById(R.id.edit);
        }

        @Override
        public void init(int position) {
            edit.setVisibility(isEdit?View.VISIBLE:View.GONE);
            if (isEdit){
                if (deleteList.get(position)){
                    edit.setImageResource(R.drawable.photo_wall_edit);
                }else {
                    edit.setImageResource(R.drawable.photo_wall_edit_null);
                }
            }
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "src":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                }
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEdit){
                        deleteList.set(position,!deleteList.get(position));
                        if (isEdit){
                            if (deleteList.get(position)){
                                edit.setImageResource(R.drawable.photo_wall_edit);
                            }else {
                                edit.setImageResource(R.drawable.photo_wall_edit_null);
                            }
                        }
                    }else {
                        if (getClickMap().containsKey("look")){
                            invokeWithContext(getClickMap().get("look"),PhotoWallHolder.this);
                        }
                    }
                }
            });
        }
    }

    private class AddHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView img;

        public AddHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void init(int position) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("add")){
                        Util.log("position:"+position);
                        Util.log("obj"+mList.get(position).getKeys().length);
                        invokeWithContext(getClickMap().get("add"),AddHolder.this);
                    }
                }
            });
            itemView.setVisibility(isEdit?View.GONE:View.VISIBLE);
        }
    }

    @Override
    protected void initV8this() {
        super.initV8this();
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array array = new V8Array(ZKToolApi.runtime);
                for (int i = 0; i < deleteList.size(); i++) {
                    if(deleteList.get(i)) {
                        array.push(mList.get(i).getInteger("docId"));
                    }
                }
                Util.log(V8Utils.js2string(array));
                return array;
            }
        },"getDeleteList");
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                isEdit = parameters.getBoolean(0);
                if(isEdit){
                    for (int i = 0; i < mList.size(); i++) {
                        deleteList.add(false);
                    }
                }else {
                    deleteList.clear();
                }
                notifyDataSetChanged();
                return null;
            }
        },"edit");
    }
}
