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
 * @Created on 2018/10/17 0017 16:36
 */
@ZKAppAdapter("notification")
public class Notification extends ZKAdapter{
    public Notification(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new Notification.ViewHolder(mLayoutInflater.inflate(R.layout.notification,viewGroup,false));
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

        private  TextView time;
        private  TextView notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = (TextView)itemView.findViewById(R.id.time);
            notification = (TextView)itemView.findViewById(R.id.notification);

          //  itemView.findViewById(R.id.right_img).setVisibility(View.VISIBLE);
        }

        private void init(int position){
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "time":
                        time.setText(v8Object.getString(key));
                        break;
                    case "notification":
                        notification.setText(v8Object.getString(key));
                        break;
                }
            }
        }
    }
}
