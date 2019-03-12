package com.zankong.tool.side_friend.div_item;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

@ZKAppAdapter("navigation")
public class NavigationItem extends ZKAdapter {
    public NavigationItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new NavigationHolder(mLayoutInflater.inflate(R.layout.item_naivegation,viewGroup,false));
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

    private class NavigationHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RelativeLayout point,button;
        private ImageView image;
        private TextView text;
        public NavigationHolder(@NonNull View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.point);
            button = itemView.findViewById(R.id.button);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            if (object.getBoolean("show")) {
                for (String s : object.getKeys()) {
                    switch (s) {
                        case "src_fill":
                            Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(s))).into(image);
                            break;
                        case "color_fill":
                            text.setTextColor(Color.parseColor(object.getString(s)));
                            break;
                        case "point":
                            if (object.getBoolean(s)) {
                                point.setVisibility(View.VISIBLE);
                            }else {
                                point.setVisibility(View.GONE);
                            }
                            break;
                        case "text":
                            text.setText(object.getString(s));
                            break;
                    }
                }
            }else {
                for (String s : object.getKeys()) {
                    switch (s) {
                        case "src":
                            Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(s))).into(image);
                            break;
                        case "color":
                            text.setTextColor(Color.parseColor(object.getString(s)));
                            break;
                        case "point":
                            if (object.getBoolean(s)) {
                                point.setVisibility(View.VISIBLE);
                            }else {
                                point.setVisibility(View.GONE);
                            }
                            break;
                        case "text":
                            text.setText(object.getString(s));
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void initV8this() {
        super.initV8this();
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int integer = parameters.getInteger(0);
                mList.get(integer).add("point",parameters.getBoolean(1));
                notifyItemChanged(integer);
                return null;
            }
        },"setPoint");
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                int integer = parameters.getInteger(0);
                for (V8Object object : mList) {
                    object.add("show",false);
                }
                mList.get(integer).add("show",true);
                notifyDataSetChanged();
                return null;
            }
        },"show");
    }
}
