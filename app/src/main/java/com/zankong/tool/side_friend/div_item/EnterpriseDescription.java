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
import org.json.JSONException;
import org.json.JSONObject;

@ZKAppAdapter("enterpriseDescription")
public class EnterpriseDescription extends ZKAdapter {
    public EnterpriseDescription(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new DescriptionHolder(mLayoutInflater.inflate(R.layout.item_enterprise_description,viewGroup,false));
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

    private class DescriptionHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView title,content;
        public DescriptionHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "src":
                        try {
                            JSONObject jsonObject = V8Utils.V82JSON(V8Utils.parse(object.getString(key)));
                            title.setText(jsonObject.optString("title"));
                            content.setText(jsonObject.optString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }
}
