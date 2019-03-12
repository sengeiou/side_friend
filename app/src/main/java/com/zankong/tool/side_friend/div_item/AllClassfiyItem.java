package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/11/21 0021 17:35
 */
@ZKAppAdapter("allclassfiy")
public class AllClassfiyItem extends ZKAdapter {

    public AllClassfiyItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHorlder(mLayoutInflater.inflate(R.layout.all_classfig_item, viewGroup, false));

    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHorlder){
            ((ViewHorlder) viewHolder).init(position);
        }
    }



    public class ViewHorlder extends RecyclerView.ViewHolder{

        private final RoundedImageView img;
        private final TextView tv;

        public ViewHorlder(@NonNull View itemView) {
            super(itemView);
            img = (RoundedImageView)itemView.findViewById(R.id.img);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }


        private void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                           Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(img);
                        break;
                    case "name":
                        tv.setText(v8Object.getString(key));
                        break;

                }
            }
        }
    }
}
