package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;

@ZKAppAdapter("edit_experience")
public class MessageEditExperience extends ZKAdapter {
    public MessageEditExperience(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }


    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        holder = new ExperienceHolder(mLayoutInflater.inflate(R.layout.message_edit_experience_item,viewGroup,false));
        return holder;
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ExperienceHolder){
            ((ExperienceHolder) viewHolder).init(position);
        }
    }
    
    
    
    
    private class ExperienceHolder extends RecyclerView.ViewHolder implements HolderInit {

        private final TextView compant_name;
        private final TextView time;
        private final TextView job;
        private final TextView context;

        public ExperienceHolder(@NonNull View itemView) {
            super(itemView);
            compant_name = itemView.findViewById(R.id.tv_company_name);
            time = itemView.findViewById(R.id.tv_time);
            job = itemView.findViewById(R.id.tv_job);
            context = itemView.findViewById(R.id.tv_context);
        }
        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "src":
                        V8Object parse = V8Utils.parse(object.getString(key));
                        for (String s : parse.getKeys()){
                            switch (s){
                                case "compant_name":
                                    compant_name.setText(parse.getString(s));
                                    break;
                                case "time":
                                    time.setText(parse.getString(s));
                                    break;
                                case "job":
                                    job.setText(parse.getString(s));
                                    break;
                                case "context":
                                    context.setText(parse.getString(s));
                                    break;
                            }
                        }
                        break;
                }
            }
        }
    }
}
