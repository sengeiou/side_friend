package com.example.zkapp_map.hkdialog.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.SieveListBean;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.zankong.tool.zkapp.views.text.Text;

import java.util.List;

public class FragmentSieveItemAdapter extends RecyclerView.Adapter {

    private List<SieveListBean> listBeans;
    private Context context;

    public FragmentSieveItemAdapter(List<SieveListBean> listBeans) {
        this.listBeans = listBeans;
        Log.e("listSiz","");
    }

    public class VH extends RecyclerView.ViewHolder{
        private final TextView tvSieve;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvSieve = itemView.findViewById(R.id.tv_sieve);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.sieve_item,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh = (VH) viewHolder;
        vh.tvSieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.tvSieve.setBackgroundResource(R.drawable.btn_bg_blue);
                Toast.makeText(context, vh.tvSieve.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        vh.tvSieve.setText(listBeans.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }
}
