package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;

@ZKAppAdapter("recruit")
public class RecruitReleaseItem extends ZKAdapter {
    public RecruitReleaseItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new RecruitReleaseHolder(mLayoutInflater.inflate(R.layout.item_recruit_release,viewGroup,false));
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

    private class RecruitReleaseHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView styleName,typeName,recruitNum,reward,createTime,orderId,applyNum;
        private LinearLayout applyList;
        private RelativeLayout change,stop;
        public RecruitReleaseHolder(@NonNull View itemView) {
            super(itemView);
            styleName = itemView.findViewById(R.id.styleName);
            typeName = itemView.findViewById(R.id.typeName);
            recruitNum = itemView.findViewById(R.id.recruitNum);
            reward = itemView.findViewById(R.id.reward);
            createTime = itemView.findViewById(R.id.createTime);
            orderId = itemView.findViewById(R.id.orderId);
            applyNum = itemView.findViewById(R.id.applyNum);
            applyList = itemView.findViewById(R.id.applyList);
            change = itemView.findViewById(R.id.change);
            stop = itemView.findViewById(R.id.stop);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            applyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("applyList")) {
                        invokeWithContext(getClickMap().get("applyList"),RecruitReleaseHolder.this);
                    }
                }
            });
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("change")) {
                        invokeWithContext(getClickMap().get("change"),RecruitReleaseHolder.this);
                    }
                }
            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("stop")) {
                        invokeWithContext(getClickMap().get("stop"),RecruitReleaseHolder.this);
                    }
                }
            });
        }
    }
}
