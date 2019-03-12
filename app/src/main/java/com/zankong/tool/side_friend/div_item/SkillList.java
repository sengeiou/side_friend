package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;

@ZKAppAdapter("skillList")
public class SkillList extends ZKAdapter {
    private final int ADD   =   1,
                       SKILL =   0;

    public SkillList(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        switch (i){
            case ADD:
                holder = new AddHolder(mLayoutInflater.inflate(R.layout.item_skill_add,viewGroup,false));
                break;
            case SKILL:
                holder = new SkillHolder(mLayoutInflater.inflate(R.layout.item_skill_skill,viewGroup,false));
                break;
        }
        return holder;
    }

    @Override
    protected int getViewType(int position) {
        V8Object object = mList.get(position);
        boolean isAdd = false;
        for (String s : object.getKeys()) {
            switch (s){
                case "isAdd":
                    isAdd = true;
                    break;
            }
        }
        if(isAdd){
            return ADD;
        }else {
            return SKILL;
        }
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class AddHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RelativeLayout add;
        public AddHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
        }

        @Override
        public void init(int position) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getClickMap().containsKey("add")){
                        invokeWithContext(getClickMap().get("add"),AddHolder.this);
                    }
                }
            });
        }
    }

    private class SkillHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RelativeLayout skill;

        public SkillHolder(@NonNull View itemView) {
            super(itemView);
            skill = itemView.findViewById(R.id.skill);
        }

        @Override
        public void init(int position) {
            skill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getClickMap().containsKey("skill")) {
                        invokeWithContext(getClickMap().get("skill"),SkillHolder.this);
                    }
                }
            });
        }
    }
}
