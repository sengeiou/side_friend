package com.zankong.tool.zkapp.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;

import org.dom4j.Element;

/**
 * Created by YF on 2018/7/10.
 */

public class NullAdapter extends ZKAdapter {
    public NullAdapter(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new NullViewHolder(mLayoutInflater.inflate(R.layout.item_null_view,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof NullViewHolder){

        }
    }

    private class NullViewHolder extends RecyclerView.ViewHolder{

        public NullViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
