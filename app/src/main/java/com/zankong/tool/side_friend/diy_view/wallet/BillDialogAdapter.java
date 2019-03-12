package com.zankong.tool.side_friend.diy_view.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zankong.tool.side_friend.R;

import java.util.List;

public class BillDialogAdapter extends RecyclerView.Adapter<BillDialogAdapter.ViewHordler> {


    private Context mContext;
    private List<String> list;
    public BillDialogAdapter(Context mContext,List<String> list){
        this.mContext = mContext;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHordler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.bill_dialog_item, viewGroup, false);
        
        return new ViewHordler(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHordler viewHordler, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHordler extends RecyclerView.ViewHolder{
        public ViewHordler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
