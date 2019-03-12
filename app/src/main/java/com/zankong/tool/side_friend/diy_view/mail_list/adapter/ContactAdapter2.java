package com.zankong.tool.side_friend.diy_view.mail_list.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.mail_list.cn.CNPinyin;
import com.zankong.tool.side_friend.diy_view.mail_list.search.Contact;
import com.zankong.tool.zkapp.views.ZKImgView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by you on 2017/9/11.
 */

public class ContactAdapter2 extends RecyclerView.Adapter<ContactAdapter2.ContactHolder> {

    private List<CNPinyin<Contact>> cnPinyinList;

    private Map<Integer, Boolean> sele;
    private MailListGroup mMailListGroup;
    private Context mContext;
    private Map<Integer,Object> selMap = new HashMap<>();

    public ContactAdapter2(List<CNPinyin<Contact>> cnPinyinList, Context context) {
        this.cnPinyinList = cnPinyinList;
        this.mContext = context;

    }

    public void addData(List<CNPinyin<Contact>> cnPinyinList) {
        this.cnPinyinList = cnPinyinList;
        sele = new HashMap<>();
        setBoolean(this.cnPinyinList);
        notifyDataSetChanged();
    }

    public void onItemClickListener(MailListGroup mailListGroup) {
        this.mMailListGroup = mailListGroup;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContactHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mail_list_item2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int i) {
        Contact contact = cnPinyinList.get(i).data;
        if (contact.isStatus()) {
            holder.btn_mail.setBackgroundResource(R.drawable.mail_btn_sel_pree);
        } else
            holder.btn_mail.setBackgroundResource(R.drawable.mail_btn_sel_nor);
        int section = getSectionForPosition(i);
        if (i == getPositionForSection(section)) {
            holder.pinyin_layout.setVisibility(View.VISIBLE);
            holder.pinyin_tv.setText(cnPinyinList.get(i).getFirstChar() + "");
        } else {
            holder.pinyin_layout.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(ZKImgView.getUrl(contact.imgUrl))
                .into(holder.iv_header);
        holder.tv_name.setText(contact.name);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.isStatus()) {
                    contact.setStatus(false);
                    holder.btn_mail.setBackgroundResource(R.drawable.mail_btn_sel_nor);
                    selMap = onMapData(contact.iD, contact);
                } else {
                    holder.btn_mail.setBackgroundResource(R.drawable.mail_btn_sel_pree);
                    contact.setStatus(true);
                    selMap = onMapData(contact.iD,contact);
                }


                mMailListGroup.onData(selMap);



            }
        });
    }

    @Override
    public int getItemCount() {
        return cnPinyinList.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        public final ImageView iv_header;

        public final TextView tv_name, pinyin_tv;
        private final LinearLayout pinyin_layout,item_layout;
        private final Button btn_mail;

        public ContactHolder(View itemView) {
            super(itemView);
            iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            pinyin_tv = (TextView) itemView.findViewById(R.id.pinyin_tv);

            btn_mail = (Button) itemView.findViewById(R.id.btn_mail);
            pinyin_layout = (LinearLayout) itemView.findViewById(R.id.layout_pinyin);
            item_layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        }
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            char firstChar = cnPinyinList.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return cnPinyinList.get(position).getFirstChar();
    }


    private void setBoolean(List<CNPinyin<Contact>> list) {
        for (int i = 0; i < list.size(); i++) {
            Contact data = list.get(i).data;
            sele.put(i, false);
            data.setStatus(false);
        }
    }


    public interface MailListGroup {
        void onData(Map<Integer, Object> data);

    }



    private Map<Integer,Object> onMapData(int uid,Contact contact){
       if (selMap.get(uid) == null){
           selMap.put(uid,contact);
       }else {
           selMap.remove(uid);
       }

       return selMap;
    }
}
