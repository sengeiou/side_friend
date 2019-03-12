package com.zankong.tool.zkapp.item.example;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;

import org.dom4j.Element;

/**
 * Created by YF on 2018/8/14.
 */

public class LinkAdapter extends ZKAdapter {
    public LinkAdapter(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new LinkHolder(mLayoutInflater.inflate(R.layout.item_link_adapter,viewGroup,false));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LinkHolder) {
            ((LinkHolder) viewHolder).init(position);
        }
    }

    private class LinkHolder extends RecyclerView.ViewHolder{
        public LinkHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void init(int position){

        }
    }

}
