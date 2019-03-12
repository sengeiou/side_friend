package com.zankong.tool.side_friend.div_item;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.views.ZKImgView;
import org.dom4j.Attribute;
import org.dom4j.Element;
import java.util.HashMap;

/**
 * Created by YF on 2018/9/17.
 */
@ZKAppAdapter("task_receiver_list")
public class Task_Receiver_List extends ZKAdapter {
    private HashMap<String,String> mClickMap;
    public Task_Receiver_List(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        mClickMap = new HashMap<>();
        for (Attribute attribute : element.element("task_receiver_list").attributes()) {
            mClickMap.put(attribute.getName(),attribute.getValue());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new ReceiverListHolder(mLayoutInflater.inflate(R.layout.item_task_receiver_list,viewGroup,false));
    }
    @Override
    protected int getViewType(int position) {
        return 0;
    }
    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class ReceiverListHolder extends RecyclerView.ViewHolder implements HolderInit {
        RoundedImageView header;
        TextView nickname,credit,userOrders,evaluate,bargainingPrice,applyTime,agree;
        private final RelativeLayout agreeLayout;
        public ReceiverListHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            agree = itemView.findViewById(R.id.agree);
            nickname = itemView.findViewById(R.id.nickname);
            credit = itemView.findViewById(R.id.credit);
            userOrders = itemView.findViewById(R.id.userOrders);
            evaluate = itemView.findViewById(R.id.evaluate);
            bargainingPrice = itemView.findViewById(R.id.bargainingPrice);
            applyTime = itemView.findViewById(R.id.applyTime);
            agreeLayout = itemView.findViewById(R.id.agreeLayout);
        }
        @Override
        public void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(header);
                        break;
                    case "nickname":
                        nickname.setText(v8Object.getString(key));
                        break;
                    case "userOrders":
                        userOrders.setText("订单量："+v8Object.getInteger(key));
                        break;
                    case "credit":
                        credit.setText(v8Object.getString(key));
                        break;
                    case "applyTime":
                        applyTime.setText("抢单时间："+v8Object.getString(key));
                        break;
                    case "bargainingPrice":
                        bargainingPrice.setText(v8Object.get(key)+"");
                        break;
                    case "evaluate":
                        evaluate.setText("评论数:"+v8Object.getInteger(key));
                        break;
                    case "isEstablish":
                        agree.setText(v8Object.getBoolean(key)?"已同意":"同意");
                        break;
                }
            }
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("header")) {
                        invokeWithContext(mClickMap.get("header"),ReceiverListHolder.this);
                    }
                }
            });
            agreeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("agree")) {
                        invokeWithContext(mClickMap.get("agree"),ReceiverListHolder.this);
                    }
                }
            });
        }
    }
}
