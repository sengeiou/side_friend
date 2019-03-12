package com.zankong.tool.side_friend.div_item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.screen.Screen_recruit;
import com.zankong.tool.side_friend.domain.TypeJSONBean;

import java.util.List;

public class TypeRecyclerAdapter extends RecyclerView.Adapter<TypeRecyclerAdapter.TypeVH> {

    private Context context;
    private int mIndex;
    private Screen_recruit screen_recruit;

    public class TypeVH extends RecyclerView.ViewHolder{
        private final GridView gv1;
        private final GridView gv2;
        public TypeVH(@NonNull View itemView) {
            super(itemView);
            gv1 = itemView.findViewById(R.id.gv1);
            gv2 = itemView.findViewById(R.id.gv2);
            gv2.setVisibility(View.GONE);
        }
    }
    private List<List<TypeJSONBean>> listList;
    public TypeRecyclerAdapter(List<List<TypeJSONBean>> listList, Screen_recruit screen_recruit) {
        this.listList = listList;
        this.screen_recruit = screen_recruit;
    }

    @NonNull
    @Override
    public TypeVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.type_recycler_view_item,viewGroup,false);
        return new TypeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeVH typeVH, int i) {
        typeVH.gv1.setAdapter(new TypeGrid1Adapter(listList.get(i),context));
        typeVH.gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<TypeJSONBean.TypesBean> types = listList.get(i).get(position).getTypes();
                if (typeVH.gv2.getVisibility()==View.VISIBLE){
                    typeVH.gv2.setVisibility(View.GONE);
                }else {
                    typeVH.gv2.setAdapter(new TypeGrid2Adapter(types, context));
                    typeVH.gv2.setVisibility(View.VISIBLE);
                }
            }
        });
        typeVH.gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                screen_recruit.getTypeOnClickIndex(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listList.size();
    }

}
