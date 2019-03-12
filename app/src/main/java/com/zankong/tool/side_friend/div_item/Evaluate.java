package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.util.transformations.ZKCircleTransform;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;

import org.dom4j.Element;

@ZKAppAdapter("evaluate")
public class Evaluate extends ZKAdapter {
    public Evaluate(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }
    private RequestOptions options = new RequestOptions().skipMemoryCache(false).dontAnimate().transforms(
            new ZKCircleTransform(0, 800,0)
    );
    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new EvaluateHolder(mLayoutInflater.inflate(R.layout.item_evaluate,viewGroup,false));
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

    private class EvaluateHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView nickname,createdAt;
        private ZKPView description;
        private ImageView img;
        public EvaluateHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            createdAt = itemView.findViewById(R.id.createdAt);
            description = itemView.findViewById(R.id.description);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).apply(options).into(img);
                        break;
                    case "nickname":
                        nickname.setText(object.getString(key));
                        break;
                    case "content":
                        description.setFaceText(object.getString(key));
                        break;
                    case "createdAt":
                        createdAt.setText(object.getString(key));
                        break;
                }
            }
        }
    }
}
