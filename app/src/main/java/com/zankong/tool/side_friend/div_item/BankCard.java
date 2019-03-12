package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@ZKAppAdapter("bankCard")
public class BankCard extends ZKAdapter {
    private List<Integer> list;

    public BankCard(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new BankHolder(mLayoutInflater.inflate(R.layout.choise_blank_card_layout, viewGroup, false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            if (list == null) {
                list = new ArrayList<>();
            }
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class BankHolder extends RecyclerView.ViewHolder implements HolderInit {

        private final RelativeLayout item_blank;
        private final ImageView icon_blank;

        public BankHolder(@NonNull View itemView) {
            super(itemView);
            item_blank = itemView.findViewById(R.id.item_blank);
            icon_blank = itemView.findViewById(R.id.check_item);
        }

        @Override
        public void init(int position) {

            if (list.contains(position)) {
                Glide.with(getContext())
                        .load(R.drawable.icon_choice_press)
                        .into(icon_blank);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.blank_choice_nor)
                        .into(icon_blank);
            }
            item_blank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.contains(position)) {
                        Glide.with(getContext())
                                .load(R.drawable.blank_choice_nor)
                                .into(icon_blank);
                        list.clear();
                        notifyDataSetChanged();
                    } else {
                        list.clear();
                        list.clear();
                        list.add(position);
                        Glide.with(getContext())
                                .load(R.drawable.icon_choice_press)
                                .into(icon_blank);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
