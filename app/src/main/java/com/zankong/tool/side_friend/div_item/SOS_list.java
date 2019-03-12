package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by YF on 2018/9/17.
 */

@ZKAppAdapter("sos_list")
public class SOS_list extends ZKAdapter {
    private final int TYPE_ORANGE = 0,
                        TYPE_RED = 1;
    private HashMap<String ,String> mClickMap;
    private HashMap<String,Integer> mType = new HashMap<String,Integer>(){{
        put("orange", TYPE_ORANGE);
        put("red", TYPE_RED);
    }};
    private HashMap<String,String> list;


    public SOS_list(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        mClickMap = new HashMap<>();
        for (Attribute attribute : element.element("sos_list").attributes()) {
            mClickMap.put(attribute.getName(),attribute.getValue());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        switch (i){
            case TYPE_ORANGE:
                holder = new SOSOrangeHolder(mLayoutInflater.inflate(R.layout.item_sos_orange,viewGroup,false));
                break;
            case TYPE_RED:
                holder = new SOSRedHolder(mLayoutInflater.inflate(R.layout.item_sos_red,viewGroup,false));
                break;
        }
        return holder;
    }

    @Override
    protected int getViewType(int position) {
        return mType.get(mList.get(position).getString("type"));
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class SOSOrangeHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView description,handle,contact,contactPhone,createdAt,line;
        private ImageView image1,image2,image3,image4,img;
        private LinearLayout images,buttons;
        public SOSOrangeHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            line = itemView.findViewById(R.id.line);
            contact = itemView.findViewById(R.id.contact);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            createdAt = itemView.findViewById(R.id.createdAt);
            handle = itemView.findViewById(R.id.handle);
            img = itemView.findViewById(R.id.img);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            images = itemView.findViewById(R.id.images);
            buttons = itemView.findViewById(R.id.buttons);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
            images.setVisibility(View.GONE);
            for (String key : object.getKeys()) {
                switch (key){
                    case "description":
                        V8Object object1 = object.getObject(key);
                        for (String s : object1.getKeys()) {
                            switch (s) {
                                case "text":
                                    
                                    description.setText(object1.getString(s));
                                    if ("".equals(object1.getString(s)) || object1.getString(s) == null){
                                        description.setVisibility(View.GONE);
                                    }
                                    break;
                                case "image":
                                    V8Array array = object1.getArray(s);
                                    if (array.length() > 0){
                                        images.setVisibility(View.VISIBLE);
                                        
                                    }
                                    for (int i = 0; i < array.length(); i++) {
                                        if (i == 0){
                                            image1.setVisibility(View.VISIBLE);
                                            Glide.with(getContext()).load(ZKImgView.getUrl(array.getString(i))).into(image1);
                                        }else if (i == 1){
                                            image2.setVisibility(View.VISIBLE);
                                            Glide.with(getContext()).load(ZKImgView.getUrl(array.getString(i))).into(image2);
                                        }else if ( i == 2){
                                            image3.setVisibility(View.VISIBLE);
                                            Glide.with(getContext()).load(ZKImgView.getUrl(array.getString(i))).into(image3);
                                        }else if(i == 3){
                                            image4.setVisibility(View.VISIBLE);
                                            Glide.with(getContext()).load(ZKImgView.getUrl(array.getString(i))).into(image4);
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                    case "isMy":
                        Util.log("isMy",object.getBoolean(key)+"");
                        buttons.setVisibility(object.getBoolean(key)?View.VISIBLE:View.GONE);
                        line.setVisibility(object.getBoolean(key)?View.VISIBLE:View.GONE);
                        break;
                    case "isCare":
//                        if(object.getBoolean(key)){
//                            itemView.setBackgroundColor(Color.parseColor("#CFFF40"));
//                        }
                        break;
                    case "status":
                        if ("released".equals(object.getString(key))) {
                            handle.setVisibility(View.VISIBLE);
                        }else {
                            handle.setVisibility(View.GONE);
                        }
                        break;
                    case "contact":
                        contact.setText(object.getString(key));
                        break;
                    case "contactPhone":
                        contactPhone.setText("联系方式: "+object.getString(key));
                        break;
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "createdAt":
                        String s = addStringTime(object.getString(key));
                        createdAt.setText(s);
                        createdAt.setVisibility("".equals(s)?View.GONE:View.VISIBLE);
                        break;
                }
            }
            handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("released".equals(object.getString("status"))){
                        if(mClickMap.containsKey("handle")){
                            invokeWithContext(mClickMap.get("handle"),SOSOrangeHolder.this);
                        }
                    }
                }
            });
        }
    }
    private class SOSRedHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView handle,contact,contactPhone,createdAt,line,address;
        private ImageView img;
        private LinearLayout buttons;
        public SOSRedHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            line = itemView.findViewById(R.id.line);
            contact = itemView.findViewById(R.id.contact);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            createdAt = itemView.findViewById(R.id.createdAt);
            handle = itemView.findViewById(R.id.handle);
            img = itemView.findViewById(R.id.img);
            buttons = itemView.findViewById(R.id.buttons);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key){
                    case "contact":
                        contact.setText("联系人:"+object.getString(key));
                        break;
                    case "contactPhone":
                        contactPhone.setText("联系电话:"+object.getString(key));
                        break;
                    case "address":
                        address.setText("事发地点:"+object.getString(key));
                        break;
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "isCare":
//                        if(object.getBoolean(key)){
//                            itemView.setBackgroundColor(Color.parseColor("#CFFF40"));
//                        }
                        break;
                    case "isMy":
                        Util.log("isMy",object.getBoolean(key)+"");
                        buttons.setVisibility(object.getBoolean(key)?View.VISIBLE:View.GONE);
                        line.setVisibility(object.getBoolean(key)?View.VISIBLE:View.GONE);
                        break;
                    case "createdAt":
                        String s = addStringTime(object.getString(key));
                        createdAt.setText(s);
                        createdAt.setVisibility("".equals(s)?View.GONE:View.VISIBLE);
                        break;
                }
            }
            handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("released".equals(object.getString("status"))){
                        if(mClickMap.containsKey("handle")){
                            invokeWithContext(mClickMap.get("handle"),SOSRedHolder.this);
                        }
                    }
                }
            });
        }
    }

    private String addStringTime(String time) {
        if (list == null) {
            list = new HashMap<>();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String currentFormat = simpleDateFormat.format(date);
        String createdAt = time.split(" ")[0];
        if (list.containsKey(createdAt)){
            if (!time.equals(list.get(createdAt)))return "";
        }else {
            list.put(createdAt,time);
        }
        return currentFormat.equals(createdAt)?"今天":createdAt;
    }
}
