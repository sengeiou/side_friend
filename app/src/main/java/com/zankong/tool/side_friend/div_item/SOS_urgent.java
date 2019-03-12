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

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by Fsnzzz on 2018/9/17.
 */

@ZKAppAdapter("sos_urgent")
public class SOS_urgent extends ZKAdapter {
    public SOS_urgent(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {


        return new SOS_urgent.ViewHolder(mLayoutInflater.inflate(R.layout.sos_urgent, viewGroup, false));

    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).init(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView sos_name;
        private final TextView sos_sex;
        private final TextView sos_phone;
        private final TextView sos_address;
        private final ImageView sos_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sos_img = itemView.findViewById(R.id.img_right);
            sos_name = itemView.findViewById(R.id.sos_name);
            sos_sex = itemView.findViewById(R.id.sos_sex);
            sos_phone = itemView.findViewById(R.id.sos_phone);
            sos_address = itemView.findViewById(R.id.sos_address);
        }

        private void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "sos_name":
                        sos_name.setText(v8Object.getString(key));
                        break;
                    case "sos_sex":
                        sos_sex.setText(v8Object.getString(key));
                        break;
                    case "sos_phone":
                        sos_phone.setText(v8Object.getString(key));
                        break;
                    case "sos_address":
                        sos_address.setText(v8Object.getString(key));
                        break;
                    case "sos_img":
                        break;

                }
            }
        }
    }
}
