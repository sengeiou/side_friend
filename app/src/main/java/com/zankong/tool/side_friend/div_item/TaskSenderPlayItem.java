package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;


@ZKAppAdapter("play_item")
public class TaskSenderPlayItem extends ZKAdapter {
    public TaskSenderPlayItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {

        View inflate = mLayoutInflater.inflate(R.layout.task_sender_play_item, viewGroup, false);

        return new ViewHodler(inflate);
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }
    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    }
    public class ViewHodler extends RecyclerView.ViewHolder implements HolderInit {
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void init(int position) {
            
        }
    }
}
