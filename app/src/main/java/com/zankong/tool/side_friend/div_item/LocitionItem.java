package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;

import org.dom4j.Element;

/**
 * @author Fsnzzz
 * @Created on 2018/10/11 0011 16:27
 */
@ZKAppAdapter("locationItem")
public class LocitionItem extends ZKAdapter {
    public LocitionItem(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {


        return new LocitionItem.ViewHolder(mLayoutInflater.inflate(R.layout.location_item,viewGroup,false));

    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder instanceof ViewHolder){
            ((ViewHolder) viewHolder).init(position);


        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        private  TextView textName;
        private  TextView textAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textAddress = (TextView) itemView.findViewById(R.id.textAddress);



        }

        private void init(int position){
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "name":
                        textName.setText(v8Object.getString(key));
                        break;
                    case "address":
                        textAddress.setText(v8Object.getString(key));
                        break;

                }
            }
        }
    }
}
