package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.side_friend.R;

import org.dom4j.Element;

/**
 * Created by YF on 2018/6/26.
 */

@ZKAppAdapter("asdf")
public class asdf extends ZKAdapter {
    public asdf(ZKDocument zkDocument,Element element) {
        super( zkDocument,element);
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) return new TestViewHolder2(mLayoutInflater.inflate(R.layout.item_test_view,viewGroup,false));
        return new TestViewHolder(mLayoutInflater.inflate(R.layout.item_test_view,viewGroup,false));

    }

    @Override
    protected int getViewType(int position) {
        if (position == 1)return 1;
        return 0;
    }


    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof TestViewHolder){
            TestViewHolder testViewHolder = (TestViewHolder) viewHolder;
            testViewHolder.init(position);
        }else {
            TestViewHolder2 testViewHolder = (TestViewHolder2) viewHolder;
            testViewHolder.init(position);
        }
    }

    private class TestViewHolder extends RecyclerView.ViewHolder{
        private TextView test;

        private TestViewHolder(@NonNull View itemView) {
            super(itemView);
            test = itemView.findViewById(R.id.test);
        }
        private void init(int position){
            test.setText(position+"");
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.log("ffffffff");
                }
            });
        }
    }
    private class TestViewHolder2 extends RecyclerView.ViewHolder{
        private TextView test;

        private TestViewHolder2(@NonNull View itemView) {
            super(itemView);
            test = itemView.findViewById(R.id.test);
        }
        private void init(int position){
            test.setText("dddddddddddddddddd");
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.log("dddddddddddddd");
                }
            });
        }
    }
}
