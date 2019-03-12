package com.example.zkapp_map.hkdialog.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.ServiceBean;
import com.example.zkapp_map.bean.TypesNameBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter{

    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private VH vh;
    private List<List<TypesNameBean>> listList;
    private ArrayList<ItemRecyclerViewAdapter> adapterList = new ArrayList<>();
    private RecyclerView mParentRv;

    public class VH extends RecyclerView.ViewHolder {
        private final TextView tvItem;
        private final RecyclerView itemRecyclerView;
        public VH(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_recycler_view_item);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private List<ServiceBean> dataList;
    private Context context;

    public RecyclerViewAdapter(List<ServiceBean> dataList, Context context,RecyclerView rv) {
        this.dataList = dataList;
        this.context = context;
        mParentRv = rv;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        vh = (VH) viewHolder;
        vh.tvItem.setText(dataList.get(i).getName());
        List<JSONBean> list = dataList.get(i).getList();

        List<TypesNameBean> styleNameBean = new ArrayList<>();
        /**
         * list集合4个一组
         */
        listList = new ArrayList<>();

        int j = 0;
        for (int k=0;k<list.size();k++){
            TypesNameBean nameBean = new TypesNameBean(list.get(k).getStyleName(),list.get(k).getTypes());
            styleNameBean.add(nameBean);
            if (j==3){
                listList.add(styleNameBean);
                styleNameBean = new ArrayList<>();
                //styleNameBean.add(nameBean);
                j=-1;
            }
            j++;
        }
        if (list.size()%4!=0) {
            listList.add(styleNameBean);
        }


        /**
         * item的recyclerView设置
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(OrientationHelper. VERTICAL);
        vh.itemRecyclerView.setLayoutManager(linearLayoutManager);

        ItemRecyclerViewAdapter itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(listList,this,vh);
        adapterList.add(itemRecyclerViewAdapter);
        vh.itemRecyclerView.setAdapter(itemRecyclerViewAdapter);

    }
    private View mView;
    public void setOldView(View view){
        mView = view;
    }
    public View getOldView(){
        return mView;
    }
    public void clearOldView(){
        mView = null;
    }
    public void change(ItemRecyclerViewAdapter.ItemVH holder,View view,int i1,int position,int index){

        for (int i = 0; i < adapterList.size(); i++) {
            if(i == index){
                adapterList.get(i).openedItemVH = adapterList.get(i).keepOne.toggle(holder,view,i1,position,this);
            }else{
                adapterList.get(i).openedItemVH = adapterList.get(i).keepOne.hide(adapterList.get(i).openedItemVH,mView,this);
            }
        }
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
