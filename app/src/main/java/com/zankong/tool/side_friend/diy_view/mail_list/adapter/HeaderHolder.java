package com.zankong.tool.side_friend.diy_view.mail_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;


/**
 * Created by you on 2017/9/12.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    public final TextView tv_header;
    public HeaderHolder(View itemView) {
        super(itemView);
        tv_header = (TextView) itemView.findViewById(R.id.tv_header);
    }
}
