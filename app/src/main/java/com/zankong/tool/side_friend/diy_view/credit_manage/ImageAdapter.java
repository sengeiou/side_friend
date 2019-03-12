package com.zankong.tool.side_friend.diy_view.credit_manage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;

import java.util.List;

/**
 * @author Fsnzzz
 * @Created on 2018/11/29 0029 10:28
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImgViewHolder>{



    private Context mContext;
    private List<String> imgs;

    public ImageAdapter(Context mContext,List<String> imgs){
        this.mContext = mContext;
        this.imgs = imgs;
    }
    @NonNull
    @Override
    public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.credit_img_adapter, viewGroup, false);
        return new ImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder imgViewHolder, int i) {

        if (imgViewHolder instanceof ImgViewHolder){
            Glide.with(mContext)
                    .load(imgs.get(i))
                    .into(imgViewHolder.img);
        }
    }

    @Override
    public int getItemCount() {
        return imgs == null ? 0 : imgs.size();
    }

    public class ImgViewHolder extends RecyclerView.ViewHolder{

        private final RoundedImageView img;

        public ImgViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (RoundedImageView)itemView.findViewById(R.id.img);
        }
    }
}
