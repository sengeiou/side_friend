package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;

import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YF on 2018/8/31.
 */

@ZKAppAdapter("contacts")
public class Contacts extends ZKAdapter {
    public Contacts(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new ContactsHolder(mLayoutInflater.inflate(R.layout.item_friend,viewGroup,false));
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

    private class ContactsHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView img;
        private ZKPView name,content;
        private TextView time,corner;
        private RelativeLayout cornerLayout;
        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            cornerLayout = itemView.findViewById(R.id.cornerLayout);
            corner = itemView.findViewById(R.id.corner);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "content":
                        content.setFaceText(object.getString(key));
                        break;
                    case "title":
                        name.setFaceText(object.getString(key));
                        break;
                    case "createTime":
                        time.setText(addStringTime(object.getString(key)));
                        break;
                    case "newsNum":
                        int newsNum = object.getInteger(key);
                        corner.setText(newsNum+"");
                        corner.setVisibility(newsNum==0?View.GONE:View.VISIBLE);
                        cornerLayout.setVisibility(newsNum==0?View.GONE:View.VISIBLE);
                        break;
                }
            }
        }
    }

    private String addStringTime(String time) {
        SimpleDateFormat showFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");// HH:mm:ss
        try {
            Date serviceTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(time);
            Date currentTime = new Date(System.currentTimeMillis());
            String serviceText = showFormat.format(serviceTime);
            String currentText = showFormat.format(currentTime);
            if (serviceText.split(" ")[0].equals(currentText.split(" ")[0])){
                return serviceText.split(" ")[1];
            }else {
                return serviceText.split(" ")[0];
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
