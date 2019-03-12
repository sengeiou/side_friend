package com.example.zkapp_map.hkdialog.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.TypesNameBean;
import com.example.zkapp_map.hkdialog.callback.ExpandableViewHoldersUtil;
import com.zankong.tool.zkapp.views.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemVH>{

    private Context context;
    private List<List<TypesNameBean>>list;
    private RecyclerViewAdapter mParentAdapter;
    private int mIndex;
    private RecyclerViewAdapter.VH mParentVH;
    public ItemVH openedItemVH;
    private int lastPosition = 0;

    /**
     * 判断是否是历史记录打开
     */
    private boolean isOld = false;


    /**
     * 记录gv1的点击position
     */
    private int gv1Position;
    private SharedPreferences type;
    private int listPosition;
    private int oldGv1Position;

    public ItemRecyclerViewAdapter(List<List<TypesNameBean>>list,RecyclerViewAdapter parentAdapter,RecyclerViewAdapter.VH vh) {
        this.list=list;
        mParentAdapter = parentAdapter;
        mParentVH = vh;
    }
    @NonNull
    @Override
    public ItemVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_item,viewGroup,false);
        return new ItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVH itemVH, int i) {
        itemVH.bind(i);

        type = context.getSharedPreferences("type", Context.MODE_PRIVATE);
        listPosition = type.getInt("listPosition", -1);
        if (listPosition == i){
            Message msg = new Message();
            msg.obj = itemVH;
            msg.what = 1;
            //handler.sendMessageDelayed(msg,500);
        }

        itemVH.gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gv1Position = position;
                itemVH.gv2.setAdapter(new GVAdapter2(list.get(i), context, position));
//                keepOne.toggle(itemVH,i,position);
                //Log.e("position",""+mParentVH.getAdapterPosition());
                mIndex = mParentVH.getAdapterPosition();
                mParentAdapter.change(itemVH,view,i,position,mIndex);
            }
        });

        itemVH.gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click(i,view, position);
                mIndex = mParentVH.getAdapterPosition();
                mParentAdapter.change(itemVH,view,i,gv1Position,mIndex);
            }
        });

    }

    private void click(int i,View view, int position) {
        SharedPreferences sp = context.getSharedPreferences("type", Context.MODE_PRIVATE);
        if (isOld){
            isOld = false;
            List<JSONBean.TypesBean> typesBeans = list.get(listPosition).get(oldGv1Position).getTypesBeans();
        }else {
            /**
             * 获取gv2点击的文本
             */
            List<JSONBean.TypesBean> typesBeans = list.get(i).get(gv1Position).getTypesBeans();
            String name = typesBeans.get(position).getName();
            //sp.edit().putString("typeName",name).putInt("mIndex",mIndex).putInt("listPosition",i).putInt("gv1Position",gv1Position).putInt("gv2Position",position).commit();
            Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ExpandableViewHoldersUtil.KeepOneH<ItemVH> keepOne = new ExpandableViewHoldersUtil.KeepOneH<ItemVH>();
    public class ItemVH extends RecyclerView.ViewHolder implements ExpandableViewHoldersUtil.Expandable{
        private final GridView gv1;
        private final GridView gv2;
        //private final TextView tv;
        public ItemVH(@NonNull View itemView) {
            super(itemView);
            gv1 = itemView.findViewById(R.id.gv1);
            //tv = itemView.findViewById(R.id.tv);
            //gv1.setOnItemClickListener(this);
            gv2 = itemView.findViewById(R.id.gv2);
        }

        public void bind(int pos) {
            for (int k=0;k<list.get(pos).size();k++){
                gv1.setAdapter(new GVAdapter1(list.get(pos),context));
            }
            keepOne.bind(this, pos);
        }
        @Override
        public View getExpandView() {
            return gv2;
        }
    }

}
